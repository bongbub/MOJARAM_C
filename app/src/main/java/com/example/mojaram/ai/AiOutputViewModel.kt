package com.example.mojaram.ai

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AiOutputViewModel @Inject constructor(

): ViewModel() {
    private val _resultImage = MutableStateFlow<String>("")
    val resultImage = _resultImage.asStateFlow()

    fun changeResultImage(image: String) {
        _resultImage.value = image
    }
}