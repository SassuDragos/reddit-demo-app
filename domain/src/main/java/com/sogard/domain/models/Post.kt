package com.sogard.domain.models

data class Post(val id: String,
                val title: String,
                val image: String,
                val totalCommentNumber: Int)