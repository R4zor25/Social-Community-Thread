package hu.bme.aut.android.socialcommunitythread.ui.thread

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.repository.Repository
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadUiAction
import hu.bme.aut.android.socialcommunitythread.ui.threaddetails.ThreadDetailsOneShotEvent
import hu.bme.aut.android.socialcommunitythread.util.DefaultPaginator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicThreadViewModel @Inject constructor() : ViewModel() {
    private val coroutineScope = MainScope()
    private val repository = Repository

    //private val _viewStateTopic: MutableStateFlow<TopicThreadViewState> = MutableStateFlow(TopicThreadViewState())
   // val viewState = _viewStateTopic.asStateFlow()
    var viewState by mutableStateOf(TopicThreadViewState())

    private val _oneShotEvents = Channel<TopicThreadOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    var threadId = 0

    init{
        coroutineScope.launch {
            _oneShotEvents.send(TopicThreadOneShotEvent.AcquireId)
        }
    }


    private val paginator = DefaultPaginator(
        initialKey = viewState.page,
        onLoadUpdated = {
            viewState = viewState.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            repository.getTopicThreadPosts(nextPage, 1, threadId)// TOOO Change this
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

    fun loadNextItems(){
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }


    fun onAction(topicThreadUiAction: TopicThreadUiAction){
        when (topicThreadUiAction){
            is TopicThreadUiAction.SavePostUiAction -> {
                coroutineScope.launch(Dispatchers.IO) {
                    repository.savePost(topicThreadUiAction.post)
                }
            }
            is TopicThreadUiAction.UnsavePostUiAction -> {
                coroutineScope.launch(Dispatchers.IO) {
                    repository.unsavePost(topicThreadUiAction.post)
                }
            }
            is TopicThreadUiAction.FollowThread -> TODO()
            is TopicThreadUiAction.OnInit -> {
                coroutineScope.launch(Dispatchers.IO) {
                    viewState = viewState.copy(isLoading = true)
                    val threadItem = repository.getThreadWithId(threadId)
                    _oneShotEvents.send(TopicThreadOneShotEvent.ThreadItemAcquired(topicThread = threadItem))
                    loadNextItems()
                    viewState = viewState.copy(isLoading = false)
                }
            }
            is TopicThreadUiAction.UnfollowThread -> TODO()
        }
    }
}