package me.alvince.sample.avatarimageview

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by alvince on 2018/7/31
 *
 * @author alvince.zy@gmail.com
 */

fun convertFromDp(value: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        value, Resources.getSystem().displayMetrics)

fun convert2Dp(dimensInPix: Float): Float {
    val metrics = Resources.getSystem().displayMetrics
    return dimensInPix.div(metrics.density)
}