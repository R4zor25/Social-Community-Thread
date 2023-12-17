package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.navigation.NavigationDrawerItems
import hu.bme.aut.android.socialcommunitythread.ui.theme.secondaryTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.circleimage.CircleImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(navController: NavController, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope) {
    var list: List<TopicThread> by remember { mutableStateOf(emptyList()) }
    val context = LocalContext.current
    val sharedPreference = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    LaunchedEffect(key1 = "key") {
        coroutineScope.launch(Dispatchers.IO) {
            list = listOf()//Repository.getAllThread()
        }
    }

    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)) {
        if(AuthInteractor.currentLoggedInUser != null && AuthInteractor.currentLoggedInUser!!.profileImage != null) {
            CircleImage(imageSize = 72, BitmapFactory.decodeByteArray(AuthInteractor.currentLoggedInUser!!.profileImage, 0, AuthInteractor.currentLoggedInUser!!.profileImage!!.size))
        }
        Spacer(modifier = Modifier.padding(top = 12.dp))
        Text(AuthInteractor.currentLoggedInUser!!.userName, color = secondaryTextColor(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.padding(top = 12.dp))
        Text(AuthInteractor.currentLoggedInUser!!.email, color = secondaryTextColor(), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
    ) {
        // List of navigation items
        item {
            NavigationDrawerItems.NavigationDrawerItems.forEach { item ->
                NavigationDrawerRowItem(item) {
                    coroutineScope.launch(Dispatchers.IO) { scaffoldState.drawerState.close() }
                    navController.navigate(item.route) {
                        if (item.route == "login_") {
                            sharedPreference.edit().remove("access").apply()
                            sharedPreference.edit().remove("refresh").apply()
                            navController.popBackStack()
                            popUpTo(navController.currentDestination?.route.toString()) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
        item{
            Spacer(Modifier.background(MaterialTheme.colors.primary).padding(top = 2.dp))
        }

    }
}

@Preview
@Composable
fun DrawerPreview() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    NavigationDrawer(NavController(LocalContext.current), scaffoldState, scope)
}