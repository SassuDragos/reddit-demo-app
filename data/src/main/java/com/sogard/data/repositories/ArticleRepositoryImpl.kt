package com.sogard.data.repositories

import com.sogard.data.apis.AppConfiguration.REDDIT_PUBLIC_BASE_URL
import com.sogard.data.apis.ArticleApi
import com.sogard.data.models.ArticleDAO
import com.sogard.data.models.getContent
import com.sogard.domain.models.article.Article
import com.sogard.domain.models.article.ArticlePaginationParameters
import com.sogard.domain.models.article.PaginatedArticleData
import com.sogard.domain.repositories.ArticleRepository
import io.reactivex.Single


class ArticleRepositoryImpl(private val postApi: ArticleApi) : ArticleRepository {

    override fun getTopArticles(pagParam: ArticlePaginationParameters): Single<PaginatedArticleData> =
        postApi.getTopArticles(pagParam.nextAnchor, pagParam.totalLoadedItems, pagParam.maxListSize)
            .map { wrapper ->

                //TODO: create a Mapper class that handles domain <-> ui and domain <-> data mapping
                val articleList = wrapper.getContent().map { it.toPost(REDDIT_PUBLIC_BASE_URL) }
                PaginatedArticleData(
                    data = articleList,
                    nextPaginationAnchor = wrapper.data.nextAnchor ?: ""
                )
            }

    private fun ArticleDAO.toPost(baseUrl: String): Article {
        return Article(id, title, "", totalComments, baseUrl + endpoint)
    }
}