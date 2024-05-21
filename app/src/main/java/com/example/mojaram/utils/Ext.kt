package com.example.mojaram.utils

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast

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
