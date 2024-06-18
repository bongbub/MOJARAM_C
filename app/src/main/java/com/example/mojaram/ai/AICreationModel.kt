package com.example.mojaram.ai

import com.google.gson.annotations.SerializedName

data class AICreationModel(
    @SerializedName("face_img")
    val faceImage: String,
    @SerializedName("shape_img")
    val shapeImage: String,
    @SerializedName("color_img")
    val colorImage: String?,
    @SerializedName("Blending_checkpoint")
    val blendingCheckPoint: String = "Default",
    @SerializedName("Alignment_images")
    val alignmentImages: String = "Auto",
    @SerializedName("Poisson_Blending")
    val poissonBlending: String = "Off",
    @SerializedName("Poissons_iters")
    val poissonsIters: Int = 115,
    @SerializedName("Poisson_erossion")
    val poissonErossion: Int = 15
)