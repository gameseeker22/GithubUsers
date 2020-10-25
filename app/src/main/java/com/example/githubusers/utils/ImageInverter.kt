package com.example.githubusers.utils

import android.graphics.*


/**
 *  Extension function for inverting image
 */
fun Bitmap.invertColors(): Bitmap? {
    val bitmap = Bitmap.createBitmap(
        width,
        height,
        Bitmap.Config.ARGB_8888
    )

    val matrixInvert = ColorMatrix().apply {
        set(
            floatArrayOf(
                -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
                0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
                0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f
            )
        )
    }

    val paint = Paint()
    ColorMatrixColorFilter(matrixInvert).apply {
        paint.colorFilter = this
    }

    Canvas(bitmap).drawBitmap(this, 0f, 0f, paint)
    return bitmap
}