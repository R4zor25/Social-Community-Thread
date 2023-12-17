package hu.bme.aut.android.socialcommunitythread.ui.postdetails

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.ThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.VoteType
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.domain.interactors.ThreadInteractor
import hu.bme.aut.android.socialcommunitythread.domain.model.CommentModel
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalCommentModel
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadOneShotEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val threadInteractor: ThreadInteractor,
) : ViewModel() {
    private val coroutineScope = MainScope()

    private val viewState: MutableStateFlow<PostDetailsViewState> = MutableStateFlow(PostDetailsViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<PostDetailsOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()


    var postId: Long = 0
    var threadId: Long = 0

    init {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.getPostDetails(threadId, postId){ errorMessage, post ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank() && post != null) {
                        viewState.update { it.copy(isLoading = false, post = post) }
                    } else {
                        _oneShotEvents.send(PostDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }


    fun savePost() {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.savePost(viewState.value.post.topicThread.topicThreadId, viewState.value.post.postId!!) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        viewState.update { it.copy(post = it.post.copy(isSavedByUser = true)) }
                        //viewState.update { it.copy(items = mutableStateListOf<Co>().apply { addAll(it.items) }) }
                    } else {
                        _oneShotEvents.send(PostDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun unsavePost() {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.unsavePost(viewState.value.post.topicThread.topicThreadId, viewState.value.post.postId!!) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        viewState.update { it.copy(post = it.post.copy(isSavedByUser = false)) }
                    } else {
                        _oneShotEvents.send(PostDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun upvotePost() {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.upvoteThreadPost(threadId, postId) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        when (viewState.value.post.userVoteType) {
                            VoteType.CLEAR -> {
                                viewState.update {
                                    it.copy(
                                        post = it.post.copy(
                                            userVoteType = VoteType.UPVOTED, voteNumber = it.post.voteNumber + 1
                                        )
                                    )
                                }
                            }

                            VoteType.UPVOTED -> {
                                viewState.update {
                                    it.copy(
                                        post = it.post.copy(
                                            userVoteType = VoteType.CLEAR, voteNumber = it.post.voteNumber - 1
                                        )
                                    )
                                }
                            }

                            VoteType.DOWNVOTED -> {
                                viewState.update {
                                    it.copy(
                                        post = it.post.copy(
                                            userVoteType = VoteType.UPVOTED, voteNumber = it.post.voteNumber + 2
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        _oneShotEvents.send(PostDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun downvotePost() {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.downvoteThreadPost(viewState.value.post.topicThread.topicThreadId, viewState.value.post.postId!!) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        when (viewState.value.post.userVoteType) {
                            VoteType.CLEAR -> {
                                viewState.update {
                                    it.copy(
                                        post = it.post.copy(
                                            userVoteType = VoteType.DOWNVOTED, voteNumber = it.post.voteNumber - 1
                                        )
                                    )
                                }
                            }
                            VoteType.UPVOTED -> {
                                viewState.update {
                                    it.copy(
                                        post = it.post.copy(
                                            userVoteType = VoteType.DOWNVOTED, voteNumber = it.post.voteNumber - 2
                                        )
                                    )
                                }
                            }
                            VoteType.DOWNVOTED -> {
                                viewState.update {
                                    it.copy(
                                        post = it.post.copy(
                                            userVoteType = VoteType.CLEAR, voteNumber = it.post.voteNumber + 1
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        _oneShotEvents.send(PostDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun upvoteComment(commentModel: PersonalCommentModel) {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.upvoteComment(viewState.value.post.topicThread.topicThreadId, viewState.value.post.postId!!, commentModel.id) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        when (commentModel.voteType) {
                            VoteType.CLEAR -> {
                                commentModel.voteNumber += 1
                                commentModel.voteType = VoteType.UPVOTED
                            }
                            VoteType.UPVOTED -> {
                                commentModel.voteType = VoteType.CLEAR
                                commentModel.voteNumber -= 1
                            }
                            VoteType.DOWNVOTED -> {
                                commentModel.voteType = VoteType.UPVOTED
                                commentModel.voteNumber += 2
                            }
                        }
                        viewState.update { it.copy(post = it.post.copy(comments = it.post.comments.map { pComment ->
                            if(pComment.id == commentModel.id){
                                pComment.voteNumber = commentModel.voteNumber
                                pComment.voteType = commentModel.voteType
                            }
                            pComment
                        }.toMutableStateList() )) }
                    } else {
                        _oneShotEvents.send(PostDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun downvoteComment(commentModel: PersonalCommentModel) {
        coroutineScope.launch(Dispatchers.IO) {
            threadInteractor.downvoteComment(viewState.value.post.topicThread.topicThreadId, viewState.value.post.postId!!, commentModel.id) { errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        when (commentModel.voteType) {
                            VoteType.CLEAR -> {
                                commentModel.voteNumber -= 1
                                commentModel.voteType = VoteType.DOWNVOTED
                            }
                            VoteType.UPVOTED -> {
                                commentModel.voteType = VoteType.DOWNVOTED
                                commentModel.voteNumber -= 2
                            }
                            VoteType.DOWNVOTED -> {
                                commentModel.voteType = VoteType.CLEAR
                                commentModel.voteNumber += 1
                            }
                        }
                        viewState.update { it.copy(post = viewState.value.post.copy(comments = it.post.comments.map { pComment ->
                            if(pComment.id == commentModel.id){
                                pComment.voteNumber = commentModel.voteNumber
                                pComment.voteType = commentModel.voteType
                            }
                            pComment
                        }.toMutableStateList() )) }

                    } else {
                        _oneShotEvents.send(PostDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun sendComment(commentText: String) {
        coroutineScope.launch(Dispatchers.IO) {
            val commentModel = CommentModel(
                threadPost = ThreadPost(),
                author = AppUser(),
                commentText = commentText,
                voteNumber = 0,
                commentTime = Date()
            )
            threadInteractor.postComment(viewState.value.post.topicThread.topicThreadId, postId, commentModel) { errorMessage, comment ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        viewState.update { it.copy(post = viewState.value.post.apply { this.comments.add(comment!!) }) }
                    } else {
                        _oneShotEvents.send(PostDetailsOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun initPostId(postId: Long) {
        this.postId = postId
    }
}