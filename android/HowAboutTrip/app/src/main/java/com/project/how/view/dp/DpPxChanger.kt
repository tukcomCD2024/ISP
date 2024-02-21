package com.project.how.view.dp

import android.content.Context
import kotlin.math.roundToInt

class DpPxChanger {
    fun dpToPx(context : Context, dp: Int): Int {
        val density: Float = context.resources.displayMetrics.density
        return (dp * density).roundToInt()
    }
}