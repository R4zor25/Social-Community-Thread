package hu.bme.aut.android.socialcommunitythread.ui.uicomponent

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import hu.bme.aut.android.socialcommunitythread.navigation.ThreadScreenNav
import hu.bme.aut.android.socialcommunitythread.ui.theme.PrimaryLight
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultIconColor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor

@Composable
fun BottomNavigationBar(navController: NavController) {
  
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.secondary
    ) {

        // observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // observe current route to change the icon
          // color,label color when navigated
        val currentRoute = navBackStackEntry?.destination?.route
  
        // Bottom nav items we declared
        BottomNavItems.BottomNavItems.forEach { navItem ->
  
            // Place the bottom nav items
            BottomNavigationItem(
                    
                // it currentRoute is equal then its selected route
                selected = currentRoute == navItem.route,
  
                // navigate on click
                onClick = {
                    navController.navigate(navItem.route){
                        navController.popBackStack()
                        popUpTo("login_screen"){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                
                // Icon of navItem
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = navItem.label, tint = defaultIconColor())
                },
                
                // label
                label = {
                    Text(text = navItem.label, color = defaultTextColor())
                },
                alwaysShowLabel = false
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
)

object BottomNavItems {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = ThreadScreenNav.MainThreadScreenNav.route
        ),
        BottomNavItem(
            label = "Search",
            icon = Icons.Filled.Search,
            route = ThreadScreenNav.SearchScreenNav.route
        ),
        BottomNavItem(
            label = "Chat",
            icon = Icons.Filled.Chat,
            route = ThreadScreenNav.ChatListScreenNav.route
        ),
    )
}