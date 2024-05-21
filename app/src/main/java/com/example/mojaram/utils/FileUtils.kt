package com.example.mojaram.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class FileUtils {
    fun createTempImageFile(context: Context): File? {
        return try {
            val imageFileName = "IMG_" + imageFileNameDateFormat.format(Date()) + "${Random.nextInt(0, 1000)}"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile(imageFileName, ".jpg", storageDir)
        } catch (e: IOException) {
            null
        }
    }

    private val imageFileNameDateFormat = SimpleDateFormat("yyyyMMdd_hhmmss", Locale.KOREAN)
}