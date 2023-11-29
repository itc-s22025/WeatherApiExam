package jp.ac.it_college.std.s22025.weather_api_exam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.ac.it_college.std.s22025.weather_api_exam.databinding.RowBinding

//RecyclerView.Adapter :リスト形式のデータを効率に表示するためのアダプタ
//Cityを一つ引数に取り、且つ何も返さない関数：callbackを引数に取る
class CityAdapter(private val callback: (City) -> Unit) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    //各行のレイアウトに関するビューホルダー
    class ViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            RowBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = cityList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // ViewHolder内のViewに都市の情報を表示
        holder.binding.name.apply {
            text = cityList[position].name
            setOnClickListener {
                // コールバック関数を呼び出し、選択された都市の情報を渡す
                callback(cityList[position])
            }
        }
    }
}