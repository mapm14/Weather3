package manuelperera.com.weather3.domain.objects

import com.google.gson.annotations.SerializedName

class City(var id: String,
           var name: String,
           @SerializedName("coord")
           var coordinates: Coordinates,
           var country: String)