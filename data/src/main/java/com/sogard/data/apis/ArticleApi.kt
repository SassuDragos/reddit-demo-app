package com.sogard.data.apis

import com.sogard.data.models.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API in charge with handling Reddit articles (Links in their documentation).
 */
interface ArticleApi {

    /**
    * Request to get top articles.
     *
     * [after]: The [name] of the next anchor element. See [ArticleDAO] for a more thorough
     *          explanation of  [after].
     * [count]: Total number of elements which have been loaded in the app so far. This helps Reddit
     *          to provide relevant paginated responses.
     * [limit]: The maximum number of article elements expected in the list.
    * */
    @GET("/top")
    fun getTopArticles(
        @Query("after") nextElement: String?,
        @Query("count") totalElementsLoaded: Int,
        @Query("limit") maxListSize: Int
    ): Single<ListingWrapper<ArticleDAO>>
}