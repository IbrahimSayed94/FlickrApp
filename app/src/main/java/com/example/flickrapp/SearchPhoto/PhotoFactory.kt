package com.example.flickrapp.SearchPhoto

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource

class PhotoFactory: DataSource.Factory<Int, Photo>()  {

    private val mutableLiveData: MutableLiveData<PhotoDataSource> = MutableLiveData()

    private val dataSource: MutableLiveData<PageKeyedDataSource<Int, Photo>> = MutableLiveData()

    override fun create(): DataSource<Int, Photo> {
        val itemDataSource = PhotoDataSource()
        dataSource.postValue(itemDataSource)
        mutableLiveData.postValue(itemDataSource)

        return itemDataSource
    }

    fun getDataSource(): MutableLiveData<PageKeyedDataSource<Int, Photo>> {
        return dataSource
    }
    fun getMutableLiveData(): MutableLiveData<PhotoDataSource> {
        return mutableLiveData
    }
}