package com.sogard.domain.models.article

data class PaginatedResponse<T>(
    val data: T,
    val nextPaginationAnchor: String
)