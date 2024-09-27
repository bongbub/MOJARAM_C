package com.example.mojaram

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mojaram.databinding.ActivityMyreviewssBinding
import com.example.mojaram.databinding.ActivityReservationBinding
import com.example.mojaram.databinding.FragmentMyReviewsBinding
import com.example.mojaram.utils.UNDER_TIRAMISU_READ_EXTERNAL_STORAGE
import com.example.mojaram.utils.UPPER_TIRAMISU_READ_EXTERNAL_STORAGE
import com.example.mojaram.utils.checkIsUpperSdkVersion


class MyReviews : AppCompatActivity() {

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestGalleryPermission: ActivityResultLauncher<Array<String>>

    private lateinit var binding: FragmentMyReviewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMyReviewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()
    }
    fun initview(){
        setPicture()

        binding.imgGroup2.setOnClickListener {
            finish()
        }

        binding.btnChooser.setOnClickListener({ v ->
            val permissions = if (Build.VERSION_CODES.TIRAMISU.checkIsUpperSdkVersion()) UPPER_TIRAMISU_READ_EXTERNAL_STORAGE else UNDER_TIRAMISU_READ_EXTERNAL_STORAGE
            requestGalleryPermission.launch(permissions)
        })
        binding.btnAdd.setOnClickListener({ v ->

        })
        binding.btnChange.setOnClickListener({ v ->
            val permissions = if (Build.VERSION_CODES.TIRAMISU.checkIsUpperSdkVersion()) UPPER_TIRAMISU_READ_EXTERNAL_STORAGE else UNDER_TIRAMISU_READ_EXTERNAL_STORAGE
            requestGalleryPermission.launch(permissions)
        })
        binding.btnRemove.setOnClickListener({ v ->
            RemovePicture()
        })
        binding.edReview.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
// 텍스트 변경 시 실행되는 메서드
                val textLength = s?.length ?: 0 // 글자의 개수를 세기
                binding.txtCount.text = "$textLength/ 1000" // 글자 수를 txt_count에 반영
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }

    fun setPicture(){
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            result.data?.data?.let { uri ->
                binding.imageView8.setImageURI(uri)
                binding.imageView8.visibility = View.VISIBLE
                binding.btnChange.visibility = View.VISIBLE
                binding.btnRemove.visibility = View.VISIBLE
                binding.btnChooser.visibility = View.GONE

            }
        }

        requestGalleryPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
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
    fun ChangePicture(){

    }
    fun RemovePicture(){
        binding.imageView8.setImageURI(null)
        binding.imageView8.visibility = View.GONE
        binding.btnChange.visibility = View.GONE
        binding.btnRemove.visibility = View.GONE
        binding.btnChooser.visibility = View.VISIBLE
    }
}