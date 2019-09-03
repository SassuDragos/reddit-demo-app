package com.sogard.domain.models

data class CommentsPaginationParams(val articleId: String,
                                    val maxListSize: Int,
                                    val maxDepth: Int)