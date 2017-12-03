package manuelperera.com.weather3.domain.objects

import com.google.gson.annotations.SerializedName
import manuelperera.com.base.screen.presenter.recyclerview.RecyclerViewAdapterItem

class Forecast(@SerializedName("main")
               var tempAndHumidity: TempAndHumidity,
               var weather: List<Weather>,
               var clouds: Clouds,
               var wind: Wind,
               var rain: Rain?,
               @SerializedName("dt_txt")
               var date: String) : RecyclerViewAdapterItem {

    constructor(type: RecyclerViewAdapterItem.Type) : this(TempAndHumidity(0.0, 0.0, 0.0, 0), listOf(), Clouds(0), Wind(0.0, 0.0), Rain(0.0), "") {
        this.rType = type
    }

    var rType: RecyclerViewAdapterItem.Type = RecyclerViewAdapterItem.Type.ITEM

    override fun setType(type: RecyclerViewAdapterItem.Type) {
        rType = type
    }

    override fun getType(): RecyclerViewAdapterItem.Type = rType

    fun createSpeech(): String {
        var speech: String = "Actual temperature is " + tempAndHumidity.temp.toString() + ", "
        speech = speech + "wind speed is " + wind.speed.toString() + "meters per second, "
        speech = if (rain != null)
            speech + "and rain in milimeters in the last 3 hours is " + rain?.volumeLastThreeHours
        else
            speech + "and rain in milimeters in the last 3 hours is 0"
        return speech
    }
}