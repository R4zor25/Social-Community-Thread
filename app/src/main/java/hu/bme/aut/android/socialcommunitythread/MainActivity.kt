package hu.bme.aut.android.socialcommunitythread

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import android.window.SplashScreen
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.socialcommunitythread.navigation.ThreadScreenNav
import hu.bme.aut.android.socialcommunitythread.ui.auth.login.LoginScreen
import hu.bme.aut.android.socialcommunitythread.ui.auth.registration.RegistrationScreen
import hu.bme.aut.android.socialcommunitythread.ui.camera.CameraScreen
import hu.bme.aut.android.socialcommunitythread.ui.chatlist.ChatListScreen
import hu.bme.aut.android.socialcommunitythread.ui.createpost.CreatePostScreen
import hu.bme.aut.android.socialcommunitythread.ui.createthread.CreateThreadScreen
import hu.bme.aut.android.socialcommunitythread.ui.friendlist.FriendListScreen
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadScreen
import hu.bme.aut.android.socialcommunitythread.ui.savedposts.SavedPostsScreen
import hu.bme.aut.android.socialcommunitythread.ui.search.SearchScreen
import hu.bme.aut.android.socialcommunitythread.ui.splash.SplashScreen
import hu.bme.aut.android.socialcommunitythread.ui.theme.AppJetpackComposeTheme
import hu.bme.aut.android.socialcommunitythread.ui.thread.TopicThreadScreen
import hu.bme.aut.android.socialcommunitythread.ui.threaddetails.ThreadDetailsScreen
import hu.bme.aut.android.socialcommunitythread.ui.userprofile.UserProfileScreen
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
                            LoginScreen(navController)
                        }
                        composable(
                            route = ThreadScreenNav.RegistrationScreenNav.route
                        ) {
                            RegistrationScreen(navController)
                        }
                        composable(
                            route = ThreadScreenNav.MainThreadScreenNav.route
                        ) {
                            MainThreadScreen(navController)
                        }
                        composable(
                            route = ThreadScreenNav.SearchScreenNav.route
                        ) {
                            SearchScreen(navController)
                        }
                        composable(
                            route = ThreadScreenNav.ChatListScreenNav.route
                        ) {
                            ChatListScreen(navController)
                        }
                        composable(
                            route = ThreadScreenNav.ChatDetailsScreenNav.route
                        ) {
                            ChatListScreen(navController)
                        }
                        composable(
                            route = ThreadScreenNav.ThreadDetailsNav.route
                        ) { backStackEntry ->
                            val postId = backStackEntry.arguments?.get("postId")
                            ThreadDetailsScreen(navController = navController, postId = postId.toString().toInt())
                        }
                        composable(
                            route = ThreadScreenNav.CreateThreadNav.route
                        ) {
                            CreateThreadScreen(navController = navController)
                        }
                        composable(
                            route = ThreadScreenNav.UserProfileNav.route
                        ) {
                            UserProfileScreen(navController = navController)
                        }
                        composable(
                            route = ThreadScreenNav.FriendListNav.route
                        ) {
                            FriendListScreen(navController = navController)
                        }
                        composable(
                            route = ThreadScreenNav.SavedPostsNav.route
                        ) {
                            SavedPostsScreen(navController = navController)
                        }
                        composable(
                            route = ThreadScreenNav.TopicThreadNav.route
                        ) { backStackEntry ->
                            val threadId = backStackEntry.arguments?.get("threadId")
                            TopicThreadScreen(navController = navController, threadId.toString().toInt())
                        }
                        composable(
                            route = ThreadScreenNav.CameraNav.route
                        ) {
                            CameraScreen(navController = navController)
                        }
                        composable(
                            route = ThreadScreenNav.CreatePostNav.route
                        ) {
                            CreatePostScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
