package jp.ac.it_college.std.s22025.weather_api_exam

data class City(
    val name: String,
    val q: String
)

val cityList = listOf(
    City("北海道(札幌)","Hokkaido"),
    City("青森","Aomori"),
    City("岩手(盛岡)","Iwate"),
    City("宮城(仙台)","Miyagi"),
    City("秋田","Akita"),
    City("山形","Yamagata"),
    City("福島","Fukushima"),
    City("茨城(水戸)","Ibaraki"),
    City("大阪", "Osaka"),
    City("神戸", "Kobe"),
    City("京都", "Kyoto"),
    City("大津", "Otsu"),
    City("奈良", "Nara"),
    City("和歌山", "Wakayama"),
    City("姫路", "Himeji"),
    City("那覇", "Naha"),
)