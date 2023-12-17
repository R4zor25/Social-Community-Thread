package hu.bme.aut.android.socialcommunitythread.ui.uicomponent

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
fun AddChatUserDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (AppUser) -> Unit,
    userList: List<AppUser>,
) {
    var username by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var userListExpanded by rememberSaveable { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                Text(
                    text = "Add User",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp),
                    color = defaultTextColor()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Color Selection
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Column {
                        ExposedDropdownMenuBox(
                            expanded = userListExpanded,
                            onExpandedChange = {
                                userListExpanded = !userListExpanded
                            }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                readOnly = true,
                                value = username,
                                onValueChange = { username = it },
                                label = { Text("Username") },
                                leadingIcon = { Icon(Icons.Filled.Person, "") },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colors.secondary,
                                    unfocusedBorderColor = Color.Gray,
                                    focusedLabelColor = Color.Black,
                                    textColor = defaultTextColor()
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = userListExpanded,
                                onDismissRequest = {
                                    userListExpanded = false
                                },
                                modifier = Modifier.height(150.dp).background(MaterialTheme.colors.primary)
                            ) {
                                userList.map { it.userName }.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        onClick = {
                                            username = selectionOption
                                            userListExpanded = false
                                        },
                                        modifier = Modifier.background(MaterialTheme.colors.primary)
                                    ) {
                                        Text(text = selectionOption, color = defaultTextColor())
                                    }
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            TextButton(onClick = onNegativeClick) {
                                Text(text = "Cancel", color = defaultTextColor())
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            TextButton(onClick = {
                                if (username.isNullOrBlank()) {
                                    Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_LONG).show()
                                } else {
                                    onPositiveClick(userList.first { it.userName == username })
                                }
                            }) {
                                Text(text = "Ok", color = defaultTextColor())
                            }
                        }
                    }
                }
            }
        }
    }
}