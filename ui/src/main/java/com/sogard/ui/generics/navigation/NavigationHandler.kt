package com.sogard.ui.generics.navigation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.sogard.ui.features.comments.CommentsActivity
import com.sogard.ui.features.toparticles.ArticleListingActivity
import com.sogard.ui.generics.navigation.NavigationKeys.ARTICLE_ID

/** Keys used to pass data between navigation elements*/
object NavigationKeys {
    const val ARTICLE_ID = "ARTICLE_ID"
}

/** Actions used for describing the navigation between application screens.*/
sealed class NavigationAction {
    /** Generic Action describing the navigation to an Activity destination. */
    abstract class ActivityAction(val keepCurrentInBackStack: Boolean) : NavigationAction()

    /** Generic Action describing the navigation to an Activity destination. */
    class ArticleListAction(keepCurrentInBackStack: Boolean) :
        ActivityAction(keepCurrentInBackStack)

    /** Action describing the navigation to the Comments Activity. */
    class CommentsAction(val postId: String, keepCurrentInBackStack: Boolean) :
        ActivityAction(keepCurrentInBackStack)

    /** Action describing the navigation to an URL destination. */
    class UrlAction(val url: String) : NavigationAction()
}


/**
 * Static Class handling the app navigation.

TODO 1: The NavigationAction and the NavigationHandler should be separated in different files once they grow in size.
TODO 2: This class is not ideal. The Android Navigation Graph should be explored for scalability purposes.
 **/
object NavigationHandler {

    /** Start a new activity with with a website URI. */
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