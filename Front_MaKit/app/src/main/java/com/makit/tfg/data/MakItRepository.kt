package com.makit.tfg.data

import android.content.Context
import com.makit.tfg.data.api.AuthResponse
import com.makit.tfg.data.api.ChallengeResponse
import com.makit.tfg.data.api.CategoriaDto
import com.makit.tfg.data.api.CreateRetoRequest
import com.makit.tfg.data.api.InterestsRequest
import com.makit.tfg.data.api.LoginRequest
import com.makit.tfg.data.api.MeResponse
import com.makit.tfg.data.api.ProgressSummaryResponse
import com.makit.tfg.data.api.RegisterRequest
import com.makit.tfg.data.api.RetoCatalogoDto
import com.makit.tfg.data.api.UpdateProfileRequest
import retrofit2.HttpException
import java.io.IOException

class MakItRepository(context: Context) {

    private val tokenStore = AuthTokenStore(context.applicationContext)

    init {
        ApiClient.setTokenProvider { cachedToken }
    }

    @Volatile
    private var cachedToken: String? = null

    suspend fun restoreSession(): Boolean {
        cachedToken = tokenStore.getToken()
        if (cachedToken.isNullOrBlank()) return false
        return try {
            ApiClient.api.me()
            true
        } catch (_: Exception) {
            clearSession()
            false
        }
    }

    suspend fun login(username: String, password: String): AuthResponse {
        val response = ApiClient.api.login(LoginRequest(username.trim(), password))
        persistToken(response.token)
        return response
    }

    suspend fun register(
        username: String,
        email: String,
        password: String,
        horaAviso: String? = null
    ): AuthResponse {
        val response = ApiClient.api.register(
            RegisterRequest(
                username = username.trim(),
                email = email.trim(),
                password = password,
                horaAviso = horaAviso
            )
        )
        persistToken(response.token)
        return response
    }

    suspend fun logout() {
        clearSession()
    }

    suspend fun profile(): MeResponse = ApiClient.api.profile()

    suspend fun updateProfile(username: String, email: String, horaAviso: String?): MeResponse {
        return ApiClient.api.updateProfile(
            UpdateProfileRequest(username.trim(), email.trim(), horaAviso)
        )
    }

    suspend fun todayChallenge(): ChallengeResponse = ApiClient.api.todayChallenge()

    suspend fun todayChallenges(): List<ChallengeResponse> = ApiClient.api.todayChallenges()

    suspend fun myChallenges(): List<ChallengeResponse> = ApiClient.api.myChallenges()

    suspend fun checkIn(challengeId: Long) = ApiClient.api.checkIn(challengeId)

    suspend fun categorias(): List<CategoriaDto> = ApiClient.api.categorias()

    suspend fun getInterests(): List<Long> = ApiClient.api.getInterests().categoriaIds

    suspend fun updateInterests(categoriaIds: List<Long>): List<Long> {
        return ApiClient.api.updateInterests(InterestsRequest(categoriaIds)).categoriaIds
    }

    suspend fun retos(): List<RetoCatalogoDto> = ApiClient.api.retos()

    suspend fun createReto(
        categoriaId: Long,
        titulo: String,
        descripcion: String?
    ): RetoCatalogoDto {
        return ApiClient.api.createReto(
            CreateRetoRequest(
                categoriaId = categoriaId,
                titulo = titulo,
                descripcion = descripcion,
                activo = true
            )
        )
    }

    suspend fun progressSummary(): ProgressSummaryResponse = ApiClient.api.progressSummary()

    private suspend fun persistToken(token: String) {
        cachedToken = token
        tokenStore.saveToken(token)
    }

    private suspend fun clearSession() {
        cachedToken = null
        tokenStore.clear()
    }

    companion object {
        fun humanizeError(throwable: Throwable): String = when (throwable) {
            is IOException -> "Sin conexion al servidor. Esta el backend en marcha?"
            is HttpException -> when (throwable.code()) {
                401 -> "Usuario o contrasena incorrectos"
                403 -> "No tienes permiso para esta accion (${throwable.response()?.raw()?.request?.method} ${throwable.response()?.raw()?.request?.url?.encodedPath})"
                404 -> "Recurso no encontrado"
                409 -> "El usuario o email ya existe"
                else -> "Error del servidor (${throwable.code()})"
            }
            else -> throwable.message ?: "Error desconocido"
        }
    }
}
