package com.sogard.data.apis

import com.sogard.data.models.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API in charge with handling Reddit comments.
 */
interface CommentsApi {

    /**
     * HTTP request which returns the tree of comments for an article.
     *
     * [articleId]: The id of the related article.
     *
     * [limit]: The maximum number of article elements expected in the list.
     *
     * [depth]: The depth of the comment tree. Each extra depth level will lead to an extra depth of
     *          replies in the comment tree. Note that it is correlated with the list [limit].
     * */
    @GET("/comments/{articleId}")
    fun getCommentsForArticle(
        @Path("articleId") articleId: String,
        @Query("limit") maxListSize: Int,
        @Query("depth") maxDepth: Int
    ): Single<List<ListingWrapper<CoreRedditDAO>>>
}