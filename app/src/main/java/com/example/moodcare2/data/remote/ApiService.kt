package com.example.moodcare2.data.remote

import com.example.moodcare2.data.model.LoginResponse
import com.example.moodcare2.data.model.LoginRequest
import com.example.moodcare2.data.model.MoodResponse
import com.example.moodcare2.data.model.MoodsResponse
import com.example.moodcare2.data.model.RegisterResponse
import com.example.moodcare2.data.model.RegisterRequest
import com.example.moodcare2.data.model.CreateMoodRequest
import com.example.moodcare2.data.model.ApiResponse
import com.example.moodcare2.data.model.ProfileResponse
import com.example.moodcare2.data.model.UpdateMoodRequest
import com.example.moodcare2.data.model.UpdateProfileRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/register.php")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/login.php")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/moods/index.php")
    suspend fun getAllMoods(@Header("Authorization") token: String): Response<MoodsResponse>

    @GET("api/moods/get.php")
    suspend fun getMoodById(
        @Header("Authorization") token: String,
        @Query("id") moodId: Int
    ): Response<MoodResponse>

    @POST("api/moods/create.php")
    suspend fun createMood(
        @Header("Authorization") token: String,
        @Body request: CreateMoodRequest
    ): Response<ApiResponse>

    @PUT("api/moods/update.php")
    suspend fun updateMood(
        @Header("Authorization") token: String,
        @Body request: UpdateMoodRequest
    ): Response<ApiResponse>

    @DELETE("api/moods/delete.php")
    suspend fun deleteMood(
        @Header("Authorization") token: String,
        @Query("id") moodId: Int
    ): Response<ApiResponse>

    @GET("api/moods/search.php")
    suspend fun searchMoodByDate(
        @Header("Authorization") token: String,
        @Query("date") date: String
    ): Response<MoodsResponse>

    @GET("api/moods/search.php")
    suspend fun searchMoodByRange(
        @Header("Authorization") token: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): Response<MoodsResponse>
    @GET("api/users/profile.php")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ProfileResponse>
    @PUT("api/users/update.php")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse>

    @Multipart
    @POST("api/users/update.php")
    suspend fun updateProfileWithImage(
        @Header("Authorization") token: String,
        @Part("nama") nama: RequestBody,
        @Part fotoProfil: MultipartBody.Part
    ): Response<ProfileResponse>
}