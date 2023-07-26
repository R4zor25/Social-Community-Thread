package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.domain.repository.Repository
import hu.bme.aut.android.socialcommunitythread.navigation.NavigationDrawerItems
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.MiniThreadRowItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(navController: NavController, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope) {
    var list: List<TopicThread> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(key1 = "key") {
        coroutineScope.launch {
            list = Repository.getAllThread()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // List of navigation items
        item {
            NavigationDrawerItems.NavigationDrawerItems.forEach { item ->
                NavigationDrawerRowItem(item, false) {
                    coroutineScope.launch { scaffoldState.drawerState.close() }
                    navController.navigate(item.route) {
                        if (item.route == "login_screen") {
                            navController.popBackStack()
                            popUpTo(navController.currentDestination?.route.toString()) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
        item {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(Beige), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Followed Threads", fontSize = 20.sp, color = Color.Black, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp))
            }
        }
        items(list.size) { i ->
            if (list.isNotEmpty()) {
                MiniThreadRowItem(topicThread = list[i], navController = navController)
                Divider()
            }
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