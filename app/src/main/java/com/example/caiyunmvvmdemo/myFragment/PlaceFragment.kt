package com.example.caiyunmvvmdemo.myFragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiyunmvvmdemo.MainActivity
import com.example.caiyunmvvmdemo.R
import com.example.caiyunmvvmdemo.WeatherActivity
import com.example.caiyunmvvmdemo.adapter.PlaceAdapter
import com.example.caiyunmvvmdemo.databinding.FragmentPlaceBinding
import com.example.caiyunmvvmdemo.viewModels.PlaceViewModel
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment: Fragment() {
    val TAG="PlaceFragment"
    val viewModel by lazy {
        ViewModelProviders.of(this).get(PlaceViewModel::class.java)
    }

    lateinit var binding: FragmentPlaceBinding
    lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_place,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(activity is MainActivity && viewModel.isSavedPlace()){
            if(loadLocalData()) return
        }
        initUI()
        observeUI()
    }

    private fun loadLocalData(): Boolean{
        if(viewModel.isSavedPlace()){
            val place=viewModel.getSavedPlace()
            val intent=Intent(context,WeatherActivity::class.java).apply {
                putExtra("location_lng",place.location.lng)
                putExtra("location_lat",place.location.lat)
                putExtra("place_name",place.name)
            }
            startActivity(intent)
            activity?.finish()
            return true
        }
        return false
    }

    private fun initUI(){
        val layoutManager= LinearLayoutManager(activity)
        adapter= PlaceAdapter(this,viewModel.placeList)
        binding.apply {
            recyclerView.adapter=adapter
            recyclerView.layoutManager=layoutManager

            searchPlaceEdit.apply {
                addTextChangedListener { s ->
                    Log.d(TAG,"textChanged")
                    val str=s.toString()
                    println(str)
                    if(str.isNotEmpty()){
                        viewModel.searchPlaces(str)
                    }
                    else{
                        recyclerView.visibility=View.GONE
                        bgImageView.visibility=View.VISIBLE
                        viewModel.placeList.clear()
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            clear.setOnClickListener {
                searchPlaceEdit.setText("")
            }
        }
    }

    private fun observeUI(){
        viewModel.init()
        viewModel.placeLiveData.observe(this, Observer { result ->
            val places= result.getOrNull()
            if(places!=null){
                binding.apply {
                    recyclerView.visibility=View.VISIBLE
                    bgImageView.visibility=View.GONE
                    viewModel.placeList.apply {
                        clear()
                        addAll(places)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            else{
                Toast.makeText(context,"can't find anythings",Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.num.observe(this, Observer { count ->
            Log.d("hello","${count}")
            binding.num.text=count.toString()
        })
    }
}