package com.sogard.domain.models.article

data class Article(val id: String,
                   val title: String,
                   val image: String,
                   val totalCommentNumber: Int,
                   val detailsUrl: String)