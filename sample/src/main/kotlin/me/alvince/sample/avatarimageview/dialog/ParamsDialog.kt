package me.alvince.sample.avatarimageview.dialog

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import com.dingmouren.colorpicker.ColorPickerDialog
import com.dingmouren.colorpicker.OnColorPickerListener
import kotlinx.android.synthetic.main.dialog_view_params.*
import me.alvince.sample.avatarimageview.Params
import me.alvince.sample.avatarimageview.R
import me.alvince.sample.avatarimageview.convert2DpInt

/**
 * Created by alvince on 2018/7/31
 *
 * @author alvince.zy@gmail.com
 * @version 1.0.1, 2018/8/1
 */
class ParamsDialog(context: Context, val data: Params, private val listener: (Params) -> Unit)
    : BottomSheetDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_view_params)

        block_color.setOnClickListener { showColorPicker() }
        text_label_color.setOnClickListener { showColorPicker() }

        edit_corner_radius.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s == null || s.isEmpty()) {
                    return
                }
                data.roundedCorner = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        s.toString().toFloat(), Resources.getSystem().displayMetrics).toInt()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        edit_stroke_width.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s == null || s.isEmpty()) {
                    return
                }
                data.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        s.toString().toFloat(), Resources.getSystem().displayMetrics).toInt()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        setOnDismissListener { listener.invoke(data) }
    }

    override fun onStart() {
        super.onStart()
        text_url.text = "URL: ${data.url}"
        check_circular.isChecked = data.roundAsCircle
        check_circular.isEnabled = false
        block_color.setBackgroundColor(data.strokeColor)
        edit_corner_radius.setText("${convert2DpInt(data.roundedCorner.toFloat())}")
        edit_stroke_width.setText("${convert2DpInt(data.strokeWidth.toFloat())}")
    }

    private fun showColorPicker() {
        val picker = ColorPickerDialog(context, data.strokeColor, object : OnColorPickerListener {
            override fun onColorConfirm(dialog: ColorPickerDialog?, color: Int) {
                data.strokeColor = color
                block_color.setBackgroundColor(color)
            }

            override fun onColorCancel(dialog: ColorPickerDialog?) {
            }

            override fun onColorChange(dialog: ColorPickerDialog?, color: Int) {
                data.strokeColor = color
            }
        })
        picker.show()
    }
}
