package com.example.moodcare2.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id")
    val user_id: Int,
    val nama: String,
    val email: String,

    @SerializedName("foto_profil")
    val fotoProfil: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
)

@Parcelize
data class Mood(
    val mood_id: Int = 0,
    val user_id: Int = 0,
    val mood_level: String,
    val description: String? = null,
    val hal_bersyukur: String? = null,
    val hal_sedih: String? = null,
    val hal_perbaikan: String? = null,
    val tanggal: String,
    val created_at: String? = null,
    val updated_at: String? = null
) : Parcelable

data class ApiResponse(
    val success: Boolean,
    val message: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String? = null,
    val token: String,
    val user: User
)

data class RegisterResponse(
    val success: Boolean,
    val message: String? = null,
    val token: String,
    val user: User
)

data class MoodsResponse(
    val success: Boolean,
    val count: Int,
    val data: List<Mood>
)

data class MoodResponse(
    val success: Boolean,
    val data: Mood
)

data class ProfileResponse(
    val success: Boolean,
    val message: String? = null,
    val data: User
)

data class LoginRequest(
    val email: String,
    val password: String,
    val device_info: String
)

data class RegisterRequest(
    val nama: String,
    val email: String,
    val password: String
)

data class CreateMoodRequest(
    val mood_level: String,
    val description: String?,
    val hal_bersyukur: String?,
    val hal_sedih: String?,
    val hal_perbaikan: String?,
    val tanggal: String
)

data class UpdateMoodRequest(
    val mood_id: Int,
    val mood_level: String,
    val description: String?,
    val hal_bersyukur: String?,
    val hal_sedih: String?,
    val hal_perbaikan: String?,
    val tanggal: String
)
data class UpdateProfileRequest(
    val nama: String,
    val foto_profil: String?
)
data class ErrorResponse(
    val success: Boolean,
    val message: String
)
