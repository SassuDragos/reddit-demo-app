package com.sogard.data.repositories

import com.sogard.data.apis.ArticleApi
import com.sogard.data.models.ArticleDAO
import com.sogard.data.models.getContent
import com.sogard.data.network.AppNetworkConfiguration.BASE_URL_PUBLIC
import com.sogard.domain.models.article.Article
import com.sogard.domain.models.article.ArticlePaginationParameters
import com.sogard.domain.models.article.PaginatedArticleData
import com.sogard.domain.repositories.ArticleRepository
import io.reactivex.Single

/**
 * Implementation of the [ArticleRepository] interface.
 * */
class ArticleRepositoryImpl(private val articleApi: ArticleApi) : ArticleRepository {

    override fun getTopArticles(pagParam: ArticlePaginationParameters): Single<PaginatedArticleData> =
        articleApi.getTopArticles(pagParam.nextAnchor, pagParam.totalLoadedItems, pagParam.maxListSize)
            .map { wrapper ->
                //TODO: create a Mapper class that handles domain <-> ui and domain <-> data mapping
                val articleList = wrapper.getContent().map { it.toArticle(BASE_URL_PUBLIC) }
                PaginatedArticleData(
                    data = articleList,
                    nextPaginationAnchor = wrapper.data.nextAnchor ?: ""
                )
            }

    /**
     * Method that build the article object from an ArticleDAO.
     *
     * Note: It also builds the detailsUrl, to simplify the work flow when a user wants to navigate there.
     * */
    private fun ArticleDAO.toArticle(baseUrl: String): Article {
        return Article(id, title, "", totalComments, baseUrl + endpoint)
    }
}