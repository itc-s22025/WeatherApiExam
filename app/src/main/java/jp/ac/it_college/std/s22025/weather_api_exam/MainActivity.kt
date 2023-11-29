package jp.ac.it_college.std.s22025.weather_api_exam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.json
import jp.ac.it_college.std.s22025.weather_api_exam.databinding.ActivityMainBinding
import jp.ac.it_college.std.s22025.weather_api_exam.model.WhetherInfo
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

private val ktorClient = HttpClient(CIO) {
    engine {
        endpoint {
            connectTimeout = 5000
            requestTimeout = 5000
            socketTimeout = 5000
        }
    }
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            }
        )
    }
}

class MainActivity : AppCompatActivity() {
    companion object {
        private const val WEATHER_INFO_URL =
            "https://api.openweathermap.org/data/2.5/weather?lang=ja"
        private const val IMAGE_URL = "http://openweathermap.org/img/w/"
        private const val IMAGE_FORMAT = ".png"
        private const val APP_ID = BuildConfig.APP_ID
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvCityList.apply {
            adapter = CityAdapter {
                getWeatherInfo(it.q)
            }
            layoutManager = LinearLayoutManager(context)
        }
    }

    @UiThread
    private fun getWeatherInfo(q: String){
        lifecycleScope.launch {
            //データ取得
            val site_url = "$WEATHER_INFO_URL&q=$q&appid=$APP_ID"
//            val image_url = "$IMAGE_URL$IMAGE_FORMAT"
            val result = ktorClient.get {
                url(site_url)
            }.body<WhetherInfo>()

            // 取得したデータを UI に反映
            result.run {
                binding.tvWeatherTelop.text = getString(R.string.tv_telop, cityName)
                binding.tvWeatherDesc.text = getString(R.string.tv_desc, weather[0].description)
                binding.tvWetherMore.text = getString(
                    R.string.tv_more,
                    mainContents.temperature -273,
                    mainContents.feelsLike -273,
                    mainContents.pressure,
                    mainContents.humidity,
                    wind.speed,
                    wind.windDegrees
                )
            }
        }
    }
}