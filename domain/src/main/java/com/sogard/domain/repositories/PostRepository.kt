package com.sogard.domain.repositories

import com.sogard.domain.models.Post
import io.reactivex.Single

interface PostRepository {

    fun getTopPosts(): Single<List<Post>>
}