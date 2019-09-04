package com.sogard.ui.helpers

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.sogard.ui.features.comments.CommentsActivity
import com.sogard.ui.features.toparticles.ArticleListingActivity
import com.sogard.ui.helpers.NavigationKeys.ARTICLE_ID

object NavigationKeys {
    const val ARTICLE_ID = "ARTICLE_ID"
}

sealed class NavigationAction {

    class UrlAction(val url: String) : NavigationAction()

    abstract class ActivityAction(val keepCurrentInBackStack: Boolean): NavigationAction()
    class ArticleListAction(keepCurrentInBackStack: Boolean) : ActivityAction(keepCurrentInBackStack)
    class CommentsAction(val postId: String, keepCurrentInBackStack: Boolean) : ActivityAction(keepCurrentInBackStack)
}

//TODO: This class is not ideal. The Android Navigation Graph should be explored for scalability purposes.

object NavigationHandler {

    private fun openUrl(currentActivity: Activity, url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            flags += Intent.FLAG_ACTIVITY_NEW_TASK
        }

        startActivity(currentActivity, intent, null)
    }

    fun goTo(action: NavigationAction, currentActivity: Activity) {
        when (action) {
            is NavigationAction.UrlAction -> openUrl(
                currentActivity,
                action.url
            )
            is NavigationAction.ActivityAction -> {
                when (action) {
                    is NavigationAction.CommentsAction -> {
                        val intent = Intent(currentActivity, CommentsActivity::class.java)
                        intent.apply {
                            putExtra(ARTICLE_ID, action.postId)
                        }
                        startActivity(currentActivity, intent, null)
                    }
                    is NavigationAction.ArticleListAction -> {
                        val intent = Intent(currentActivity, ArticleListingActivity::class.java)
                        startActivity(currentActivity, intent, null)
                    }
                }
                if (!action.keepCurrentInBackStack) {
                    currentActivity.finish()
                }
            }
        }
    }
}