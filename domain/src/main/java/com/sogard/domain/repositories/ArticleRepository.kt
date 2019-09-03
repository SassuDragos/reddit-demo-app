package com.sogard.domain.repositories

import com.sogard.domain.models.article.PaginatedResponse
import com.sogard.domain.models.article.ArticlePaginationParameters
import com.sogard.domain.models.article.Article
import io.reactivex.Single

interface ArticleRepository {
    fun getTopArticles(pagParam: ArticlePaginationParameters): Single<PaginatedResponse<List<Article>>>
}