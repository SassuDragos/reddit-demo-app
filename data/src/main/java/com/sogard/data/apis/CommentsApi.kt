package com.sogard.data.apis

import com.sogard.data.models.CommentDAO
import com.sogard.data.models.DataWrapper
import com.sogard.data.models.ListingDataWrapper
import com.sogard.data.models.PostDAO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentsApi {

    @GET("/comments/{articleId}")
    fun getComments(
        @Path("articleId") articleId: String,
        @Query("after") nextElement: String?,
        @Query("count") totalElementsLoaded: Int,
        @Query("limit") maxListSize: Int,
        @Query("depth") maxDepth: Int
    ): Single<List<DataWrapper<ListingDataWrapper<CommentDAO>>>>
}