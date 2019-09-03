package com.sogard.domain.models.comment

data class CommentsPaginationParams(val articleId: String,
                                    val maxListSize: Int,
                                    val maxDepth: Int)