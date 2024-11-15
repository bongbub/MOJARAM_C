package com.example.mojaram.ai

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mojaram.data.AICreationDataSource
import com.example.mojaram.data.FirebaseDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class CreateStyleViewModel @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val aiCreationDataSource: AICreationDataSource
): ViewModel() {
    private val _onLoading = MutableStateFlow<Boolean>(false)
    val onLoading = _onLoading.asStateFlow()

    private val _creatingAIPicture = MutableStateFlow<Boolean>(false)
    val creatingAIPicture = _creatingAIPicture.asStateFlow()

    private val _faceImage = MutableStateFlow<String>("")
    val faceImage = _faceImage.asStateFlow()

    private val _selectedImage = MutableStateFlow<Int?>(null)
    val selectedImage = _selectedImage.asStateFlow()

    private val _hairStyleImage = MutableStateFlow<String>("")
    val hairStyleImage = _hairStyleImage.asStateFlow()

    private val _dyeingImage = MutableStateFlow<String>("")
    val dyeingImage = _dyeingImage.asStateFlow()

    private val _outputImage = MutableStateFlow<String>("")
    val outputImage = _outputImage.asStateFlow()

    private val _error = MutableStateFlow<Boolean>(false)
    val error = _error.asStateFlow()

    private val _responseAICreation = MutableStateFlow<ResponseAICreation?>(null)
    val responseAICreation = _responseAICreation.asStateFlow()

    fun changeSelectedImage(idx: Int) {
        _selectedImage.value = idx
    }

    fun changeFaceImage(image: String) {
        _faceImage.value = image
    }

    fun uploadImage(imageUri: Uri, resultListener: (String) -> Unit) {
        viewModelScope.launch {
            _onLoading.value = true
            firebaseDataSource.postImage(imageUri).collect {
                _onLoading.value = false
                if(it.isNotEmpty()) {
                    if(selectedImage.value == 0) {
                        _hairStyleImage.value = it
                    } else if(selectedImage.value == 1) {
                        _dyeingImage.value = it
                    }
                    resultListener(it)
                }
            }
        }
    }

    fun createImage() {
        _creatingAIPicture.value = true
        viewModelScope.launch {
            aiCreationDataSource.postImageCreation(
                faceImage = faceImage.value,
                shapeImage = hairStyleImage.value,
                colorImage = dyeingImage.value
            ).let { response ->
                if(response.isSuccessful && response.body() != null) {
                    _responseAICreation.value = response.body()
                    delay(3000)
                    getCreationResult(predictionId = response.body()!!.predictionId)
                } else {

                }

            }
        }
    }

    private fun getCreationResult(predictionId: String) {
        viewModelScope.launch {
            aiCreationDataSource.getImageResult(predictionId).let { response ->
                if(response.isSuccessful && response.body() != null) {
                    if(response.body()!!.status == "processing") {
                        delay(3000)
                        getCreationResult(predictionId)
                    } else if(response.body()!!.status == "succeeded") {
                        _outputImage.value = response.body()!!.outputUrl ?: ""
                    } else {
                        _error.value = true
                    }
                }
            }
        }
    }
}