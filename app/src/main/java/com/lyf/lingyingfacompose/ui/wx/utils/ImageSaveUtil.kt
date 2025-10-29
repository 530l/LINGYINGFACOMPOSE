package com.lyf.lingyingfacompose.ui.wx.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * @author: njb
 * @date:   2025/8/18 16:28
 * @desc:   描述
 */
object ImageSaveUtil {
    suspend fun saveImageToGallery(
        context: Context,
        imageUrl: String,
        imageLoader: ImageLoader
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            // 1. Coil加载图片为Bitmap
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false) // 禁用硬件加速以获取可写入的Bitmap
                .build()
            val bitmap = imageLoader.execute(request).drawable?.toBitmap() ?: return@withContext false

            // 2. 定义保存路径
            val saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                .absolutePath + "/Camera"
            val fileName = "IMG_${System.currentTimeMillis()}.jpg"
            val saveFile = File(saveDir, fileName)
            if (!saveFile.parentFile?.exists()!!) saveFile.parentFile?.mkdirs()

            // 3. 写入文件
            val outputStream = FileOutputStream(saveFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // 4. 通知系统图库更新
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DATA, saveFile.absolutePath)
                put(MediaStore.Images.Media.MIME_TYPE, Constants.MIME_JPEG)
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            }
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}