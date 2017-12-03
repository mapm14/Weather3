package manuelperera.com.weather3.domain.objects

import com.google.gson.annotations.SerializedName

class Rain(@SerializedName("3h")
           var volumeLastThreeHours: Double?)