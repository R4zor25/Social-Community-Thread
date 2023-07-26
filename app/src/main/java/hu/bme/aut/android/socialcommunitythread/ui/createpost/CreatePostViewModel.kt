package hu.bme.aut.android.socialcommunitythread.ui.createpost

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.repository.Repository
import hu.bme.aut.android.socialcommunitythread.ui.createthread.CreateThreadOneShotEvent
import hu.bme.aut.android.socialcommunitythread.ui.createthread.CreateThreadUiAction
import hu.bme.aut.android.socialcommunitythread.ui.createthread.CreateThreadViewState
import hu.bme.aut.android.socialcommunitythread.ui.search.SearchViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor() : ViewModel() {
    private val coroutineScope = MainScope()

    //private val _viewState: MutableStateFlow<CreateThreadViewState> = MutableStateFlow(CreateThreadViewState())
    //val viewState = _viewState.asStateFlow()

    var viewState by mutableStateOf(CreatePostViewState())

    private val _oneShotEvents = Channel<CreatePostOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    private val repository = Repository

    fun onAction(createPostUiAction: CreatePostUiAction) {
        when (createPostUiAction) {
            is CreatePostUiAction.CreateThread -> TODO()
        }
    }
}
