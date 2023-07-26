package hu.bme.aut.android.socialcommunitythread.ui.friendlist

import androidx.compose.foundation.background
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadViewModel
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige

@Composable
fun FriendListScreen(navController: NavController){
    val friendListViewModel = hiltViewModel<FriendListViewModel>()
    Scaffold(
        modifier = Modifier.background(Beige)
    ) {

    }
}