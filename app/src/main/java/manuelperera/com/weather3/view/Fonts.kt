package manuelperera.com.weather3.view

import android.content.Context
import android.graphics.Typeface

enum class Fonts(private val fontName: String) {

    REGULAR("fonts/OpenSans-Regular.ttf"),
    SEMI_BOLD("fonts/OpenSans-Semibold.ttf"),
    BOLD("fonts/OpenSans-Bold.ttf");

    var typeface: Typeface? = null

    fun getTypeface(context: Context): Typeface? {
        typeface ?: run {
            typeface = Typeface.createFromAsset(context.assets, fontName)
        }
        return typeface
    }
}