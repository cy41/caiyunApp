package com.example.caiyunmvvmdemo.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.caiyunmvvmdemo.AppAplication
import com.example.caiyunmvvmdemo.MainActivity
import com.example.caiyunmvvmdemo.R
import com.example.caiyunmvvmdemo.WeatherActivity
import com.example.caiyunmvvmdemo.data.Place
import com.example.caiyunmvvmdemo.databinding.PlaceItemBinding
import com.example.caiyunmvvmdemo.myFragment.PlaceFragment
import kotlinx.android.synthetic.main.activity_weather.*

class PlaceAdapter(val fragment: Fragment,val list: List<Place>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {


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
                this.place=place
                setClickListener {
                    Toast.makeText(AppAplication.context,"click",Toast.LENGTH_SHORT).show()
                    if(fragment.activity is WeatherActivity){
                        (fragment.activity as WeatherActivity).apply {
                            drawerLayout.closeDrawers()
                            viewModel.locationLat=place.location.lat
                            viewModel.locationLng=place.location.lng
                            viewModel.placeName=place.name
                            refreshWeather()
                        }

                    }
                    else{
                        val intent=Intent(AppAplication.context,WeatherActivity::class.java).apply {
                            putExtra("location_lng",place.location.lng)
                            putExtra("location_lat",place.location.lat)
                            putExtra("place_name",place.name)
                        }
                        (fragment as PlaceFragment).viewModel.savePlace(place)
                        fragment.activity?.finish()
                        fragment.startActivity(intent)
                    }
                }
            }
        }

    }
}