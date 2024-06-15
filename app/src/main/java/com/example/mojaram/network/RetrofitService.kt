package com.example.mojaram.network

import com.example.mojaram.ai.AICreationModel
import com.example.mojaram.ai.ResponseAICreation
import com.example.mojaram.ai.ResponseAiResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitService {
    @POST("processImage")
    suspend fun postAICreation(
        @Body aiCreationModel: AICreationModel
    ): Response<ResponseAICreation>

    @GET("checkPredictionStatus")
    suspend fun getAICreation(
        @Query("predictionId") predictionId: String
    ): Response<ResponseAiResult>
}