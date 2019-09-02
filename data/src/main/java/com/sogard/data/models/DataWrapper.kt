package com.sogard.data.models

import com.squareup.moshi.Json

sealed class DataType(val kind: String){
    class Link: DataType("t3")
    class Comment: DataType("t1")
}

//TODO: replace String type for [kind] with the DataType sealed class.
//TODO: Deserialization of data should be dynamic based on the [kind] type
data class DataWrapper<T>(
    val kind: String,
    val data: T
)

/**
 * [children]: The list of elements ("a slice of the list") requested from the API.
 * [after]: The name of the element which precedes the items provided in the [children] list. Reddit returns
 * only a slice of the existing list of elements (e.g. list of posts, list of comments, list of reddits, etc.).
 * the [after] parameter is used as the anchor point of the [children] slice
 * [before]: The name of the element which follows the items provided in the [children] list.
 * */
data class ListingDataWrapper<T>(
    val children: List<DataWrapper<T>>,
    @field:Json(name = "after") val previousElementName: String?,
    @field:Json(name = "before") val nextElementName: String?
)

internal fun <T> DataWrapper<ListingDataWrapper<T>>.getContent(): List<T> {
    return this.data.children.map { it.data }
}

