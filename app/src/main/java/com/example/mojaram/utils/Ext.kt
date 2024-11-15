package com.example.mojaram.utils

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

fun Int.checkIsUpperSdkVersion(): Boolean {
    return Build.VERSION.SDK_INT >= this
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

val UNDER_TIRAMISU_READ_EXTERNAL_STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

val UPPER_TIRAMISU_READ_EXTERNAL_STORAGE = arrayOf(
    Manifest.permission.READ_MEDIA_IMAGES
)

val UPPER_TIRAMISU_TAKE_PICTURE_PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.READ_MEDIA_IMAGES
)
val UNDER_TIRAMISU_TAKE_PICTURE_PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

inline fun <T, R> R.collectFlow(flow: Flow<T>, state: Lifecycle.State, crossinline block: suspend (T) -> Unit) {
    when(this) {
        is AppCompatActivity -> {
            lifecycleScope.launch {
                repeatOnLifecycle(state) {
                    flow.collect { block(it) }
                }
            }
        }
        is Fragment -> {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(state) {
                    flow.collect { block(it) }
                }
            }
        }
        else -> {}
    }
}

inline fun <T, R> R.collectWhenStarted(flow: Flow<T>, crossinline block: suspend (T) -> Unit) {
    collectFlow(flow, Lifecycle.State.STARTED, block)
}

val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)