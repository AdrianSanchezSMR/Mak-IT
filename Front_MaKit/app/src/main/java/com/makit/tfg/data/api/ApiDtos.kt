package com.makit.tfg.data.api

data class LoginRequest(val username: String, val password: String)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val horaAviso: String? = null
)

data class AuthResponse(val token: String, val username: String, val role: String)

data class MeResponse(
    val id: Long,
    val username: String,
    val email: String,
    val horaAviso: String?,
    val role: String
)

data class UpdateProfileRequest(
    val username: String,
    val email: String,
    val horaAviso: String?
)

data class ChallengeResponse(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val categoriaId: Long,
    val categoriaNombre: String,
    val completadoHoy: Boolean,
    val fecha: String? = null
)

data class CheckInResponse(
    val message: String,
    val challengeId: Long,
    val completado: Boolean
)

data class CategoriaDto(
    val id: Long,
    val nombre: String,
    val descripcion: String?
)

data class RetoCategoriaDto(
    val id: Long,
    val nombre: String,
    val descripcion: String?
)

data class RetoCatalogoDto(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val activo: Boolean,
    val categoria: RetoCategoriaDto
)

data class InterestsRequest(val categoriaIds: List<Long>)

data class InterestsResponse(val categoriaIds: List<Long>)

data class DailyProgressPoint(val fecha: String, val completado: Boolean)

data class ProgressSummaryResponse(
    val totalRetosAsignados: Long,
    val totalRetosCompletados: Long,
    val totalRetosPendientes: Long,
    val tasaCompletado: Double,
    val serieDiaria: List<DailyProgressPoint>
)

data class CreateRetoRequest(
    val categoriaId: Long,
    val titulo: String,
    val descripcion: String?,
    val activo: Boolean = true
)
