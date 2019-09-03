package com.sogard.data.repositories

import com.sogard.data.AppConfiguration.REDDIT_PUBLIC_BASE_URL
import com.sogard.data.apis.PostApi
import com.sogard.data.models.PostDAO
import com.sogard.data.models.getContent
import com.sogard.domain.models.PaginatedResponse
import com.sogard.domain.models.PaginationParameters
import com.sogard.domain.models.Post
import com.sogard.domain.repositories.PostRepository
import io.reactivex.Single


class PostRepositoryImpl(private val postApi: PostApi) : PostRepository {

    override fun getTopPosts(pagParam: PaginationParameters): Single<PaginatedResponse<List<Post>>> =
        postApi.getTopPosts(pagParam.nextAnchor, pagParam.totalLoadedItems, pagParam.maxListSize)
            .map { wrapper ->
                //TODO: create a Mapper class that handles domain <-> ui and domain <-> data mapping
                val postList =
                    wrapper.getContent().map { postDAO -> postDAO.toPost(REDDIT_PUBLIC_BASE_URL) }
                PaginatedResponse(postList, wrapper.data.nextAnchor ?: "")
            }

    private fun PostDAO.toPost(baseUrl: String): Post {
        return Post(id, title, "", totalComments, baseUrl + endpoint)
    }
}