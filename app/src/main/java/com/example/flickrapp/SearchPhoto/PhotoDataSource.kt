package com.example.flickrapp.SearchPhoto

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.flickrapp.network.ApiClient
import com.example.flickrapp.network.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoDataSource() : PageKeyedDataSource<Int, Photo>() {


    private val TAG = "PhotoDataSource"

    private val FIRST_PAGE = 1
    private val PER_PAGE = 20


    private var networkState: MutableLiveData<NetworkState>? = null

    init {

        networkState = MutableLiveData()
        
    }
    private var call: Call<PhotosResponse>?=null

    fun getNetworkState(): MutableLiveData<NetworkState>? {
        return networkState
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Photo>) {
        networkState!!.postValue(NetworkState.LOADING)

        call = ApiClient.build().searchPhotos(page = FIRST_PAGE,perPage = PER_PAGE)
        call?.enqueue(object : Callback<PhotosResponse>
        {
            override fun onFailure(call: Call<PhotosResponse>, t: Throwable) {
                val errorMessage = t.message!!
                Log.e(TAG,"intial $errorMessage")

                networkState!!.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
            }

            override fun onResponse(call: Call<PhotosResponse>, response: Response<PhotosResponse>) {

                if(response.isSuccessful)
                {
                    if (response.body() != null)
                    {



                        callback.onResult(response.body()?.photos?.photo!!, null, FIRST_PAGE + 1)
                    }
                    Log.e(TAG,"intial ${response.body()?.photos?.photo!!.size}")
                    networkState!!.postValue(NetworkState.getLoaded(addBanner(response.body()?.photos?.photo!!)))

                }
                else
                {
                    networkState!!.postValue(NetworkState(NetworkState.Status.FAILED,response.message()))

                }
            }
        })

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {

        networkState!!.postValue(NetworkState.LOADING)

        call = ApiClient.build().searchPhotos(page = params.key,perPage = PER_PAGE)
        call?.enqueue(object : Callback<PhotosResponse>
        {
            override fun onFailure(call: Call<PhotosResponse>, t: Throwable) {
                val errorMessage = t.message!!
                Log.e(TAG,"after $errorMessage")

                networkState!!.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
            }

            override fun onResponse(call: Call<PhotosResponse>, response: Response<PhotosResponse>) {

                if(response.isSuccessful)
                {
                    if (response.body() != null)
                    {
                        callback.onResult(response.body()?.photos?.photo!!, params.key + 1)
                    }
                    Log.e(TAG,"after ${response.body()?.photos?.photo!!.size}")
                    networkState!!.postValue(NetworkState.getLoaded(addBanner(response.body()?.photos?.photo!!)))
                }
                else
                {
                    networkState!!.postValue(NetworkState(NetworkState.Status.FAILED,response.message()))
                }
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {

        call = ApiClient.build().searchPhotos(page  = FIRST_PAGE,perPage = PER_PAGE)
        call?.enqueue(object : Callback<PhotosResponse>
        {
            override fun onFailure(call: Call<PhotosResponse>, t: Throwable) {
                val errorMessage = t.message!!
                Log.e(TAG,"before $errorMessage")

                networkState!!.postValue(NetworkState(NetworkState.Status.FAILED, errorMessage))
            }

            override fun onResponse(call: Call<PhotosResponse>, response: Response<PhotosResponse>) {

                if(response.isSuccessful)
                {
                    val adjacentKey = if (params.key > 1) params.key - 1 else null

                    if (response.body() != null)
                    {
                        callback.onResult(response.body()?.photos?.photo!!, adjacentKey)
                    }
                    Log.e(TAG,"before ${response.body()?.photos?.photo!!.size}")
                    networkState!!.postValue(NetworkState.getLoaded(addBanner(response.body()?.photos?.photo!!)))

                }
                else
                {

                }
            }
        })
    }


    private fun addBanner(photoList : ArrayList<Photo>) : List<Photo>
    {
        for (i in 0 until photoList.size)
        {
           if(i % 5 == 0 && i != 0)
           {
               photoList.add(Photo(id = "Banner"))
           }
        }

        return photoList
    } // fun of addBanner
}