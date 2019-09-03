package com.sogard.data.repositories

import com.sogard.data.apis.AppConfiguration.REDDIT_PUBLIC_BASE_URL
import com.sogard.data.apis.PostApi
import com.sogard.data.models.PostDAO
import com.sogard.data.models.getContent
import com.sogard.domain.models.article.PaginatedResponse
import com.sogard.domain.models.article.ArticlePaginationParameters
import com.sogard.domain.models.article.Article
import com.sogard.domain.repositories.ArticleRepository
import io.reactivex.Single


class ArticleRepositoryImpl(private val postApi: PostApi) : ArticleRepository {

    override fun getTopArticles(pagParam: ArticlePaginationParameters): Single<PaginatedResponse<List<Article>>> =
        postApi.getTopPosts(pagParam.nextAnchor, pagParam.totalLoadedItems, pagParam.maxListSize)
            .map { wrapper ->
                //TODO: create a Mapper class that handles domain <-> ui and domain <-> data mapping
                val postList =
                    wrapper.getContent().map { postDAO -> postDAO.toPost(REDDIT_PUBLIC_BASE_URL) }
                PaginatedResponse(
                    postList,
                    wrapper.data.nextAnchor ?: ""
                )
            }

    private fun PostDAO.toPost(baseUrl: String): Article {
        return Article(
            id,
            title,
            "",
            totalComments,
            baseUrl + endpoint
        )
    }
}