package manuelperera.com.weather3.view.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.Button
import com.jakewharton.rxbinding2.view.RxView
import manuelperera.com.weather3.view.Fonts
import io.reactivex.Observable
import manuelperera.com.weather3.R

class TypefaceButton : Button {

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
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceButton)
        applyFont(context, typedArray)
        typedArray.recycle()
    }

    private fun applyFont(context: Context, typedArray: TypedArray) {
        typeface = Fonts.values()[typedArray.getInteger(R.styleable.TypefaceButton_fontName, 3)].getTypeface(context)
    }

    fun asObservable(): Observable<Any> = RxView.clicks(this)
}