package com.sogard.domain.repositories

import com.sogard.domain.models.article.PaginatedResponse
import com.sogard.domain.models.article.ArticlePaginationParameters
import com.sogard.domain.models.article.Article
import io.reactivex.Single

interface PostRepository {
    fun getTopArticles(pagParam: ArticlePaginationParameters): Single<PaginatedResponse<List<Article>>>
}