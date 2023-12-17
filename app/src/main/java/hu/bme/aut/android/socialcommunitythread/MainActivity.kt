package hu.bme.aut.android.socialcommunitythread

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.socialcommunitythread.navigation.ThreadScreenNav
import hu.bme.aut.android.socialcommunitythread.ui.auth.login.LoginScreen
import hu.bme.aut.android.socialcommunitythread.ui.auth.login.LoginScreenViewModel
import hu.bme.aut.android.socialcommunitythread.ui.auth.registration.RegistrationScreen
import hu.bme.aut.android.socialcommunitythread.ui.auth.registration.RegistrationViewModel
import hu.bme.aut.android.socialcommunitythread.ui.camera.CameraScreen
import hu.bme.aut.android.socialcommunitythread.ui.chatdetails.ChatDetailsScreen
import hu.bme.aut.android.socialcommunitythread.ui.chatdetails.ChatDetailsViewModel
import hu.bme.aut.android.socialcommunitythread.ui.chatlist.ChatListScreen
import hu.bme.aut.android.socialcommunitythread.ui.chatlist.ChatListViewModel
import hu.bme.aut.android.socialcommunitythread.ui.createpost.CreatePostScreen
import hu.bme.aut.android.socialcommunitythread.ui.createpost.CreatePostViewModel
import hu.bme.aut.android.socialcommunitythread.ui.createthread.CreateThreadScreen
import hu.bme.aut.android.socialcommunitythread.ui.createthread.CreateThreadViewModel
import hu.bme.aut.android.socialcommunitythread.ui.friendlist.FriendListScreen
import hu.bme.aut.android.socialcommunitythread.ui.friendlist.FriendListViewModel
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadScreen
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadViewModel
import hu.bme.aut.android.socialcommunitythread.ui.filteredposts.FilteredPostsScreen
import hu.bme.aut.android.socialcommunitythread.ui.filteredposts.FilteredPostsViewModel
import hu.bme.aut.android.socialcommunitythread.ui.search.SearchScreen
import hu.bme.aut.android.socialcommunitythread.ui.search.SearchViewModel
import hu.bme.aut.android.socialcommunitythread.ui.splash.SplashScreen
import hu.bme.aut.android.socialcommunitythread.ui.theme.AppJetpackComposeTheme
import hu.bme.aut.android.socialcommunitythread.ui.thread.TopicThreadScreen
import hu.bme.aut.android.socialcommunitythread.ui.thread.TopicThreadViewModel
import hu.bme.aut.android.socialcommunitythread.ui.postdetails.PostDetailsScreen
import hu.bme.aut.android.socialcommunitythread.ui.postdetails.PostDetailsViewModel
import hu.bme.aut.android.socialcommunitythread.ui.userprofile.UserProfileScreen
import hu.bme.aut.android.socialcommunitythread.ui.userprofile.UserProfileViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalGetImage @ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@InternalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.e("PUSH_TOKEN", "Push Token: $it")
        }
        setContent {
            AppJetpackComposeTheme() {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ThreadScreenNav.SplashScreenNav.route
                    ) {
                        composable(route = ThreadScreenNav.SplashScreenNav.route) {
                            SplashScreen(navController = navController)
                        }
                        composable(
                            route = ThreadScreenNav.LoginScreenNav.route
                        ) {
                            val loginScreenViewModel = hiltViewModel<LoginScreenViewModel>()
                            LoginScreen(navController, loginScreenViewModel)
                        }
                        composable(
                            route = ThreadScreenNav.RegistrationScreenNav.route
                        ) {
                            val registrationViewModel = hiltViewModel<RegistrationViewModel>()
                            RegistrationScreen(navController, registrationViewModel)
                        }
                        composable(
                            route = ThreadScreenNav.MainThreadScreenNav.route
                        ) {
                            val mainThreadViewModel = hiltViewModel<MainThreadViewModel>()
                            MainThreadScreen(navController, mainThreadViewModel)
                        }
                        composable(
                            route = ThreadScreenNav.SearchScreenNav.route
                        ) {
                            val searchScreenViewModel = hiltViewModel<SearchViewModel>()
                            SearchScreen(navController, searchScreenViewModel)
                        }
                        composable(
                            route = ThreadScreenNav.ChatListScreenNav.route
                        ) {
                            val chatListViewModel = hiltViewModel<ChatListViewModel>()
                            ChatListScreen(navController, chatListViewModel)
                        }
                        composable(
                            route = ThreadScreenNav.ChatDetailsScreenNav.route
                        ) {
                            val chatDetailsViewModel = hiltViewModel<ChatDetailsViewModel>()
                            val chatId = it.arguments?.get("chatId").toString()
                            ChatDetailsScreen(navController = navController, chatDetailsViewModel, chatId.toLong())
                        }
                        composable(
                            route = ThreadScreenNav.PostDetailsNav.route
                        ) { backStackEntry ->
                            val postId = backStackEntry.arguments?.get("postId")
                            val threadId = backStackEntry.arguments?.get("threadId")
                            val postDetailsViewModel = hiltViewModel<PostDetailsViewModel>().apply {
                                this.postId = postId.toString().toLong()
                                this.threadId = threadId.toString().toLong()
                            }
                            PostDetailsScreen(navController = navController, postDetailsViewModel)
                        }
                        composable(
                            route = ThreadScreenNav.CreateThreadNav.route
                        ) {
                            val createThreadViewModel = hiltViewModel<CreateThreadViewModel>()
                            CreateThreadScreen(navController = navController)
                        }
                        composable(
                            route = ThreadScreenNav.UserProfileNav.route
                        ) {
                            val userProfileViewModel = hiltViewModel<UserProfileViewModel>()
                            UserProfileScreen(navController = navController, userProfileViewModel)
                        }
                        composable(
                            route = ThreadScreenNav.FriendListNav.route
                        ) {
                            val friendListViewModel = hiltViewModel<FriendListViewModel>()
                            FriendListScreen(navController = navController)
                        }
                        composable(
                            route = ThreadScreenNav.SavedPostsNav.route
                        ) {
                            val filteredPostsViewModel = hiltViewModel<FilteredPostsViewModel>()
                            FilteredPostsScreen(navController = navController)
                        }
                        composable(
                            route = ThreadScreenNav.TopicThreadNav.route
                        ) { backStackEntry ->
                            val topicThreadViewModel = hiltViewModel<TopicThreadViewModel>()
                            val threadId = backStackEntry.arguments?.get("threadId")
                            TopicThreadScreen(navController = navController, threadId.toString().toLong())
                        }
                        composable(
                            route = ThreadScreenNav.CameraNav.route
                        ) {
                            CameraScreen(navController = navController)
                        }
                        composable(
                            route = ThreadScreenNav.CreatePostNav.route
                        ) {
                            val createPostsViewModel = hiltViewModel<CreatePostViewModel>()
                            val threadId = it.arguments?.get("threadId")
                            createPostsViewModel.threadId = threadId.toString().toLong()
                            CreatePostScreen(navController = navController, createPostsViewModel)
                        }
                    }
                }
            }
        }
    }
}
