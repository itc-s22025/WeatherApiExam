package jp.ac.it_college.std.s22025.weather_api_exam.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WhetherInfo (
    //座標
    @SerialName("coord") val coordinates: Coordinates,

    //天気
    val weather: List<Whether>,

    //main以下
    @SerialName("main") val mainContents: MainContents,

    //風情報
    val wind: Wind,

    //都市ID
    @SerialName("id") val cityId: Int,

    //都市名
    @SerialName("name") val cityName: String,
)

@Serializable
data class Coordinates(
    //経度
    @SerialName("lon") val longitude: Double,

    //緯度
    @SerialName("lat") val latitude: Double
)

@Serializable
data class Whether(
    val id: Int,

    @SerialName("main") val groupName: String,

    val description: String,

    val icon: String
)

//main以下(気温・体感気温・気圧・湿度)
@Serializable
data class MainContents(
    //気温
    @SerialName("temp") val temperature: Double,

    //体感温度
    @SerialName("feels_like") val feelsLike: Double,

    @SerialName("temp_min") val tempMin: Double,

    @SerialName("temp_max") val tempMax: Double,

    //気圧
    val pressure: Double,

    //湿度
    val humidity: Double
)

//wind以下(風速・風向)
@Serializable
data class Wind(
    //風速
    val speed: Double,

    //風向
    @SerialName("deg") val windDegrees: Int

)
