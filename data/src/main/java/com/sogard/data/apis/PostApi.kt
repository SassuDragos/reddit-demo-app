package com.sogard.data.apis

import com.sogard.data.models.DataWrapper
import com.sogard.data.models.ListingDataWrapper
import com.sogard.data.models.PostDAO
import io.reactivex.Single
import retrofit2.http.GET

interface PostApi {

    @GET("/top")
    fun getTopPosts(): Single<DataWrapper<ListingDataWrapper<PostDAO>>>
}