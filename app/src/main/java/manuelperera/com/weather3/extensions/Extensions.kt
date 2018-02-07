package manuelperera.com.weather3.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.provider.Settings
import android.support.v13.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.squareup.picasso.Picasso
import manuelperera.com.weather3.R
import manuelperera.com.weather3.domain.objects.Weather
import org.jetbrains.anko.image
import java.text.SimpleDateFormat
import java.util.*

const val patternDateFromAPI = "yyyy-MM-dd HH:mm:ss"
const val patternDateOfApp = "dd/MM HH:mm"

fun ViewGroup.inflate(layoutResourceId: Int): View = LayoutInflater.from(context).inflate(layoutResourceId, this, false)

fun convertDpToPixel(dp: Float): Float = dp * (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun convertPixelsToDp(px: Float): Float = px / (Resources.getSystem().displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun String.formatDate(): String {
    val simpleDateFormatFromAPI = SimpleDateFormat(patternDateFromAPI, Locale.getDefault())
    val date = simpleDateFormatFromAPI.parse(this)
    val simpleDateFormatOfApp = SimpleDateFormat(patternDateOfApp, Locale.getDefault())
    return simpleDateFormatOfApp.format(date)
}

fun Activity.hideKeyboard() {
    currentFocus?.let {
        val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

fun ImageView.loadUrl(url: String?, placeholder: Int = R.mipmap.ic_launcher) {
    if (url != null)
        Picasso.with(context)
                .load(url)
                .placeholder(placeholder)
                .into(this)
}

fun ImageView.setWeatherImageView(weather: Weather, context: Context) {
    when (weather.id) {
        in 200..299 -> this.image = ContextCompat.getDrawable(context, R.drawable.weather_storm)
        in 300..399 -> this.image = ContextCompat.getDrawable(context, R.drawable.weather_drizzle)
        in 500..599 -> this.image = ContextCompat.getDrawable(context, R.drawable.weather_rain)
        in 600..699 -> this.image = ContextCompat.getDrawable(context, R.drawable.weather_snow)
        800 -> {
            if (weather.icon == "01d")
                this.image = ContextCompat.getDrawable(context, R.drawable.weather_clear_day)
            else if (weather.icon == "01n")
                this.image = ContextCompat.getDrawable(context, R.drawable.weather_clear_night)
        }
        in 801..899 -> this.image = ContextCompat.getDrawable(context, R.drawable.weather_clouds)
        else -> this.image = ContextCompat.getDrawable(context, R.drawable.weather_not_available)
    }
}

fun SwipeRefreshLayout.setThemeColors() {
    setColorSchemeResources(
            R.color.colorAccent,
            R.color.colorAccentDark
    )
}

private var isFirstTimeRequestLocationPermissions = true

fun Activity.checkLocationPermissionAndOpenSettingsIfUserCheckedNeverShow() {
    val permissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    val permissionCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
    if (permissionFineLocation != PackageManager.PERMISSION_GRANTED && permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
        if (!isFirstTimeRequestLocationPermissions && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
            openAppPermissionSettings(this)
        else
            isFirstTimeRequestLocationPermissions = false
    }
}

private fun openAppPermissionSettings(activity: Activity) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", activity.packageName, null)
    intent.data = uri
    activity.startActivity(intent)
}