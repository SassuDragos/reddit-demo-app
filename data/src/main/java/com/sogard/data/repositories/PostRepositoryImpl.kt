package com.sogard.data.repositories

import com.sogard.data.apis.PostApi
import com.sogard.data.models.getContent
import com.sogard.domain.models.Post
import com.sogard.domain.repositories.PostRepository
import io.reactivex.Single

class PostRepositoryImpl(private val postApi: PostApi) : PostRepository {

    //TODO: A separate cache component could be used to save the posts in a database.

    private var topPostsList = mutableListOf<Post>()

    override fun getTopPosts(): Single<List<Post>> =
        postApi.getTopPosts().map { wrapper ->
            wrapper.getContent().map { postDAO -> postDAO.toPost() }
        }
//            .doOnSuccess { postList -> topPostsList.addAll(postList) }
}