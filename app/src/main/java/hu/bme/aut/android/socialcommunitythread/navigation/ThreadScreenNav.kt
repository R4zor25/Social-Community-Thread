package hu.bme.aut.android.socialcommunitythread.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.BottomNavItem

sealed class ThreadScreenNav(val route: String) {
    object SplashScreenNav : ThreadScreenNav("splash")
    object LoginScreenNav : ThreadScreenNav("login_")
    object RegistrationScreenNav : ThreadScreenNav("registration")
    object MainThreadScreenNav : ThreadScreenNav("main_thread")
    object SearchScreenNav : ThreadScreenNav("search")
    object ChatListScreenNav : ThreadScreenNav("chat_list")
    object ChatDetailsScreenNav : ThreadScreenNav("chat_details")
    object ThreadDetailsNav : ThreadScreenNav("thread_details/{postId}")
    object CreateThreadNav : ThreadScreenNav("create_thread")
    object UserProfileNav : ThreadScreenNav("user_profile")
    object FriendListNav: ThreadScreenNav("friend_list")
    object SavedPostsNav: ThreadScreenNav("saved_posts")
    object TopicThreadNav: ThreadScreenNav("topic_thread/{threadId}")
    object CameraNav: ThreadScreenNav("camera")
    object CreatePostNav: ThreadScreenNav("create_post")
}

object NavigationDrawerItems {

    val NavigationDrawerItems = listOf(
        BottomNavItem(
            label = "Profile",
            icon = Icons.Filled.Person,
            route = ThreadScreenNav.UserProfileNav.route
        ),
        BottomNavItem(
            label = "Create TopicThread",
            icon = Icons.Filled.Create,
            route = ThreadScreenNav.CreateThreadNav.route
        ),
        BottomNavItem(
            label = "Saved Posts",
            icon = Icons.Filled.WatchLater,
            route = ThreadScreenNav.SavedPostsNav.route
        ),
        BottomNavItem(
            label = "Friends",
            icon = Icons.Filled.People,
            route = ThreadScreenNav.FriendListNav.route
        ),
        BottomNavItem(
            label = "Log out",
            icon = Icons.Filled.Logout,
            route = ThreadScreenNav.LoginScreenNav.route
        )
    )

}