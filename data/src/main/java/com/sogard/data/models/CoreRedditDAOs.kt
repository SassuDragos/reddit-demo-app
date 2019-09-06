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


/** Base class for all Reddit DAO */
sealed class CoreRedditDAO

/**
 * Data class wrapping a list of CoreRedditDAO which can be found in [children].
 *
 * [children]: The list of elements ("a slice of the list") requested from the API.
 * [after]: The name of the element which follows the items provided in the [children] list. Reddit returns
 * only a slice of the existing list of elements (e.g. list of posts, list of comments, list of reddits, etc.).
 * the [after] parameter can be used as an anchor point for the next [children] slice.
 * [before]: The name of the element which precedes the items provided in the [children] list.
 * */
data class ListingData<T: CoreRedditDAO>(
    val children: List<DataWrapper<T>>,
    @Json(name = "before") val previousAnchor: String?,
    @Json(name = "after") val nextAnchor: String?
) : CoreRedditDAO()


/**
 * [id]: An alphanumeric string (e.g. cxkzul)
 * [name]: The [id] prefixed with the data type (e.g. t3_cxkzul, t1_cxtyui)
 * [title]: The article title.
 * [authorName]: ..
 * [totalComments]: Total available comments for the respective article.
 * [endpoint]: The endpoint where the article detail page can be.
 */
data class ArticleDAO(
    val id: String,
    val name: String,
    val title: String,
    @Json(name = "author_fullname") val authorName: String,
    @Json(name = "num_comments") val totalComments: Int,
    @Json(name = "permalink") val endpoint: String
) : CoreRedditDAO()

/**
 *  Comment Data object.
 *
 *  [body]: the content of the comment.
 * */
data class CommentDAO(
    val id: String,
    @Json(name = "author_fullname") val authorName: String,
    val body: String
    //TODO: Fix deserialization issue
//    @Json(name = "num_comments") val totalComments: Int,
) : CoreRedditDAO()

/**
 *  Data object describing what other data is available in the parrent listing.
 *
 *  [id]: Description identifier.
 *  [totalUndiscoveredElements]: Total amount of elements which have not been loaded.
 *  [parentId]: The id of the parent comment.
 *  [depth]: the depth of the current comment list in the comment tree.
 *  [children]: the [id] of all the unloaded comments.
 * */
data class MoreDataDAO(
    val id: String,
    @Json(name = "count") val totalUndiscoveredElements: Int,
    @Json(name = "parent_id") val parentId: String,
    val depth: Int,
    val children: List<String>
) : CoreRedditDAO()