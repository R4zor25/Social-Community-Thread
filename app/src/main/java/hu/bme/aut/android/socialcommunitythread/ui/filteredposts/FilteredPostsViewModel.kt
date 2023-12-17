package hu.bme.aut.android.socialcommunitythread.ui.filteredposts

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.VoteType
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import hu.bme.aut.android.socialcommunitythread.domain.interactors.ThreadInteractor
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadOneShotEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class FilteredPostsViewModel @Inject constructor(
    private val threadInteractor: ThreadInteractor
) : ViewModel() {
    private val coroutineScope = MainScope()

   // private val _viewState: MutableStateFlow<SavedPostsViewState> = MutableStateFlow(SavedPostsViewState())
    //val viewState = _viewState.asStateFlow()
   private val viewState: MutableStateFlow<FilteredPostsViewState> = MutableStateFlow(FilteredPostsViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<FilteredPostsOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    init{
        getUpvotedPosts()
    }

    fun savePost(post: PersonalThreadPost) {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.savePost(post.topicThread.topicThreadId, post.postId!!) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        post.isSavedByUser = true
                        viewState.update { it.copy(filteredPostList = mutableStateListOf<PersonalThreadPost>().apply { addAll(it.filteredPostList) }) }
                    } else {
                        _oneShotEvents.send(FilteredPostsOneShotEvent.ShowToastMessage(errorMessage))
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
                        viewState.update { it.copy(filteredPostList = mutableStateListOf<PersonalThreadPost>().apply { addAll(it.filteredPostList) }) }
                    } else {
                        _oneShotEvents.send(FilteredPostsOneShotEvent.ShowToastMessage(errorMessage))
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
                            it.copy(filteredPostList = mutableStateListOf<PersonalThreadPost>().apply { addAll(it.filteredPostList) })
                        }
                    } else {
                        _oneShotEvents.send(FilteredPostsOneShotEvent.ShowToastMessage(errorMessage))
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
                            it.copy(filteredPostList = mutableStateListOf<PersonalThreadPost>().apply { addAll(it.filteredPostList) })
                        }
                    } else {
                        _oneShotEvents.send(FilteredPostsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun getSavedPosts(){
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.getSavedPosts() { errorMessage, posts ->
                if (errorMessage.isBlank() && posts != null) {
                    viewState.update { it.copy(isLoading = false, filteredPostList = posts.toMutableStateList()) }
                } else {
                    coroutineScope.launch(Dispatchers.IO) {
                        _oneShotEvents.send(FilteredPostsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun getUpvotedPosts(){
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.getUpvotedPosts() { errorMessage, posts ->
                if (errorMessage.isBlank() && posts != null) {
                    viewState.update { it.copy(isLoading = false, filteredPostList = posts.toMutableStateList()) }
                } else {
                    coroutineScope.launch(Dispatchers.IO) {
                        _oneShotEvents.send(FilteredPostsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun getDownvotedPosts(){
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.getDownvotedPosts() { errorMessage, posts ->
                if (errorMessage.isBlank() && posts != null) {
                    viewState.update { it.copy(isLoading = false, filteredPostList = posts.toMutableStateList()) }
                } else {
                    coroutineScope.launch(Dispatchers.IO) {
                        _oneShotEvents.send(FilteredPostsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun getUsersPosts(){
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.getUsersPosts() { errorMessage, posts ->
                if (errorMessage.isBlank() && posts != null) {
                    viewState.update { it.copy(isLoading = false, filteredPostList = posts.toMutableStateList()) }
                } else {
                    coroutineScope.launch(Dispatchers.IO) {
                        _oneShotEvents.send(FilteredPostsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }


}