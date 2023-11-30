package jp.ac.it_college.std.s22025.weather_api_exam

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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
            "https://api.openweathermap.org/data/2.5/forecast?lang=ja"
        private const val IMAGE_URL = "https://openweathermap.org/img/wn/"
        private const val IMAGE_FORMAT = ".png"
        private const val APP_ID = BuildConfig.APP_ID
    }

    private lateinit var binding: ActivityMainBinding

    //位置情報を取得するためのライブラリ↓
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //位置情報の更新に関する設定情報が格納されている↓
    private lateinit var locationRequest: LocationRequest

    //位置情報が取得できた・変わった等位置情報に関するイベントが発生したときのリスナメソッドがで意義されている↓
    private lateinit var locationCallback: LocationCallback

    //現在地のパーミッション
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGrantedFineLocation =
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
        val isGrantedCoarseLocation =
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
        //どちらかの権限が許可もらえたという場合
        if(isGrantedFineLocation || isGrantedCoarseLocation){
            requestLocationUpdates()
            return@registerForActivityResult
        }
        //結局権限の許可をもらえなかったとき とりあえずログだけだしとく
        Log.w("CHAPTER14", "許可がもらえなかったのでいじけました;-(")
    }

    private var latitude = 0.0
    private var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSearch.setOnClickListener(::onMapSearchButtonClick)


        //位置情報取得関連
        //位置情報取得クライアントを作成
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 5000
        ).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                //lastLocation: 最新の位置情報
                result.lastLocation?.also { location ->
                    //緯度経度取る
                    latitude = location.latitude
                    longitude = location.longitude
                }
            }
        }

        binding.rvCityList.apply {
            adapter = CityAdapter {
                getWeatherInfo(it.q)
            }
            layoutManager = LinearLayoutManager(context)
        }
    }

    //取得開始のリクエストを投げる↓
    override fun onResume() {
        super.onResume()
        requestLocationUpdates()
    }

    //リソースの開放
    override fun onPause() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        super.onPause()
    }

    //btSearchクリック時のイベント
    //getWeatherInfoと同じことしてるから後でまとめる
    @UiThread
    private fun onMapSearchButtonClick(view: View) {
        lifecycleScope.launch {

            val locationUrl = "$WEATHER_INFO_URL&lat=$latitude&lon=$longitude&appid=$APP_ID"

            val result = ktorClient.get {
                url(locationUrl)
            }.body<WhetherInfo>()

            result.run {
                binding.tvWeatherTelop.text = getString(R.string.tv_telop, cityInfo.cityName)
                binding.tvWeatherDesc.text = getString(R.string.tv_desc, hourlyList[0].dateText, hourlyList[0].weather[0].description)
                binding.tvWetherMore.text = getString(
                    R.string.tv_more,
                    hourlyList[0].mainContents.temperature -273,
                    hourlyList[0].mainContents.feelsLike -273,
                    hourlyList[0].mainContents.pressure,
                    hourlyList[0].mainContents.humidity,
                    hourlyList[0].wind.speed,
                    hourlyList[0].wind.gust,
                    hourlyList[0].wind.windDegrees
                )

                // 天気アイコンのURL
                val iconUrl = "$IMAGE_URL${hourlyList[0].weather[0].icon}$IMAGE_FORMAT"

                // Glideを使って画像をロードしてImageViewに表示
                Glide.with(this@MainActivity)
                    .load(iconUrl)
                    .into(binding.ivWeatherIcon)


            }
        }

    }

    @UiThread
    private fun getWeatherInfo(q: String){
        lifecycleScope.launch {
            //データ取得
            val siteUrl = "$WEATHER_INFO_URL&q=$q&appid=$APP_ID"

            //ktorClient.get{} ->GETリクエストを行うことを定義
            val result = ktorClient.get {
                //url() ->URLを設定　してリクエスト
                url(siteUrl)
                //.body ->取得にトライ
            }.body<WhetherInfo>()

            // 取得したデータを UI に反映
            result.run {
                binding.tvWeatherTelop.text = getString(R.string.tv_telop, cityInfo.cityName)
                binding.tvWeatherDesc.text = getString(R.string.tv_desc,hourlyList[0].dateText ,hourlyList[0].weather[0].description)
                binding.tvWetherMore.text = getString(
                    R.string.tv_more,
                    hourlyList[0].mainContents.temperature -273,
                    hourlyList[0].mainContents.feelsLike -273,
                    hourlyList[0].mainContents.pressure,
                    hourlyList[0].mainContents.humidity,
                    hourlyList[0].wind.speed,
                    hourlyList[0].wind.gust,
                    hourlyList[0].wind.windDegrees
                )

                // 天気アイコンのURL
                val iconUrl = "$IMAGE_URL${hourlyList[0].weather[0].icon}$IMAGE_FORMAT"

                // Glideを使って画像をロードしてImageViewに表示
                Glide.with(this@MainActivity)
                    .load(iconUrl)
                    .into(binding.ivWeatherIcon)
            }
        }
    }


    //location
    private fun requestLocationUpdates(){
        //true or false
        val isGrantedFineLocation = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        //true or false
        val isGrantedCoarseLocation = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        //位置情報取得の権限↑のうち、どちらか一方でも権限あれば(=trueなら)OKなので位置情報取得開始
        if (isGrantedFineLocation || isGrantedCoarseLocation){
            //権限チェック処理入れないと赤線が消えない
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                mainLooper
            )
            return
        }
        //ここまで来たらどの権限も無いのでリクエスト
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

}