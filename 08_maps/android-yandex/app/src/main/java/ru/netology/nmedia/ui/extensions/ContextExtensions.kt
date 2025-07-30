package ru.netology.nmedia.ui.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap

fun Context.getBitmapFromVectorDrawable(
    @DrawableRes drawableId: Int,
    @ColorInt tintColor: Int? = null,
): Bitmap? {
    val drawable = ContextCompat.getDrawable(this, drawableId) ?: return null

    tintColor?.also(drawable::setTint)

    val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}