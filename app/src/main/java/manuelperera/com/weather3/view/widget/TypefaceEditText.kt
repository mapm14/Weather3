package manuelperera.com.weather3.view.widget

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import manuelperera.com.weather3.view.Fonts
import manuelperera.com.weather3.R

class TypefaceEditText : AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        text?.let {
            applyStyle(context, attrs)
        }
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        text?.let {
            applyStyle(context, attrs)
        }
    }

    private fun applyStyle(context: Context, attrs: AttributeSet) {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceEditText)
        applyFont(context, typedArray)
        typedArray.recycle()
    }

    private fun applyFont(context: Context, typedArray: TypedArray) {
        typeface = Fonts.values()[typedArray.getInteger(R.styleable.TypefaceEditText_fontName, 0)].getTypeface(context)
    }

}