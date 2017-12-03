package manuelperera.com.weather3.domain.objects

import com.google.gson.annotations.SerializedName

class TempAndHumidity(var temp: Double,
                      @SerializedName("temp_min")
                      var tempMin: Double,
                      @SerializedName("temp_max")
                      var tempMax: Double,
                      var humidity: Int)