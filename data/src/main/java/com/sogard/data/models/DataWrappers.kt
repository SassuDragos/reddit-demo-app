package com.sogard.data.models

import com.squareup.moshi.Json

const val KEY_WRAPPER_TYPE = "kind"

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

data class ListingWrapper<T>(override val data: ListingData<T>) :
    DataWrapper<ListingData<T>>(DataWrapperType.Listing, data)

data class ArticleWrapper(override val data: ArticleDAO) :
    DataWrapper<ArticleDAO>(DataWrapperType.t3, data)

data class CommentWrapper(override val data: CommentDAO) :
    DataWrapper<CommentDAO>(DataWrapperType.t1, data)

data class MoreDataWrapper(override val data: MoreDataDAO) :
    DataWrapper<MoreDataDAO>(DataWrapperType.more, data)


internal fun <T: CoreRedditDAO> ListingWrapper<DataWrapper<T>>.getContent(): List<T> {
    return this.data.children.map { it.data }
}

