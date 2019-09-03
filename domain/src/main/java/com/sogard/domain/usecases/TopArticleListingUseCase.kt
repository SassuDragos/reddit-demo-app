package com.sogard.domain.usecases

import com.sogard.domain.models.article.ArticlePaginationParameters
import com.sogard.domain.models.article.Article
import com.sogard.domain.repositories.ArticleRepository
import extensions.applyIoScheduler
import io.reactivex.Single

class TopArticleListingUseCase(private val articleRepository: ArticleRepository) {

    private var nextAnchor: String? = null
    private val MAX_RESPONSE_SIZE = 15

    fun getArticles(totalLoadedItems: Int): Single<List<Article>> {
        if (totalLoadedItems == 0) nextAnchor = null

        val paginationParameters =
            ArticlePaginationParameters(
                nextAnchor,
                totalLoadedItems,
                MAX_RESPONSE_SIZE
            )
        return articleRepository.getTopArticles(paginationParameters)
            .doOnSuccess { nextAnchor = it.nextPaginationAnchor }
            .map { paginatedResponse -> paginatedResponse.data }
            .applyIoScheduler()
    }
}