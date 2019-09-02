package com.sogard.data.apis

import com.sogard.data.models.DataWrapper
import com.sogard.data.models.ListingDataWrapper
import com.sogard.data.models.PostDAO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PostApi {

    @GET("/top")
    fun getTopPosts(
        @Query("after") nextElement: String?,
        @Query("count") totalElementsLoaded: Int,
        @Query("limit") maxListSize: Int
    ): Single<DataWrapper<ListingDataWrapper<PostDAO>>>
}