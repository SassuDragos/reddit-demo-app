package com.sogard.domain.models.article

data class PaginatedArticleData(
    val data: List<Article>,
    val nextPaginationAnchor: String
)

