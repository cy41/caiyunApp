package com.example.caiyunmvvmdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.caiyunmvvmdemo.AppAplication
import com.example.caiyunmvvmdemo.R
import com.example.caiyunmvvmdemo.data.Place
import com.example.caiyunmvvmdemo.databinding.PlaceItemBinding

class PlaceAdapter(val list: List<Place>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.place_item,parent,false))
    }

    override fun getItemCount() =list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(val binding: PlaceItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(place: Place){
            binding.apply {
                placeName.text=place.name
                placeAddress.text=place.address
                setClickListener {
                    Toast.makeText(AppAplication.context,"click",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}