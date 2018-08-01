package me.alvince.sample.avatarimageview.dialog

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.WindowManager
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

    private var circular = false
    private var cornerRadius = 0F
    private var strokeWidth = 0F
    private var strokeColor = Color.TRANSPARENT

    init {
        circular = data.roundAsCircle
        cornerRadius = data.roundedCorner.toFloat()
        strokeWidth = data.strokeWidth.toFloat()
        strokeColor = data.strokeColor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_view_params)
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        edit_corner_radius.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s == null || s.isEmpty()) {
                    return
                }
                cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        s.toString().toFloat(), Resources.getSystem().displayMetrics)
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
                strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        s.toString().toFloat(), Resources.getSystem().displayMetrics)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        block_color.setOnClickListener { showColorPicker() }
        text_label_color.setOnClickListener { showColorPicker() }
        btn_negative.setOnClickListener { dismiss() }
        btn_positive.setOnClickListener {
            if (validate()) {
                listener.invoke(data)
            }
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val url = SpannableString("URL [ ${data.url} ]")
        val urlStart = url.indexOf('[').inc()
        val urlEnd = url.lastIndexOf(']')
        url.setSpan(StyleSpan(Typeface.BOLD), 0, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        url.setSpan(StyleSpan(Typeface.ITALIC), urlStart, urlEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        text_url.text = url
        check_circular.isChecked = circular
        check_circular.isEnabled = false
        block_color.setBackgroundColor(strokeColor)
        edit_corner_radius.setText("${convert2DpInt(cornerRadius)}")
        edit_stroke_width.setText("${convert2DpInt(strokeWidth)}")
    }

    private fun showColorPicker() {
        val picker = ColorPickerDialog(context, strokeColor, object : OnColorPickerListener {
            override fun onColorConfirm(dialog: ColorPickerDialog?, color: Int) {
                strokeColor = color
                block_color.setBackgroundColor(color)
            }

            override fun onColorCancel(dialog: ColorPickerDialog?) {
            }

            override fun onColorChange(dialog: ColorPickerDialog?, color: Int) {
            }
        })
        picker.show()
    }

    private fun validate(): Boolean {
        var changed = false
        if (data.strokeColor != strokeColor) {
            data.strokeColor = strokeColor
            changed = true
        }
        val strokeWidthPix = strokeWidth.toInt()
        if (data.strokeWidth != strokeWidthPix) {
            data.strokeWidth = strokeWidthPix
            changed = true
        }
        val radiusPix = cornerRadius.toInt()
        if (data.roundedCorner != radiusPix) {
            data.roundedCorner = radiusPix
            changed = true
        }
        return changed
    }
}
