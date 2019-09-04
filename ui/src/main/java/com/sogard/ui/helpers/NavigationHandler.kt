package com.sogard.ui.helpers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.sogard.ui.helpers.NavigationKeys.ARTICLE_ID
import com.sogard.ui.features.comments.CommentsActivity

object NavigationKeys {
    const val ARTICLE_ID = "ARTICLE_ID"
}

sealed class NavigationDestination {
    class UrlDestination(val url: String) : NavigationDestination()
    class CommentsDestination(val postId: String) : NavigationDestination()
}

object NavigationHandler {

    private fun openUrl(currentActivity: Activity, url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            flags += Intent.FLAG_ACTIVITY_NEW_TASK
        }

        startActivity(currentActivity, intent, null)
    }

    fun goTo(destination: NavigationDestination, currentActivity: Activity) {
        when (destination) {
            is NavigationDestination.UrlDestination -> openUrl(
                currentActivity,
                destination.url
            )
            is NavigationDestination.CommentsDestination -> {
                val intent = Intent(currentActivity, CommentsActivity::class.java)
                intent.apply {
                    putExtra(ARTICLE_ID, destination.postId)
                }
                startActivity(currentActivity, intent, null)
            }
        }
    }
}