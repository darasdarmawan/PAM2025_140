package com.example.moodcare2.data.repository

import com.example.moodcare2.data.model.ApiResponse
import com.example.moodcare2.data.model.CreateMoodRequest
import com.example.moodcare2.data.model.LoginRequest
import com.example.moodcare2.data.model.LoginResponse
import com.example.moodcare2.data.model.MoodResponse
import com.example.moodcare2.data.model.MoodsResponse
import com.example.moodcare2.data.model.ProfileResponse
import com.example.moodcare2.data.model.RegisterRequest
import com.example.moodcare2.data.model.RegisterResponse
import com.example.moodcare2.data.model.UpdateMoodRequest
import com.example.moodcare2.data.model.UpdateProfileRequest
import com.example.moodcare2.data.remote.RetrofitClient
import com.example.moodcare2.utils.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import retrofit2.Response
import android.os.Build
import com.example.moodcare2.data.model.SaveGraphRequest

class MoodCareRepository(val sessionManager: SessionManager) {

    private val apiService = RetrofitClient.apiService
    suspend fun register(nama: String, email: String, password: String): Response<RegisterResponse> {
        return apiService.register(RegisterRequest(nama, email, password))
    }

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        val deviceInfo = "${Build.MANUFACTURER} ${Build.MODEL}"

        return apiService.login(
            LoginRequest(
                email = email,
                password = password,
                device_info = deviceInfo
            )
        )
    }
    suspend fun getAllMoods(): Response<MoodsResponse> {
        val token = sessionManager.getToken() ?: return Response.error(401,
            okhttp3.ResponseBody.create(null, "No token"))
        return apiService.getAllMoods(token)
    }

    suspend fun getMoodById(moodId: Int): Response<MoodResponse> {
        val token = sessionManager.getToken() ?: return Response.error(401,
            okhttp3.ResponseBody.create(null, "No token"))
        return apiService.getMoodById(token, moodId)
    }

    suspend fun createMood(
        moodLevel: String,
        description: String?,
        halBersyukur: String?,
        halSedih: String?,
        halPerbaikan: String?,
        tanggal: String
    ): Response<ApiResponse> {
        val token = sessionManager.getToken() ?: return Response.error(401,
            okhttp3.ResponseBody.create(null, "No token"))
        val request = CreateMoodRequest(moodLevel, description, halBersyukur, halSedih, halPerbaikan, tanggal)
        return apiService.createMood(token, request)
    }

    suspend fun updateMood(
        moodId: Int,
        moodLevel: String,
        description: String?,
        halBersyukur: String?,
        halSedih: String?,
        halPerbaikan: String?,
        tanggal: String
    ): Response<ApiResponse> {
        val token = sessionManager.getToken() ?: return Response.error(401,
            okhttp3.ResponseBody.create(null, "No token"))
        val request = UpdateMoodRequest(moodId, moodLevel, description, halBersyukur, halSedih, halPerbaikan, tanggal)
        return apiService.updateMood(token, request)
    }

    suspend fun deleteMood(moodId: Int): Response<ApiResponse> {
        val token = sessionManager.getToken() ?: return Response.error(401,
            okhttp3.ResponseBody.create(null, "No token"))
        return apiService.deleteMood(token, moodId)
    }

    suspend fun searchMoodByDate(date: String): Response<MoodsResponse> {
        val token = sessionManager.getToken() ?: return Response.error(401,
            okhttp3.ResponseBody.create(null, "No token"))
        return apiService.searchMoodByDate(token, date)
    }

    suspend fun searchMoodByRange(startDate: String, endDate: String): Response<MoodsResponse> {
        val token = sessionManager.getToken() ?: return Response.error(401,
            okhttp3.ResponseBody.create(null, "No token"))
        return apiService.searchMoodByRange(token, startDate, endDate)
    }

    suspend fun getProfile(): Response<ProfileResponse> {
        val token = sessionManager.getToken() ?: return Response.error(401,
            okhttp3.ResponseBody.create(null, "No token"))
        return apiService.getProfile(token)
    }
    suspend fun updateProfileWithImage(nama: String, imageFile: File?): Response<ProfileResponse> {
        val token = sessionManager.getToken() ?: return Response.error(401,
            okhttp3.ResponseBody.create(null, "No token"))

        return if (imageFile != null && imageFile.exists()) {
            val namaPart = nama.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("fotoProfil", imageFile.name, requestFile)

            apiService.updateProfileWithImage(token, namaPart, imagePart)
        } else {
            val response = apiService.updateProfile(token, UpdateProfileRequest(nama, null))
            if (response.isSuccessful && response.body()?.success == true) {
                getProfile()
            } else {
                Response.error(response.code(), response.errorBody()!!)
            }
        }
    }
    suspend fun saveGraphHistory(
        startDate: String,
        endDate: String,
        fileName: String
    ): Response<ApiResponse> {
        val token = sessionManager.getToken()
            ?: return Response.error(401, okhttp3.ResponseBody.create(null, "No token"))

        val request = SaveGraphRequest(
            start_date = startDate,
            end_date = endDate,
            file_name = fileName
        )

        return apiService.saveGraphHistory(token, request)
    }

}