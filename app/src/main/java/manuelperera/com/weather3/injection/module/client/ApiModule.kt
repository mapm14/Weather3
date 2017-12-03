package manuelperera.com.weather3.injection.module.client

import dagger.Module
import dagger.Provides
import manuelperera.com.weather3.BuildConfig
import manuelperera.com.weather3.client.apiclient.ForecastApiClient
import manuelperera.com.weather3.client.apiclient.ForecastApiClientImpl
import manuelperera.com.weather3.client.retrofit.RetrofitForecastApiClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun okHttpClient(): OkHttpClient =
            OkHttpClient().newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    })
                    .addInterceptor({ chain ->
                        var request = chain.request()
                        val url = request.url().newBuilder().addQueryParameter("appid", BuildConfig.WEATHER_API_KEY).build()
                        request = request.newBuilder().url(url).build()
                        chain.proceed(request)
                    })
                    .build()

    @Provides
    fun forecastsApiClient(client: OkHttpClient): ForecastApiClient =
            ForecastApiClientImpl(Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                    .create(RetrofitForecastApiClient::class.java))

}