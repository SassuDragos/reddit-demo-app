package com.sogard.domain.models

data class PaginatedResponse<T>(
    val data: T,
    val nextPaginationAnchor: String
)