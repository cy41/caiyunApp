package com.example.caiyunmvvmdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiyunmvvmdemo.data.ShowMltiData
import com.example.caiyunmvvmdemo.databinding.ForecastItemBinding

class WeatherAdapter(val list: List<ShowMltiData>): RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ForecastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int =list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(val itemBinding: ForecastItemBinding): RecyclerView.ViewHolder(itemBinding.root){

        fun bind(item: ShowMltiData){
            itemBinding.apply {
                this.item=item
                skyIcon.setImageResource(item.skyIcon)
            }

        }

    }
}
