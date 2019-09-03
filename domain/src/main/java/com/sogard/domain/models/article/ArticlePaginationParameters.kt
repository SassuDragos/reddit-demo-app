package com.sogard.domain.models.article

data class ArticlePaginationParameters(val nextAnchor: String?,
                                       val totalLoadedItems: Int,
                                       val maxListSize: Int)