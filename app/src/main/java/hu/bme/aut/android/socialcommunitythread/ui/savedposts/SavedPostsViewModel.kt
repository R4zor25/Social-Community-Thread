package hu.bme.aut.android.socialcommunitythread.ui.savedposts

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.repository.Repository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadUiAction
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class SavedPostsViewModel @Inject constructor() : ViewModel() {
    private val coroutineScope = MainScope()

   // private val _viewState: MutableStateFlow<SavedPostsViewState> = MutableStateFlow(SavedPostsViewState())
    //val viewState = _viewState.asStateFlow()
    var viewState by mutableStateOf(SavedPostsViewState())

    private val _oneShotEvents = Channel<SavedPostsOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    private val repository = Repository

    init{
        viewModelScope.launch {
            val list = repository.getSavedPosts()
            viewState = viewState.copy(isLoading = false, savedPostList = list)
        }
    }

    fun onAction(savedPostsUiAction: SavedPostsUiAction){
        when (savedPostsUiAction){
            is SavedPostsUiAction.SavePostUiAction -> {
                coroutineScope.launch(Dispatchers.IO) {
                    repository.savePost(savedPostsUiAction.post)
                }
            }
            is SavedPostsUiAction.UnsavePostUiAction -> {
                coroutineScope.launch(Dispatchers.IO) {
                    repository.unsavePost(savedPostsUiAction.post)
                }
            }
        }
    }
}