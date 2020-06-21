package com.example.caiyunmvvmdemo.myFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiyunmvvmdemo.R
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
        initRecyclerView()
        observeUI()
    }

    private fun initRecyclerView(){
        val layoutManager= LinearLayoutManager(activity)
        adapter= PlaceAdapter(viewModel.placeList)
        binding.apply {
            recyclerView.adapter=adapter
            recyclerView.layoutManager=layoutManager

            searchPlaceEdit.apply {
                addTextChangedListener(object : TextWatcher{
                    override fun afterTextChanged(s: Editable?) {
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

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                })
            }
        }
    }

    private fun observeUI(){
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
    }
}