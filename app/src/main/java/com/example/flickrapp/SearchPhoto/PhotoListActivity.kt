package com.example.flickrapp.SearchPhoto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flickrapp.R
import com.example.flickrapp.network.NetworkState
import com.example.flickrapp.utils.ProgressLoading
import kotlinx.android.synthetic.main.activity_main.*

class PhotoListActivity : AppCompatActivity() , OnPhotoListener {


    private val photoVM : PhotoViewModel by viewModels()

    private lateinit var adapter : PhotoAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initList()
        initViewModel()
    } // fun of onCreate


    private fun initList()
    {
        adapter = PhotoAdapter(context = this,onPhotoListener = this)
        recyclerView_photos.layoutManager = LinearLayoutManager(this)
        recyclerView_photos.adapter = adapter
    } // fun of initList
    
    private fun initViewModel()
    {
        photoVM.searchPhoto()
        photoVM.networkState.observe(this,networkStateObserver)
        photoVM.pagingNetworkState?.observe(this,pagingNetworkStateObserver)
        photoVM.itemPagedReviewList?.observe(this,photoObserver)
    } // fun of initViewModel


    private val networkStateObserver = Observer<NetworkState> { networkState ->
        when(networkState.status)
        {
            NetworkState.Status.RUNNING -> {
                ProgressLoading.show(this)
            } // LOADING
            NetworkState.Status.SUCCESS -> {
                ProgressLoading.dismiss()
            }// LOADED
            NetworkState.Status.FAILED -> {
                ProgressLoading.dismiss()
                Toast.makeText(this,getString(R.string.something_wrong),Toast.LENGTH_SHORT).show()
            } // FAILED
        }
    } // networkStateObserver

    private val pagingNetworkStateObserver = Observer<NetworkState> { networkState ->
        adapter.setNetworkState(newNetworkState = networkState.status)
    } // networkStateObserver

    private val photoObserver = Observer<PagedList<Photo>>
    {
        adapter.submitList(it)
    } // photoObserver

    override fun onClick(photo: String) {
        FullScreenActivity.startActivity(context = this,imagePath = photo)
    } // fun of onClick 
}