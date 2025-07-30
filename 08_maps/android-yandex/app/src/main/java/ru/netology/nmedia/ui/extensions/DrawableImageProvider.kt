package ru.netology.nmedia.ui.extensions

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.yandex.runtime.image.ImageProvider

data class ImageInfo(@field:DrawableRes val id: Int, @field:ColorInt val tintColor: Int? = null)

class DrawableImageProvider(
    private val context: Context,
    private val imageInfo: ImageInfo,
) : ImageProvider() {
    override fun getImage() = context.getBitmapFromVectorDrawable(
        drawableId = imageInfo.id,
        tintColor = imageInfo.tintColor,
    )

    override fun getId() = imageInfo.toString()
}
