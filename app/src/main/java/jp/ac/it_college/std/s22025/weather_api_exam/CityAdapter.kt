package jp.ac.it_college.std.s22025.weather_api_exam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.ac.it_college.std.s22025.weather_api_exam.databinding.RowBinding

class CityAdapter(
    private val callback: (City) -> Unit
) :
    RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    class ViewHolder(val binding: RowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            RowBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = cityList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.apply {
            text = cityList[position].name
            setOnClickListener {
                callback(cityList[position])
            }
        }
    }
}