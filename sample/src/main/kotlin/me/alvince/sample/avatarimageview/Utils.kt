package me.alvince.sample.avatarimageview

import android.content.res.Resources
import android.util.TypedValue

fun convertFromDp(value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        value, Resources.getSystem().displayMetrics)

fun convert2Dp(dimensInPix: Float): Float {
    val metrics = Resources.getSystem().displayMetrics
    return dimensInPix.div(metrics.density)
}

fun convert2DpInt(dimensInPix: Float): Int {
    val value = convert2Dp(dimensInPix)
    if (value.times(100).toInt().rem(100) > 0) {
        return value.toInt().inc()
    }
    return value.toInt()
}