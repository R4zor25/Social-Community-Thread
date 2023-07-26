package hu.bme.aut.android.socialcommunitythread.ui.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.bme.aut.android.socialcommunitythread.R
import hu.bme.aut.android.socialcommunitythread.domain.model.Post
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.BottomNavigationBar
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.MiniThreadRowItem
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.autocomplete.*
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.navigationdrawer.NavigationDrawer
import hu.bme.aut.android.socialcommunitythread.ui.uicomponent.topbar.TopBar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SearchScreen(navController: NavController) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val threadNameList by remember { mutableStateOf(mutableListOf<String>()) }
    var filteredThreadList by remember { mutableStateOf(listOf<TopicThread>()) }
    val listState = rememberLazyListState()
    var autoCompleteEntities by rememberSaveable {
        mutableStateOf(threadNameList.asAutoCompleteEntities(
            filter = { item, query ->
                item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        ))
    }
    var editTextValue by rememberSaveable { mutableStateOf("") }

    if (threadNameList.isEmpty() && filteredThreadList.isEmpty() && viewModel.viewState.items.isNotEmpty()) {
        threadNameList.clear()

        filteredThreadList = viewModel.viewState.items.filter {
            it.name.lowercase(Locale.getDefault()).contains(editTextValue.lowercase())
        }
        for (thread in viewModel.viewState.items)
            threadNameList.add(thread.name)
        autoCompleteEntities = threadNameList.asAutoCompleteEntities(
            filter = { item, query ->
                item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
            }
        )
    }

    LaunchedEffect("key") {
        viewModel.oneShotEvent
            .onEach {
                when (it) {
                    SearchOneShotEvent.initAutoComplete -> {
                        filteredThreadList = viewModel.viewState.items
                        threadNameList.clear()
                        for(thread in filteredThreadList)
                            threadNameList.add(thread.name)
                        autoCompleteEntities = threadNameList.asAutoCompleteEntities(
                            filter = { item, query ->
                                item.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
                            }
                        )
                    }
                    is SearchOneShotEvent.ShowToastMessage -> TODO()
                }
            }.collect()
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(stringResource(R.string.search), leftIconImage = null, rightIconImage = {
                Image(
                    painter = painterResource(R.drawable.capybara),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            }, scope, scaffoldState, {}, {})
        },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            NavigationDrawer(navController = navController, scaffoldState = scaffoldState, scope)
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
    ) {
        if (!viewModel.viewState.isLoading) {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(bottom = 60.dp)) {
                item {
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom) {
                        AutoCompleteBox(
                            items = autoCompleteEntities,
                            itemContent = { item ->
                                ValueAutoCompleteString(item.value)
                            }
                        ) {
                            val view = LocalView.current

                            onItemSelected { item ->
                                editTextValue = item.value
                                filter(editTextValue)
                                filteredThreadList = viewModel.viewState.items.filter {
                                    it.name.lowercase(Locale.getDefault()).contains(editTextValue.lowercase())
                                }
                                view.clearFocus()
                            }

                            TextSearchBar(
                                modifier = Modifier
                                    .testTag(AutoCompleteSearchBarTag)
                                    .padding(start = 12.dp),
                                value = editTextValue,
                                label = "Search",
                                onDoneActionClick = {
                                    view.clearFocus()
                                },
                                onClearClick = {
                                    editTextValue = ""
                                    filter(editTextValue)
                                    filteredThreadList = viewModel.viewState.items.filter {
                                        it.name.lowercase(Locale.getDefault()).contains(editTextValue.lowercase())
                                    }
                                    view.clearFocus()
                                },
                                onFocusChanged = { focusState ->
                                    isSearching = focusState.isFocused
                                    filter(editTextValue)
                                },
                                onValueChanged = { query ->
                                    editTextValue = query
                                    filter(editTextValue)
                                    filteredThreadList = viewModel.viewState.items.filter {
                                        it.name.lowercase(Locale.getDefault()).contains(editTextValue.lowercase())
                                    }
                                }
                            )
                        }
                    }
                }
                items(filteredThreadList.size) { i ->
                    val item = filteredThreadList[i]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        MiniThreadRowItem(item, navController = navController)
                        Divider()
                    }
                }
            }
        } else {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 60.dp),
                    horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator()
                }
        }
    }
}