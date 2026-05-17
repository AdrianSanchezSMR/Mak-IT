package com.makit.tfg.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makit.tfg.data.CategoryOption
import com.makit.tfg.data.Challenge
import com.makit.tfg.data.MakItRepository
import com.makit.tfg.data.UserProfile
import com.makit.tfg.data.api.AuthResponse
import com.makit.tfg.data.api.ChallengeResponse
import com.makit.tfg.data.api.DailyProgressPoint
import com.makit.tfg.data.api.MeResponse
import com.makit.tfg.data.api.RetoCatalogoDto
import com.makit.tfg.data.formatHoraAviso
import kotlinx.coroutines.launch

class MakItAppState(
    private val repository: MakItRepository
) : ViewModel() {

    var isLoggedIn by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isRestoringSession by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var profile by mutableStateOf<UserProfile?>(null)
        private set

    var todayChallenge by mutableStateOf<Challenge?>(null)
        private set

    var todayChallenges by mutableStateOf<List<Challenge>>(emptyList())
        private set

    var catalogChallenges by mutableStateOf<List<Challenge>>(emptyList())
        private set

    var categories by mutableStateOf<List<CategoryOption>>(emptyList())
        private set

    var selectedInterestIds by mutableStateOf<Set<Long>>(emptySet())
        private set

    var weeklyProgress by mutableStateOf(0f)
        private set

    val isAdmin: Boolean
        get() = profile?.role == "ADMIN"

    init {
        viewModelScope.launch {
            isRestoringSession = true
            val restored = repository.restoreSession()
            if (restored) {
                isLoggedIn = true
                refreshAll()
            }
            isRestoringSession = false
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val auth = repository.login(username, password)
                completeAuth(auth)
                onSuccess()
                refreshAll()
            } catch (e: Exception) {
                errorMessage = MakItRepository.humanizeError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun register(
        username: String,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val auth = repository.register(username, email, password)
                completeAuth(auth, email.trim())
                onSuccess()
                refreshAll()
            } catch (e: Exception) {
                errorMessage = MakItRepository.humanizeError(e)
            } finally {
                isLoading = false
            }
        }
    }

    private fun completeAuth(auth: AuthResponse, email: String = profile?.email.orEmpty()) {
        isLoggedIn = true
        profile = UserProfile(
            username = auth.username,
            email = email,
            role = auth.role,
            streakDays = 0,
            completedCount = 0,
            dailyReminderHour = "--:--",
            activeCategoryIds = emptySet(),
            activeCategoryNames = emptyList()
        )
    }
    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            isLoggedIn = false
            profile = null
            todayChallenge = null
            todayChallenges = emptyList()
            catalogChallenges = emptyList()
            categories = emptyList()
            selectedInterestIds = emptySet()
            weeklyProgress = 0f
            onLoggedOut()
        }
    }

    fun refreshAll() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val me = repository.profile()
                val interestIds = runCatching { repository.getInterests() }.getOrDefault(emptyList())
                val allCategories = runCatching { repository.categorias() }.getOrDefault(emptyList())
                val summary = runCatching { repository.progressSummary() }.getOrNull()
                val retos = runCatching { repository.retos() }.getOrDefault(emptyList())
                val today = runCatching { repository.todayChallenges() }.getOrDefault(emptyList())

                categories = allCategories.map { CategoryOption(it.id, it.nombre) }
                selectedInterestIds = interestIds.toSet()
                profile = if (summary != null) {
                    buildProfile(me, interestIds, allCategories.map { it.id to it.nombre }, summary)
                } else {
                    profile?.copy(
                        username = me.username,
                        email = me.email,
                        role = me.role,
                        dailyReminderHour = formatHoraAviso(me.horaAviso),
                        activeCategoryIds = interestIds.toSet(),
                        activeCategoryNames = allCategories
                            .filter { it.id in interestIds }
                            .map { it.nombre }
                    ) ?: UserProfile(
                        username = me.username,
                        email = me.email,
                        role = me.role,
                        streakDays = 0,
                        completedCount = 0,
                        dailyReminderHour = formatHoraAviso(me.horaAviso),
                        activeCategoryIds = interestIds.toSet(),
                        activeCategoryNames = allCategories
                            .filter { it.id in interestIds }
                            .map { it.nombre }
                    )
                }
                weeklyProgress = ((summary?.tasaCompletado ?: 0.0) / 100.0).toFloat().coerceIn(0f, 1f)
                catalogChallenges = retos
                    .filter { it.activo && (interestIds.isEmpty() || it.categoria.id in interestIds) }
                    .map { it.toChallenge() }
                todayChallenges = today.map { it.toChallenge() }
                todayChallenge = todayChallenges.firstOrNull()
            } catch (e: Exception) {
                errorMessage = MakItRepository.humanizeError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun completeCheckIn(challenge: Challenge, onSuccess: () -> Unit) {
        if (challenge.isCompletedToday) return

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.checkIn(challenge.id)
                todayChallenges = todayChallenges.map {
                    if (it.id == challenge.id) it.copy(isCompletedToday = true) else it
                }
                todayChallenge = todayChallenges.firstOrNull()
                val summary = repository.progressSummary()
                val me = repository.profile()
                val interestIds = repository.getInterests()
                profile = profile?.copy(
                    completedCount = summary.totalRetosCompletados.toInt(),
                    streakDays = computeStreak(summary.serieDiaria)
                ) ?: buildProfile(
                    me,
                    interestIds,
                    categories.map { it.id to it.name },
                    summary
                )
                weeklyProgress = (summary.tasaCompletado / 100.0).toFloat().coerceIn(0f, 1f)
                onSuccess()
            } catch (e: Exception) {
                errorMessage = MakItRepository.humanizeError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun completeCheckIn(onSuccess: () -> Unit) {
        val challenge = todayChallenge ?: return
        completeCheckIn(challenge, onSuccess)
    }

    fun updateInterests(ids: Set<Long>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val saved = repository.updateInterests(ids.toList())
                selectedInterestIds = saved.toSet()
                val me = repository.profile()
                val summary = runCatching { repository.progressSummary() }.getOrNull()
                val retos = runCatching { repository.retos() }.getOrDefault(emptyList())
                val today = runCatching { repository.todayChallenges() }.getOrDefault(emptyList())

                profile = if (summary != null) {
                    buildProfile(
                        me,
                        saved,
                        categories.map { it.id to it.name },
                        summary
                    )
                } else {
                    profile?.copy(
                        username = me.username,
                        email = me.email,
                        role = me.role,
                        activeCategoryIds = saved.toSet(),
                        activeCategoryNames = categories
                            .filter { it.id in saved }
                            .map { it.name }
                    )
                }
                catalogChallenges = retos
                    .filter { it.activo && (saved.isEmpty() || it.categoria.id in saved) }
                    .map { it.toChallenge() }
                todayChallenges = today.map { it.toChallenge() }
                todayChallenge = todayChallenges.firstOrNull()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = MakItRepository.humanizeError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun updateReminderHour(hora: String, onSuccess: () -> Unit) {
        val current = profile ?: return
        val horaApi = if (hora.length == 5) "$hora:00" else hora

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val me = repository.updateProfile(current.username, current.email, horaApi)
                val interestIds = repository.getInterests()
                val summary = repository.progressSummary()
                profile = buildProfile(
                    me,
                    interestIds,
                    categories.map { it.id to it.name },
                    summary
                )
                onSuccess()
            } catch (e: Exception) {
                errorMessage = MakItRepository.humanizeError(e)
            } finally {
                isLoading = false
            }
        }
    }

    fun createCatalogReto(
        categoriaId: Long,
        titulo: String,
        descripcion: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                repository.createReto(categoriaId, titulo, descripcion.ifBlank { null })
                refreshAll()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = MakItRepository.humanizeError(e)
            } finally {
                isLoading = false
            }
        }
    }

    private fun buildProfile(
        me: MeResponse,
        interestIds: List<Long>,
        categoryNames: List<Pair<Long, String>>,
        summary: com.makit.tfg.data.api.ProgressSummaryResponse
    ): UserProfile {
        val names = categoryNames
            .filter { (id, _) -> id in interestIds }
            .map { (_, name) -> name }
        return UserProfile(
            username = me.username,
            email = me.email,
            role = me.role,
            streakDays = computeStreak(summary.serieDiaria),
            completedCount = summary.totalRetosCompletados.toInt(),
            dailyReminderHour = formatHoraAviso(me.horaAviso),
            activeCategoryIds = interestIds.toSet(),
            activeCategoryNames = names
        )
    }

    private fun computeStreak(serie: List<DailyProgressPoint>): Int {
        var streak = 0
        for (point in serie.asReversed()) {
            if (point.completado) streak++ else break
        }
        return streak
    }

    private fun ChallengeResponse.toChallenge() = Challenge(
        id = id,
        title = titulo,
        description = descripcion.orEmpty(),
        categoryName = categoriaNombre,
        categoryId = categoriaId,
        isCompletedToday = completadoHoy,
        isActive = true
    )

    private fun RetoCatalogoDto.toChallenge() = Challenge(
        id = id,
        title = titulo,
        description = descripcion.orEmpty(),
        categoryName = categoria.nombre,
        categoryId = categoria.id,
        isActive = activo
    )
}





