package com.example.mojaram.data

import com.example.mojaram.ai.ResponseAICreation
import com.example.mojaram.ai.ResponseAiResult
import retrofit2.Response

interface AICreationDataSource {
    suspend fun postImageCreation(
        faceImage: String,
        shapeImage: String,
        colorImage: String?
    ): Response<ResponseAICreation>

    suspend fun getImageResult(
        predictionId: String
    ): Response<ResponseAiResult>
}