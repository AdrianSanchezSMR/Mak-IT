package com.makit.tfg.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MakItApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("api/auth/me")
    suspend fun me(): MeResponse

    @GET("api/users/me/profile")
    suspend fun profile(): MeResponse

    @PUT("api/users/me/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): MeResponse

    @GET("api/challenges/today")
    suspend fun todayChallenge(): ChallengeResponse

    @POST("api/challenges/random")
    suspend fun randomChallenge(): ChallengeResponse

    @GET("api/challenges/today/all")
    suspend fun todayChallenges(): List<ChallengeResponse>

    @GET("api/challenges/mine")
    suspend fun myChallenges(): List<ChallengeResponse>

    @PUT("api/challenges/{id}/checkin")
    suspend fun checkIn(@Path("id") challengeId: Long): CheckInResponse

    @GET("api/categorias")
    suspend fun categorias(): List<CategoriaDto>

    @GET("api/users/me/interests")
    suspend fun getInterests(): InterestsResponse

    @PUT("api/users/me/interests")
    suspend fun updateInterests(@Body request: InterestsRequest): InterestsResponse

    @GET("api/retos")
    suspend fun retos(): List<RetoCatalogoDto>

    @POST("api/retos")
    suspend fun createReto(@Body request: CreateRetoRequest): RetoCatalogoDto

    @GET("api/users/me/progress/summary")
    suspend fun progressSummary(): ProgressSummaryResponse
}
