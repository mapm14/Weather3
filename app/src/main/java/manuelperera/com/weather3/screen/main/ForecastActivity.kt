package manuelperera.com.weather3.screen.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import co.develoop.reactivepermission.rx2.ReactivePermission
import com.google.android.gms.actions.SearchIntents
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.MapsInitializer
import kotlinx.android.synthetic.main.activity_forecast.*
import kotlinx.android.synthetic.main.container_location.*
import kotlinx.android.synthetic.main.item_no_permissions.*
import kotlinx.android.synthetic.main.toolbar.*
import manuelperera.com.weather3.R
import manuelperera.com.weather3.Weather3App
import manuelperera.com.weather3.domain.objects.Forecast
import manuelperera.com.weather3.extensions.checkLocationPermissionAndOpenSettingsIfUserCheckedNeverShow
import manuelperera.com.weather3.extensions.formatDate
import manuelperera.com.weather3.extensions.setThemeColors
import manuelperera.com.weather3.extensions.setWeatherImageView
import manuelperera.com.weather3.screen.main.injection.DaggerForecastActivityComponent
import manuelperera.com.weather3.screen.main.section.ForecastActivityRecyclerAdapter
import org.jetbrains.anko.toast
import java.util.*
import javax.inject.Inject

class ForecastActivity : FragmentActivity(), ForecastActivityPresenterView, TextToSpeech.OnInitListener {

    private var toSpeech: TextToSpeech? = null
    private var textToSpeechResult: Int = TextToSpeech.LANG_NOT_SUPPORTED

    @Inject
    lateinit var forecastActivityPresenter: ForecastActivityPresenter

    init {
        DaggerForecastActivityComponent.builder().appComponent(Weather3App.Companion.daggerAppComponent()).build().inject(this)
    }

    private lateinit var googleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        forecastActivityPresenter.init(this)
        toSpeech = TextToSpeech(this, this)
        collapsingToolbar.title = getString(R.string.app_name)
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent))

        MapsInitializer.initialize(application)
        initGoogleApiClient()
        checkPermissions()
        getIntentQuery()
    }

    override fun onInit(status: Int) {
        toSpeech?.let { ts ->
            textToSpeechResult = ts.setLanguage(Locale.US)
        }
    }

    private fun textToSpeech(text: String) {
        if (textToSpeechResult == TextToSpeech.LANG_MISSING_DATA || textToSpeechResult == TextToSpeech.LANG_NOT_SUPPORTED)
            toast(getString(R.string.tts_not_supported))
        else
            toSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }

    private fun getIntentQuery() {
        if (SearchIntents.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)

            // TODO: Get information from query and validate it
            toast(query)
        }
    }

    private fun initGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER
        locationRequest.interval = 10000
        locationRequest.smallestDisplacement = 100f
    }

    private fun checkPermissions() {
        permissionRetryButton.asObservable().subscribe {
            requestPermissions()
        }

        val permissionFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val permissionCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permissionFineLocation != PackageManager.PERMISSION_GRANTED && permissionCoarseLocation != PackageManager.PERMISSION_GRANTED)
            requestPermissions()
        else
            setLatAndLng()
    }

    @SuppressLint("MissingPermission")
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ReactivePermission.Builder(this)
                    .addPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .addPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    .subscribe { permissionResults ->
                        if (permissionResults.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && permissionResults.hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            setLatAndLng()
                            if (!googleApiClient.isConnected)
                                googleApiClient.connect()
                        } else {
                            loadingContainer.visibility = View.GONE
                            noPermissionsContainer.visibility = View.VISIBLE
                        }
                    }

            checkLocationPermissionAndOpenSettingsIfUserCheckedNeverShow()
        } else
            setLatAndLng()
    }

    private fun setLatAndLng() {
        val userLocation = getLastKnownUserLocation()
        userLocation?.let {
            forecastActivityPresenter.setLatAndLng(userLocation.latitude, userLocation.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownUserLocation(): Location? {
        val locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }

    override fun initRecycler() {
        containerLocation.visibility = View.GONE
        forecastCoordinatorContainer.visibility = View.VISIBLE

        forecastRecyclerView?.apply {
            layoutManager = LinearLayoutManager(this@ForecastActivity)
            itemAnimator = null
            adapter = ForecastActivityRecyclerAdapter(this@ForecastActivity)
        }

        forecastSwipeRefresh.setThemeColors()

        forecastSwipeRefresh.setOnRefreshListener {
            setLatAndLng()
            forecastActivityPresenter.clearCacheForecast()
        }
    }

    override fun getCurrentForecast() {
        forecastActivityPresenter.getCurrentForecast()
    }

    override fun setToolbarInfo(forecast: Forecast) {
        toolbarCurrentTempTextView.text = getString(R.string.celsius_format, forecast.tempAndHumidity.temp)
        toolbarMinAndMaxTempTextView.text = getString(R.string.temp_min_and_max_celsius_format, forecast.tempAndHumidity.tempMin, forecast.tempAndHumidity.tempMax)
        toolbarWindSpeedTextView.text = getString(R.string.wind_ms_format, forecast.wind.speed)
        toolbarHumidityPercentageTextView.text = getString(R.string.humidity_percentage_format, forecast.clouds.all)
        toolbarCloudsPercentageTextView.text = getString(R.string.clouds_percentage_format, forecast.tempAndHumidity.humidity)
        toolbarDateTextView.text = forecast.date.formatDate()
        val descriptionWithUppercase = forecast.weather[0].description.substring(0, 1).toUpperCase() + forecast.weather[0].description.substring(1)
        toolbarWeatherTextView.text = descriptionWithUppercase

        if (forecast.rain != null && forecast.rain?.volumeLastThreeHours != null) {
            toolbarPrecipitationTextView.visibility = View.VISIBLE
            toolbarPrecipitationTextView.text = getString(R.string.rain_mm_format, forecast.rain?.volumeLastThreeHours)
        } else
            toolbarPrecipitationTextView.visibility = View.GONE

        toolbarImageView.setWeatherImageView(forecast.weather[0], this)

        textToSpeech(forecast.createSpeech())
    }

    override fun setNameOfTheCity(name: String) {
        toolbarCityTextView.text = name
        collapsingToolbar.title = name
    }

    override fun reloadAdapter() {
        (forecastRecyclerView.adapter as ForecastActivityRecyclerAdapter).reloadAdapter()
    }

    override fun getSwipeRefreshLayout(): SwipeRefreshLayout = forecastSwipeRefresh

    override fun onResume() {
        super.onResume()
        googleApiClient.connect()
    }

    override fun onPause() {
        super.onPause()
        googleApiClient.disconnect()
    }

    override fun onDestroy() {
        forecastRecyclerView.adapter = null
        forecastActivityPresenter.clear()
        toSpeech?.stop()
        toSpeech?.shutdown()
        super.onDestroy()
    }

}
