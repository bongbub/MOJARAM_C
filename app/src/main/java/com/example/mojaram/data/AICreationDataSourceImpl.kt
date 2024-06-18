package com.example.mojaram.data

import com.example.mojaram.ai.AICreationModel
import com.example.mojaram.ai.ResponseAICreation
import com.example.mojaram.ai.ResponseAiResult
import com.example.mojaram.network.RetrofitService
import retrofit2.Response
import javax.inject.Inject

class AICreationDataSourceImpl @Inject constructor(
    private val retrofitService: RetrofitService
): AICreationDataSource {
    override suspend fun postImageCreation(
        faceImage: String,
        shapeImage: String,
        colorImage: String?
    ): Response<ResponseAICreation> {
        return retrofitService.postAICreation(
            AICreationModel(
                faceImage = faceImage,
                shapeImage = shapeImage,
                colorImage = colorImage
            )
        )
    }

    override suspend fun getImageResult(predictionId: String): Response<ResponseAiResult> {
        return retrofitService.getAICreation(
            predictionId = predictionId
        )
    }
}