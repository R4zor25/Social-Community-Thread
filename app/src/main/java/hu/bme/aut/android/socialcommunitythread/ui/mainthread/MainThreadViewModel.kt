package hu.bme.aut.android.socialcommunitythread.ui.mainthread

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.VoteType
import hu.bme.aut.android.socialcommunitythread.domain.interactors.ThreadInteractor
import hu.bme.aut.android.socialcommunitythread.util.DefaultPaginator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class MainThreadViewModel @Inject constructor(
    private val threadInteractor: ThreadInteractor,
) : ViewModel() {
    private val coroutineScope = MainScope()


    private val viewState: MutableStateFlow<MainThreadViewState> = MutableStateFlow(MainThreadViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<MainThreadOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    private val paginator = DefaultPaginator(
        initialKey = viewState.value.page,
        onLoadUpdated = { loadUpdated ->
            viewState.update { it.copy(isLoading = loadUpdated) }
        },
        onRequest = { nextPage ->
            suspendCoroutine<Result<List<PersonalThreadPost>>> { continuation ->

            }
        },
        getNextKey = {
            viewState.value.page + 1
        },
        onError = { error ->
            viewState.update { it.copy(error = error?.localizedMessage.toString()) }
        },
        onSuccess = { items, newKey ->
            viewState.update {
                it.copy(
                    items = (it.items + items).toMutableStateList(),
                    page = newKey,
                    endReached = items.isEmpty()
                )
            }
        }
    )

    init {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.getRecommendedPosts { errorMessage, result ->
                if (errorMessage.isBlank() && result != null) {
                    viewState.update { it.copy(items = result.toMutableStateList()) }
                } else {
                    coroutineScope.launch(Dispatchers.IO) {
                        _oneShotEvents.send(MainThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }


    fun savePost(post: PersonalThreadPost) {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.savePost(post.topicThread.topicThreadId, post.postId!!) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        post.isSavedByUser = true
                        viewState.update { it.copy(items = mutableStateListOf<PersonalThreadPost>().apply { addAll(it.items) }) }
                    } else {
                        _oneShotEvents.send(MainThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun unsavePost(post: PersonalThreadPost) {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.unsavePost(post.topicThread.topicThreadId, post.postId!!) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        post.isSavedByUser = false
                        viewState.update { it.copy(items = mutableStateListOf<PersonalThreadPost>().apply { addAll(it.items) }) }
                    } else {
                        _oneShotEvents.send(MainThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun upvotePost(post: PersonalThreadPost) {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.upvoteThreadPost(post.topicThread.topicThreadId, post.postId!!) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        when (post.userVoteType) {
                            VoteType.CLEAR -> {
                                post.voteNumber += 1
                                post.userVoteType = VoteType.UPVOTED
                            }

                            VoteType.UPVOTED -> {
                                post.voteNumber -= 1
                                post.userVoteType = VoteType.CLEAR
                            }

                            VoteType.DOWNVOTED -> {
                                post.voteNumber += 2
                                post.userVoteType = VoteType.UPVOTED
                            }
                        }
                        viewState.update {
                            it.copy(items = mutableStateListOf<PersonalThreadPost>().apply { addAll(it.items) })
                        }
                    } else {
                        _oneShotEvents.send(MainThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun downVotePost(post: PersonalThreadPost) {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.downvoteThreadPost(post.topicThread.topicThreadId, post.postId!!) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        when (post.userVoteType) {
                            VoteType.CLEAR -> {
                                post.voteNumber -= 1
                                post.userVoteType = VoteType.DOWNVOTED
                            }

                            VoteType.UPVOTED -> {
                                post.voteNumber -= 2
                                post.userVoteType = VoteType.DOWNVOTED
                            }

                            VoteType.DOWNVOTED -> {
                                post.voteNumber += 1
                                post.userVoteType = VoteType.CLEAR
                            }
                        }
                        viewState.update {
                            it.copy(items = mutableStateListOf<PersonalThreadPost>().apply { addAll(it.items) })
                        }
                    } else {
                        _oneShotEvents.send(MainThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }
}