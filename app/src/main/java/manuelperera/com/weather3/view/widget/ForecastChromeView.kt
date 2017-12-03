package manuelperera.com.weather3.view.widget

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.chrome_forecast.view.*
import manuelperera.com.weather3.R
import manuelperera.com.weather3.domain.objects.Forecast
import manuelperera.com.weather3.extensions.formatDate
import manuelperera.com.weather3.extensions.setWeatherImageView

class ForecastChromeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : CardView(context, attrs, defStyleAttr) {

    init {
        init()
    }

    private fun init() {
        inflate(context, R.layout.chrome_forecast, this)
    }

    fun setForecastChrome(forecast: Forecast) {
        forecastCurrentTempTextView.text = context.getString(R.string.temp_celsius_format, forecast.tempAndHumidity.temp)
        forecastMinAndMaxTempTextView.text = context.getString(R.string.temp_min_and_max_celsius_format, forecast.tempAndHumidity.tempMin, forecast.tempAndHumidity.tempMax)
        forecastWindSpeedTextView.text = context.getString(R.string.wind_ms_format, forecast.wind.speed)
        forecastHumidityPercentageTextView.text = context.getString(R.string.humidity_percentage_format, forecast.clouds.all)
        forecastCloudsPercentageTextView.text = context.getString(R.string.clouds_percentage_format, forecast.tempAndHumidity.humidity)
        forecastDateTextView.text = forecast.date.formatDate()
        val descriptionWithUppercase = forecast.weather[0].description.substring(0, 1).toUpperCase() + forecast.weather[0].description.substring(1)
        forecastWeatherTextView.text = descriptionWithUppercase

        if (forecast.rain != null && forecast.rain?.volumeLastThreeHours != null) {
            forecastPrecipitationTextView.visibility = View.VISIBLE
            forecastPrecipitationTextView.text = context.getString(R.string.rain_mm_format, forecast.rain?.volumeLastThreeHours)
        } else
            forecastPrecipitationTextView.visibility = View.GONE

        forecastImageView.setWeatherImageView(forecast.weather[0], context)

        requestLayout()
    }

}