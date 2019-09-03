package com.sogard.data.apis

import com.sogard.data.models.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {

    @GET("/top")
    fun getTopArticles(
        @Query("after") nextElement: String?,
        @Query("count") totalElementsLoaded: Int,
        @Query("limit") maxListSize: Int
    ): Single<ListingWrapper<DataWrapper<ArticleDAO>>>
}