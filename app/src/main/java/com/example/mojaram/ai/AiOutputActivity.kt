package com.example.mojaram.ai

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.example.mojaram.databinding.ActivityAiOutputBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AiOutputActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAiOutputBinding
    private val viewModel by viewModels<AiOutputViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiOutputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadResultImage()
        downloadImage()
        binding.imageviewBack.setOnClickListener {
            finish()
        }
    }

    private fun loadResultImage() {
        intent.getStringExtra(RESULT_IMAGE)?.let {
            binding.imageviewResult.load(it)
            viewModel.changeResultImage(it)
        }
    }

    private fun downloadImage() {
        binding.textviewSave.setOnClickListener {
            lifecycleScope.launch {
                if(viewModel.resultImage.value.isNotEmpty()) {
                    saveImageToGallery(viewModel.resultImage.value)
                } else {
                    Toast.makeText(this@AiOutputActivity, "이미지를 다운로드 할 수 없어요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun saveImageToGallery(imageUrl: String) {
        val imageLoader = ImageLoader.Builder(this)
            .crossfade(true)
            .build()

        val request = ImageRequest.Builder(this)
            .data(imageUrl)
            .target { drawable ->
                val bitmap = (drawable as? BitmapDrawable)?.bitmap
                if (bitmap != null) {
                    saveBitmapToGallery(bitmap)
                } else {
                    Toast.makeText(this, "이미지 다운로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .build()

        imageLoader.execute(request)
    }

    private fun saveBitmapToGallery(bitmap: Bitmap) {
        val filename = "mojaram_${System.currentTimeMillis()}.jpg"

        kotlin.runCatching {
            checkDirectoryExist()
            val imageUri: Uri? = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/${IMAGE_FOLDER_NAME}")
                }
            )

            imageUri?.let {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)) {
                        Toast.makeText(this, "이미지 다운로드 완료 [갤러리/MOJARAM폴더]", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "이미지 다운로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }.getOrElse {
            Toast.makeText(this, "이미지 다운로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkDirectoryExist() {
        val directory = File(Environment.getExternalStoragePublicDirectory("${Environment.DIRECTORY_PICTURES}"), IMAGE_FOLDER_NAME)
        if(!directory.exists()) {
            directory.mkdirs()
        }
    }




    companion object {
        const val RESULT_IMAGE = "RESULT_IMAGE"
        private const val IMAGE_FOLDER_NAME = "MOJARAM"
    }
}