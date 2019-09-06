package com.sogard.data.models

import com.squareup.moshi.Json


/**
 *
 *  [General API Implementation Considerations]:
 *
 *  Generally, Reddit API is based on Generic Data Wrappers. Each response contains sublcasses of two
 *  types of data: [DataWrapper] and [CoreRedditDAO]. The [CoreRedditDAO] contains the requested data,
 *  while the [DataWrapper] offers information about the data (e.g. data type, context information,
 *  pagination information).
 *
 *  Up until now, the application uses 4 data types:
 *      -> [ListingWrapper] describes a [ListingData]: Used when requesting lists.
 *      -> [ArticleWrapper] describes an [ArticleDAO]: Used when requesting articles.
 *      -> [CommentWrapper] describes an [CommentDAO]: Used when requesting comments
 *      -> [MoreDataWrapper] describes an [MoreDataDAO]: Used as list children, when more data exists
 *      than the one returned in the response.
 *
 *
 *  [Reddit API identifiers]
 *  When working with [CoreRedditDAO], the API uses (at least) two important object identifiers:
 *      -> [id]: an alphanumeric string (e.g. cxkzul)
 *      -> [name]: The [id] prefixed with the data type (e.g. t3_cxkzul, t1_cxtyui).
 *
 *  For more information about available types check [DataWrapperType].
 *
 *  [General API Pagination Considerations]:
 *
 *  When requesting article data, Reddit uses a dynamic pagination algorithm. This algorithm relies
 *  on the number of already loaded articles and the [after] / [before]. Using this two parameters,
 *  Reddit API can return paginated responses.
 *
 */


const val KEY_WRAPPER_TYPE = "kind"

/**
 *  Data type describing the content of the data field.
 *
 *  The type of a DataWrapper is given by the associated [DataWrapperType]. For example, a
 *  [DataWrapper] with a [t1] value will mean that it holds a CommendDAO in its data.
 */
enum class DataWrapperType {
    /** List */
    Listing,

    /** Comment */
    t1,

    /** Link (Article) */
    t3,

    /** More data available */
    more,
    t2,
    t4,
    t5,
    t6
}

sealed class DataWrapper<T : CoreRedditDAO>(
    @field:Json(name = "kind") val dataWrapperType: DataWrapperType,
    open val data: T
)

data class ListingWrapper<T: CoreRedditDAO>(override val data: ListingData<T>) :
    DataWrapper<ListingData<T>>(DataWrapperType.Listing, data)

data class ArticleWrapper(override val data: ArticleDAO) :
    DataWrapper<ArticleDAO>(DataWrapperType.t3, data)

data class CommentWrapper(override val data: CommentDAO) :
    DataWrapper<CommentDAO>(DataWrapperType.t1, data)

data class MoreDataWrapper(override val data: MoreDataDAO) :
    DataWrapper<MoreDataDAO>(DataWrapperType.more, data)


/**
 * Mapping function that simplifies the navigation through the wrapping object fields.
 * */
internal fun <T: CoreRedditDAO> ListingWrapper<T>.getContent(): List<T> {
    return this.data.children.map { it.data }
}

