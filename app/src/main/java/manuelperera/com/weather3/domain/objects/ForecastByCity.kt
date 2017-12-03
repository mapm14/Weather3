package manuelperera.com.weather3.domain.objects

import com.google.gson.annotations.SerializedName

class ForecastByCity(var city: City,
                     @SerializedName("list")
                     var forecasts: List<Forecast>)