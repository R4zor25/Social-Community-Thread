package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.navigation.NavigationDrawerItems
import hu.bme.aut.android.socialcommunitythread.ui.theme.Beige
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.BottomNavItem

@Composable
fun NavigationDrawerRowItem(item: BottomNavItem, selected: Boolean, onItemClick: (BottomNavItem) -> Unit) {
    val background = if (selected) Beige else Color.Transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(80.dp)
            .background(Beige)
    ) {
        Image(
            imageVector = item.icon,
            contentDescription = item.label,
            colorFilter = ColorFilter.tint(Color.Black),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(60.dp)
                .width(60.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.label,
            fontSize = 20.sp,
            color = Color.Black
        )
    }
}

@Preview(showBackground = false)
@Composable
fun NavigationDrawerRowItemPreview() {
    NavigationDrawerRowItem(item = NavigationDrawerItems.NavigationDrawerItems[0], selected = false, onItemClick = {})
}