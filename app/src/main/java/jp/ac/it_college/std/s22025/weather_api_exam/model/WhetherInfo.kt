package jp.ac.it_college.std.s22025.weather_api_exam.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WhetherInfo (
    //時間単位のlist
    @SerialName("list") val hourlyList: List<Hourly>,

    //都市情報
    @SerialName("city") val cityInfo: CityInfo
)

@Serializable
data class Hourly(
    val dt: Int,

    @SerialName("main") val mainContents: MainContents,

    val weather: List<Weather>,

    val wind: Wind,

    @SerialName("dt_txt") val dateText: String,

    @SerialName("pop") val rainPer: Double

)

@Serializable
data class CityInfo(
    @SerialName("id") val cityId: Int,

    @SerialName("name") val cityName: String,

    @SerialName("coord") val coordinates: Coordinates
)

//main以下(気温・体感気温・気圧・湿度)
@Serializable
data class MainContents(
    @SerialName("temp") val temperature: Double,
    //体感温度
    @SerialName("feels_like") val feelsLike: Double,

    @SerialName("temp_min") val tempMin: Double,

    @SerialName("temp_max") val tempMax: Double,

    //気圧
    val pressure: Double,

    @SerialName("sea_level") val seaLevel: Int,

    @SerialName("grnd_level") val groundLevel: Int,

    //湿度
    val humidity: Double,

    @SerialName("temp_kf") val tempKf: Double

)

@Serializable
data class Weather(
    val id: Int,

    @SerialName("main") val weatherName: String,

    val description: String,

    val icon: String
)

//wind以下(風速・風向)
@Serializable
data class Wind(
    //風速
    val speed: Double,

    //風向
    @SerialName("deg") val windDegrees: Int,

    //瞬間風速
    val gust: Double

)

@Serializable
data class Coordinates(
    //緯度
    @SerialName("lat") val latitude: Double,
    //経度
    @SerialName("lon") val longitude: Double


)