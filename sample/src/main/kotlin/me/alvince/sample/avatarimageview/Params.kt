package me.alvince.sample.avatarimageview

/**
 * Created by alvince on 2018/7/31
 *
 * @author alvince.zy@gmail.com
 */
data class Params(var url: String) {
    var strokeColor: Int = 0
    var strokeWidth: Int = convertFromDp(2F).toInt()
    var roundedCorner: Int = convertFromDp(5F).toInt()
    var roundAsCircle = false
}
