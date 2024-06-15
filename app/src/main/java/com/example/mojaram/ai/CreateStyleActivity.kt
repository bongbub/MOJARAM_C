package com.example.mojaram.ai

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.example.mojaram.R
import com.example.mojaram.ai.AiOutputActivity.Companion.RESULT_IMAGE
import com.example.mojaram.databinding.ActivityCreateStyleBinding
import com.example.mojaram.home.HomeFragment
import com.example.mojaram.utils.UNDER_TIRAMISU_READ_EXTERNAL_STORAGE
import com.example.mojaram.utils.UPPER_TIRAMISU_READ_EXTERNAL_STORAGE
import com.example.mojaram.utils.checkIsUpperSdkVersion
import com.example.mojaram.utils.collectWhenStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateStyleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStyleBinding
    private val viewModel by viewModels<CreateStyleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStyleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFaceImage()
        selectImage()
        create()
    }

    private fun initFaceImage() {
        val faceImage = intent.getStringExtra(HomeFragment.FACE_IMG)
        if(faceImage != null) {
            viewModel.changeFaceImage(faceImage)
        }
        binding.imageviewFace.load(faceImage)
    }

    private fun selectImage() {
        val permissions = if (Build.VERSION_CODES.TIRAMISU.checkIsUpperSdkVersion()) UPPER_TIRAMISU_READ_EXTERNAL_STORAGE else UNDER_TIRAMISU_READ_EXTERNAL_STORAGE

        binding.buttonHairstyle.root.setOnClickListener {
            viewModel.changeSelectedImage(0)
            requestGalleryPermission.launch(permissions)
        }

        binding.buttonDyeing.root.setOnClickListener {
            viewModel.changeSelectedImage(1)
            requestGalleryPermission.launch(permissions)
        }

        collectWhenStarted(viewModel.hairStyleImage) { image ->
            if(image.isNotEmpty()) {
                binding.imageviewUploadHair.load(image)
            } else {
                binding.imageviewUploadHair.load(R.drawable.ic_upload_picture)
            }
        }

        collectWhenStarted(viewModel.dyeingImage) { image ->
            if(image.isNotEmpty()) {
                binding.imageviewUploadDyeing.load(image)
            } else {
                binding.imageviewUploadDyeing.load(R.drawable.ic_upload_picture)
            }
        }

        collectWhenStarted(viewModel.onLoading) { onLoading ->
            binding.layoutProgress.visibility = if(onLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun create() {
        binding.textviewCreate.setOnClickListener {
            viewModel.createImage()
        }
        collectWhenStarted(viewModel.creatingAIPicture) { creating ->
            binding.layoutCreateingAiImage.visibility = if(creating) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        collectWhenStarted(viewModel.error) { hasError ->
            if(hasError) {
                Toast.makeText(this, "이미지 생성이 불가합니다. 다른 사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        collectWhenStarted(viewModel.outputImage) { output ->
            if(output.isNotEmpty()) {
                Intent(this, AiOutputActivity::class.java).let {
                    it.putExtra(RESULT_IMAGE, output)
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        result.data?.data?.let { uri ->
            viewModel.uploadImage(uri) { imageUrl ->

            }
        }
    }

    var requestGalleryPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if(permissions.all { it.value }) {
            Intent(Intent.ACTION_PICK).apply {
                type = MediaStore.Images.Media.CONTENT_TYPE
                type = "image/*"
                galleryLauncher.launch(this)
            }
        } else {

        }
    }
}