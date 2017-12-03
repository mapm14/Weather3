package manuelperera.com.weather3.extensions

import android.app.Activity
import manuelperera.com.weather3.R

fun Activity.transitionUpAnimation() {
    overridePendingTransition(R.anim.slide_up_info, R.anim.no_change)
}

fun Activity.transitionDownAnimation() {
    overridePendingTransition(R.anim.no_change, R.anim.slide_down_info)
}