package com.example.flickrapp.SearchPhoto

data class PhotosResponse(
    val stat : String ?= "",
    val photos : PhotoData ?= PhotoData()
)

data class PhotoData(
    val page : Int ?= 0,
    val total : Int ?= 0,
    val photo : ArrayList<Photo> ?= ArrayList()
)

data class Photo(
    val id : String ?= "",
    val server : String ?= "",
    val secret : String ?= "",
    val title : String ?= "",
    val farm : String ?= ""
)
