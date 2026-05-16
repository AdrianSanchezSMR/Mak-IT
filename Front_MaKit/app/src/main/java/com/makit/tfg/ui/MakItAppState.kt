package com.makit.tfg.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.makit.tfg.data.Challenge
import com.makit.tfg.data.ChallengeCategory
import com.makit.tfg.data.Difficulty
import com.makit.tfg.data.UserProfile
import java.util.UUID

class MakItAppState : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
        private set

    val profile = UserProfile(
        name = "Ana Navarro",
        email = "ana@ejemplo.com",
        initials = "AN",
        streakDays = 7,
        completedCount = 42
    )

    private val _challenges = mutableStateListOf(
        Challenge(
            id = "1",
            title = "Medita 10 minutos sin distracciones",
            description = "Busca un lugar tranquilo y concéntrate en tu respiración durante 10 minutos.",
            category = ChallengeCategory.Mindfulness,
            difficulty = Difficulty.Media,
            durationMinutes = 10,
            isToday = true
        ),
        Challenge(
            id = "2",
            title = "Medita 10 minutos",
            description = "Sesión de mindfulness matutina.",
            category = ChallengeCategory.Mindfulness,
            difficulty = Difficulty.Media,
            durationMinutes = 10
        ),
        Challenge(
            id = "3",
            title = "Leer 20 páginas",
            description = "Lee un capítulo de tu libro actual.",
            category = ChallengeCategory.Lectura,
            difficulty = Difficulty.Facil,
            durationMinutes = 30
        )
    )
    val challenges: List<Challenge> get() = _challenges

    val todayChallenge: Challenge?
        get() = _challenges.firstOrNull { it.isToday } ?: _challenges.firstOrNull()

    fun login(email: String, @Suppress("UNUSED_PARAMETER") password: String) {
        isLoggedIn = email.isNotBlank()
    }

    fun logout() {
        isLoggedIn = false
    }

    fun completeCheckIn() {
        val index = _challenges.indexOfFirst { it.isToday }
        if (index >= 0) {
            val current = _challenges[index]
            _challenges[index] = current.copy(isToday = false)
        }
    }

    fun addChallenge(
        title: String,
        description: String,
        category: ChallengeCategory,
        difficulty: Difficulty
    ) {
        _challenges.add(
            Challenge(
                id = UUID.randomUUID().toString(),
                title = title.trim(),
                description = description.trim().ifBlank {
                    "Reto personalizado de ${category.label.lowercase()}."
                },
                category = category,
                difficulty = difficulty,
                durationMinutes = when (difficulty) {
                    Difficulty.Facil -> 10
                    Difficulty.Media -> 20
                    Difficulty.Dificil -> 45
                }
            )
        )
    }
}
