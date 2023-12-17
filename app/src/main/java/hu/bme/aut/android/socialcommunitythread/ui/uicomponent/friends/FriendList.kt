package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor

enum class FriendType {
    INCOMING,
    OUTGOING,
    ALREADY_FRIEND,
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendList(
    items: SnapshotStateList<AppUser>,
    friendType : FriendType,
    onAcceptPending: (AppUser) -> Unit,
    onRefusePending: (AppUser) -> Unit,
    onDeleteFriend: (AppUser) -> Unit
) {
    LazyColumn {
        items(items) { item ->
            FriendListRowItem( item, friendType, onAcceptPending, onRefusePending, onDeleteFriend)
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun FriendListRowItem(
    appUser: AppUser,
    friendType: FriendType,
    onAcceptPending: (AppUser) -> Unit,
    onRefusePending: (AppUser) -> Unit,
    onDeleteFriend: (AppUser) -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colors.secondary, shape = RoundedCornerShape(40.dp))
            .fillMaxWidth()
            .height(50.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.People, contentDescription = "", modifier = Modifier.padding(12.dp, 0.dp), tint = Color.Black)
        Text(text = appUser.userName, fontSize = 18.sp, fontWeight = FontWeight(700), color = Color.Black)
        if (friendType == FriendType.INCOMING) {
            Spacer(modifier = Modifier.padding(start = 20.dp))
            Button(modifier = Modifier
                .height(40.dp),
                onClick = { onAcceptPending(appUser) },
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Green
                ),
                content = {
                    Text(text = "Accept", color = defaultTextColor())
                })
            Spacer(modifier = Modifier.padding(start = 20.dp))
            Button(modifier = Modifier
                .height(40.dp),
                shape = RoundedCornerShape(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red
                ),
                onClick = { onRefusePending(appUser) }, content = {
                    Text(text = "Decline", color = defaultTextColor())
                })
        } else {
            if(friendType == FriendType.ALREADY_FRIEND) {
                Column(horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(end = 12.dp, start = 20.dp)) {
                }
            }
            Column(horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 12.dp)) {
                Button(modifier = Modifier
                    .height(40.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red
                    ),
                    onClick = { onDeleteFriend(appUser) }, content = {
                        Text(text = "Delete", color = Color.Black)
                    })
            }
        }
    }
}
