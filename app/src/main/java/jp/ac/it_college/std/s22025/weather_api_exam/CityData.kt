package jp.ac.it_college.std.s22025.weather_api_exam

data class City(
    val name: String,
    val q: String
)

val cityList = listOf(
    City("大阪", "Osaka"),
    City("神戸", "Kobe"),
    City("京都", "Kyoto"),
    City("大津", "Otsu"),
    City("奈良", "Nara"),
    City("和歌山", "Wakayama"),
    City("姫路", "Himeji"),
    City("那覇", "Naha"),
)