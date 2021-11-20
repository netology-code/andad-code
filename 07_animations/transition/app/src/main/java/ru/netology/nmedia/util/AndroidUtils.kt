package ru.netology.nmedia.util

import android.content.Context
import kotlin.math.ceil

object AndroidUtils {
    fun dp(context: Context, dp: Float): Int =
        ceil(context.resources.displayMetrics.density * dp).toInt()
}