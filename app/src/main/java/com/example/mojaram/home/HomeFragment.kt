package com.example.mojaram.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mojaram.LogInActivity
import com.example.mojaram.databinding.FragmentHomeBinding
import com.example.mojaram.map.MapFragment
import com.example.mojaram.map.SalonModel
import com.example.mojaram.salon.SalonDetailActivity
import com.example.mojaram.utils.AutoClearedValue
import com.example.mojaram.utils.FileUtils
import com.example.mojaram.utils.UNDER_TIRAMISU_READ_EXTERNAL_STORAGE
import com.example.mojaram.utils.UNDER_TIRAMISU_TAKE_PICTURE_PERMISSIONS
import com.example.mojaram.utils.UPPER_TIRAMISU_READ_EXTERNAL_STORAGE
import com.example.mojaram.utils.UPPER_TIRAMISU_TAKE_PICTURE_PERMISSIONS
import com.example.mojaram.utils.checkIsUpperSdkVersion
import com.example.mojaram.utils.collectWhenStarted
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var binding by AutoClearedValue<FragmentHomeBinding>()
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var storageRef: StorageReference
    private val PICK_IMAGE_REQUEST = 1 //이미지 선택
    private var cameraFileUri: Uri? = null
    private val fileUtils = FileUtils()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHairSalonRecommendationList()
        selectPicture()
        takePicture()

    }

    private fun setHairSalonRecommendationList() {
        binding.recyclerviewSalons.adapter = HairSalonListAdapter(
            onClickItem = { salonModel ->
                Intent(requireContext(), SalonDetailActivity::class.java).let {
                    it.putExtra(
                        MapFragment.SALON_DETAIL_KEY,
                        salonModel
                    )
                    startActivity(it)
                }
            }
        )

        collectWhenStarted(viewModel.recommendations) { recommendations ->
            (binding.recyclerviewSalons.adapter as HairSalonListAdapter).submitList(recommendations)

        }
    }

    private fun selectPicture() {
        val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {  result: ActivityResult ->
            result.data?.data?.let { uri ->

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

        binding.textviewSelectPicture.setOnClickListener {
            val permissions = if (Build.VERSION_CODES.TIRAMISU.checkIsUpperSdkVersion()) UPPER_TIRAMISU_READ_EXTERNAL_STORAGE else UNDER_TIRAMISU_READ_EXTERNAL_STORAGE
            requestGalleryPermission.launch(permissions)
        }
    }

    private fun takePicture() {
        val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSaved ->

        }

        var requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if(permissions.all { it.value }) {
                fileUtils.createTempImageFile(requireContext())?.let { file ->
                    FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file).let { uri ->
                        cameraFileUri = uri
                        cameraLauncher.launch(uri)
                    }
                }
            } else {

            }
        }

        binding.textviewTakePicture.setOnClickListener {
            val permissions = if (Build.VERSION_CODES.TIRAMISU.checkIsUpperSdkVersion()) UPPER_TIRAMISU_TAKE_PICTURE_PERMISSIONS else UNDER_TIRAMISU_TAKE_PICTURE_PERMISSIONS
            requestCameraPermission.launch(permissions)
        }
    }


    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)  // 이미지 선택
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            uploadImageToFirebase(imageUri)  // 선택한 이미지를 업로드
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?) {
        imageUri?.let {
            val fileReference = storageRef.child("images/${System.currentTimeMillis()}.jpg")

            fileReference.putFile(imageUri)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "이미지 업로드 성공", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }

        companion object {
            const val TAG: String = "홈 로그"

            fun newInstance(): HomeFragment {
                return HomeFragment()
            }
        }

}