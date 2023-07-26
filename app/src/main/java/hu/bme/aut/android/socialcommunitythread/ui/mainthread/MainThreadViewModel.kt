package hu.bme.aut.android.socialcommunitythread.ui.mainthread

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.socialcommunitythread.domain.repository.Repository
import hu.bme.aut.android.socialcommunitythread.util.DefaultPaginator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainThreadViewModel @Inject constructor() : ViewModel() {
    private val coroutineScope = MainScope()

    // private val _viewState: MutableStateFlow<MainThreadViewState> = MutableStateFlow(MainThreadViewState())
    // val viewState = _viewState.asStateFlow()
    private val repository = Repository

    var viewState by mutableStateOf(MainThreadViewState())

    private val _oneShotEvents = Channel<MainThreadOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    private val paginator = DefaultPaginator(
        initialKey = viewState.page,
        onLoadUpdated = {
            viewState = viewState.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            repository.getThreadItemsWithPaging(nextPage, 20)
        },
        getNextKey = {
            viewState.page + 1
        },
        onError = {
            viewState = viewState.copy(error = it?.localizedMessage)
        },
        onSuccess = {items, newKey ->
            viewState = viewState.copy(
                items = viewState.items + items,
                page = newKey,
                endReached = items.isEmpty()
            )
        }
    )

    init {
        loadNextItems()
    }

    fun loadNextItems(){
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    fun onAction(mainThreadUiAction: MainThreadUiAction) {
        when (mainThreadUiAction) {
            is MainThreadUiAction.SavePostUiAction -> {
                coroutineScope.launch(Dispatchers.IO) {
                    repository.savePost(mainThreadUiAction.post)
                }
            }
            is MainThreadUiAction.UnsavePostUiAction -> {
                coroutineScope.launch(Dispatchers.IO) {
                    repository.unsavePost(mainThreadUiAction.post)
                }
            }
        }
    }
}