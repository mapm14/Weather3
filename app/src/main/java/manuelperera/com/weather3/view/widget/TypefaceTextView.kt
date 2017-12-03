package manuelperera.com.weather3.view.widget

import android.content.Context
import android.content.res.TypedArray
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ScaleXSpan
import android.util.AttributeSet
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import manuelperera.com.weather3.view.Fonts
import io.reactivex.Observable
import manuelperera.com.weather3.R

class TypefaceTextView : TextView {

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
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView)
        applyFont(context, typedArray)
        applyLetterSpacing(typedArray)
        typedArray.recycle()
    }

    private fun applyFont(context: Context, typedArray: TypedArray) {
        typeface = Fonts.values()[typedArray.getInteger(R.styleable.TypefaceTextView_fontName, 0)].getTypeface(context)
    }

    private fun applyLetterSpacing(typedArray: TypedArray) {
        val letterSpacingFloat = typedArray.getFloat(R.styleable.TypefaceTextView_letterSpacing, 0f) * 72f
        val stringBuilder = StringBuilder()

        for (i in 0 until text.length) {
            val c = "" + text[i]
            stringBuilder.append(c)
            if (i + 1 < text.length) {
                stringBuilder.append("\u00A0")
            }
        }

        val finalText = SpannableString(stringBuilder.toString())

        if (stringBuilder.toString().length > 1) {
            var i = 1
            while (i < stringBuilder.toString().length) {
                finalText.setSpan(ScaleXSpan(((letterSpacingFloat + 1) / 10)), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                i += 2
            }
        }

        super.setText(finalText, BufferType.SPANNABLE)
    }

    fun asObservable(): Observable<Any> = RxView.clicks(this)
}