package hu.bme.aut.android.socialcommunitythread.ui.thread

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.VoteType
import hu.bme.aut.android.socialcommunitythread.domain.interactors.ThreadInteractor
import hu.bme.aut.android.socialcommunitythread.util.DefaultPaginator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class TopicThreadViewModel @Inject constructor(
    private val threadInteractor: ThreadInteractor,
) : ViewModel() {
    private val coroutineScope = MainScope()

    //private val _viewStateTopic: MutableStateFlow<TopicThreadViewState> = MutableStateFlow(TopicThreadViewState())
    // val viewState = _viewStateTopic.asStateFlow()

    private val viewState: MutableStateFlow<TopicThreadViewState> = MutableStateFlow(TopicThreadViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<TopicThreadOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    private var threadId = 0L

    init {
        coroutineScope.launch(Dispatchers.IO) {
            _oneShotEvents.send(TopicThreadOneShotEvent.AcquireId)
        }
    }


    private val paginator = DefaultPaginator(
        initialKey = uiState.value.page,
        onLoadUpdated = { loading ->
            viewState.update { it.copy(isLoading = loading) }
        },
        onRequest = { nextPage ->
            suspendCoroutine<Result<List<PersonalThreadPost>>> { continuation ->

            }
        },
        getNextKey = {
            viewState.value.page + 1
        },
        onError = {
            viewState.update { viewState -> viewState.copy(error = it?.localizedMessage) }
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

    fun savePost(post: PersonalThreadPost) {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.savePost(post.topicThread.topicThreadId, post.postId!!) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        /*
                        viewState.update {
                            it.copy(items = mutableStateListOf<PersonalThreadPost>().apply { addAll(it.items) })
                        }
                        */
                        post.isSavedByUser = true //TODO elég-ea iveupdatehez, szerintem nem
                    } else {
                        _oneShotEvents.send(TopicThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun unsavePost(post: PersonalThreadPost) {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.savePost(post.topicThread.topicThreadId, post.postId!!) { errorMessage -> //TODO make it unsave
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        post.isSavedByUser = false //TODO elég-ea iveupdatehez, szerintem nem
                    } else {
                        _oneShotEvents.send(TopicThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun followThread() {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.followThread(threadId) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        viewState.update { it.copy(personalTopicThread = it.personalTopicThread.copy(isFollowedByUser = true)) }
                    } else {
                        _oneShotEvents.send(TopicThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun unfollowThread() {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.unfollowThread(threadId) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        viewState.update { it.copy(personalTopicThread = it.personalTopicThread.copy(isFollowedByUser = false)) }
                    } else {
                        _oneShotEvents.send(TopicThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun upvotePost(post: PersonalThreadPost) {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.upvoteThreadPost(threadId, post.postId!!) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        when (post.userVoteType) {
                            VoteType.CLEAR -> post.userVoteType = VoteType.UPVOTED
                            VoteType.UPVOTED -> post.userVoteType = VoteType.CLEAR
                            VoteType.DOWNVOTED -> post.userVoteType = VoteType.UPVOTED
                        }
                    } else {
                        _oneShotEvents.send(TopicThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun downVotePost(post: PersonalThreadPost) {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.downvoteThreadPost(threadId, post.postId!!) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        when (post.userVoteType) {
                            VoteType.CLEAR -> post.userVoteType = VoteType.DOWNVOTED
                            VoteType.UPVOTED -> post.userVoteType = VoteType.DOWNVOTED
                            VoteType.DOWNVOTED -> post.userVoteType = VoteType.CLEAR
                        }
                    } else {
                        _oneShotEvents.send(TopicThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun onInit() {
        coroutineScope.launch(Dispatchers.IO) {
             async {
                threadInteractor.getTopicThreadPosts(threadId = threadId) { errorMessage, result ->
                    if (errorMessage.isBlank() && result != null) {
                        viewState.update { it.copy(items = result.toMutableStateList()) }
                    } else {
                        coroutineScope.launch(Dispatchers.IO) {
                            _oneShotEvents.send(TopicThreadOneShotEvent.ShowToastMessage(errorMessage))
                        }
                    }
                }
            }.await()
            async {
                threadInteractor.getTopicThreadDetails(threadId = threadId) { errorMessage, result ->
                    coroutineScope.launch(Dispatchers.IO) {
                        if (errorMessage.isBlank() && result != null) {
                            viewState.update { it.copy(personalTopicThread = result) }
                        } else {
                            _oneShotEvents.send(TopicThreadOneShotEvent.ShowToastMessage(errorMessage))
                        }
                    }
                }
            }.await()
        }
    }

    fun passThreadId(threadId: Long) {
        this.threadId = threadId
    }
}