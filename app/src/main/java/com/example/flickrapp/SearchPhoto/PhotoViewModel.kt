package com.example.flickrapp.SearchPhoto

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.flickrapp.base.BaseViewModel
import com.example.flickrapp.network.NetworkState

class PhotoViewModel : BaseViewModel() {

    var itemPagedReviewList: LiveData<PagedList<Photo>>? = null

    private var liveDataReviewSource: LiveData<PageKeyedDataSource<Int, Photo>>? = null

    var pagingNetworkState: LiveData<NetworkState>? = null


    fun searchPhoto()
    {
        val itemDataSourceFactory = PhotoFactory()
        liveDataReviewSource = itemDataSourceFactory.getDataSource()

        pagingNetworkState = Transformations.switchMap(
            itemDataSourceFactory.getMutableLiveData()
        ) { dataSource: PhotoDataSource -> dataSource.getNetworkState() }

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(20)
            .setPageSize(20).build()
        itemPagedReviewList = LivePagedListBuilder(itemDataSourceFactory, pagedListConfig)
            .build()

    } // fun of getOwnerCategoryProducts
}