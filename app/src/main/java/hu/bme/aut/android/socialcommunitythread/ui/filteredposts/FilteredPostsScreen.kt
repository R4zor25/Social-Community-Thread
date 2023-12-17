package hu.bme.aut.android.socialcommunitythread.ui.filteredposts

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.ui.theme.defaultTextColor
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.threadrowitem.ThreadRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FilteredPostsScreen(navController: NavController) {
    val viewModel = hiltViewModel<FilteredPostsViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val viewState = viewModel.uiState.collectAsState().value
    val listState = rememberLazyListState()
    var expanded = rememberSaveable { mutableStateOf(false) }
    val options = listOf<String>("Upvoted posts", "Downvoted Posts", "Saved posts", "Posts by User")
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    is FilteredPostsOneShotEvent.ShowToastMessage -> TODO()
                }
            }.collect()
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(stringResource(R.string.saved_posts), leftIconImage = Icons.Filled.ArrowBack, rightIconImage = {
                if(AuthInteractor.currentLoggedInUser != null
                    && AuthInteractor.currentLoggedInUser!!.profileImage.size != null
                    && AuthInteractor.currentLoggedInUser!!.profileImage.size > 0){
                    Image(
                        bitmap = BitmapFactory.decodeByteArray(AuthInteractor.currentLoggedInUser!!.profileImage, 0, AuthInteractor.currentLoggedInUser!!.profileImage!!.size).asImageBitmap(),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.capybara),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                }
            }, scope, scaffoldState, {
                navController.navigateUp()
            }, {})
        },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
        ) {
            item {
                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.secondary)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    expanded = expanded.value, onExpandedChange = {
                        expanded.value = !expanded.value
                    }) {
                    TextField(
                        readOnly = true,
                        value = selectedOptionText,
                        onValueChange = { },
                        label = { Text("Categories") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded.value
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(textColor = defaultTextColor())
                    )
                    ExposedDropdownMenu(

                        expanded = expanded.value,
                        onDismissRequest = {
                            expanded.value = false
                        },
                        modifier = Modifier.background(MaterialTheme.colors.primary)
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedOptionText = selectionOption
                                    expanded.value = false
                                    when (selectionOption) {
                                        "Upvoted posts" -> {
                                            viewModel.getUpvotedPosts()
                                        }

                                        "Downvoted Posts" -> {
                                            viewModel.getDownvotedPosts()
                                        }

                                        "Saved posts" -> {
                                            viewModel.getSavedPosts()
                                        }

                                        "Posts by User" -> {
                                            viewModel.getUsersPosts()
                                        }
                                    }
                                },
                                modifier = Modifier.background(MaterialTheme.colors.primary),
                            ) {
                                Text(text = selectionOption, color = defaultTextColor())
                            }
                        }
                    }
                }
            }
            items(viewState.filteredPostList.size) { i ->
                val item = viewState.filteredPostList[i]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary)
                        .padding(16.dp)
                ) {
                    ThreadRowItem(item, onItemClick = {
                        navController.navigate("post_details/${item.topicThread.topicThreadId}/${item.postId}")
                    },
                        onSaveLaterClick = {
                            if (it.isSavedByUser)
                                viewModel.unsavePost(it)
                            else
                                viewModel.savePost(it)
                        },
                        onUpVote = {
                            viewModel.upvotePost(it)
                        },
                        onDownVote = {
                            viewModel.downVotePost(it)
                        })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            item {
                if (viewState.isLoading) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 60.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}