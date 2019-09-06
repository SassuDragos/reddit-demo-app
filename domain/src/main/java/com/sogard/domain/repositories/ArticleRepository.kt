package com.sogard.domain.repositories

import com.sogard.domain.models.article.PaginatedArticleData
import com.sogard.domain.models.article.ArticlePaginationParameters
import com.sogard.domain.models.article.Article
import io.reactivex.Single

/**
 * Interface that exposes article manipulation on the data sources.
 * */
interface ArticleRepository {

    /**
     *  [Article fetching]
     *
     *  Method that takes some pagination parameters for the next article list and returns the
     *  corresponding paginated list of articles.
     * */
    fun getTopArticles(pagParam: ArticlePaginationParameters): Single<PaginatedArticleData>
}