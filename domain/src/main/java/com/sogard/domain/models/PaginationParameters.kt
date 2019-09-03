package com.sogard.domain.models

data class PaginationParameters(val nextAnchor: String?,
                                val totalLoadedItems: Int,
                                val maxListSize: Int)