package hu.bme.aut.android.socialcommunitythread.ui.createpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.dto.PostType
import hu.bme.aut.android.socialcommunitythread.domain.dto.ThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.interactors.ThreadInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val threadInteractor: ThreadInteractor
) : ViewModel() {
    private val coroutineScope = MainScope()

    //private val _viewState: MutableStateFlow<CreateThreadViewState> = MutableStateFlow(CreateThreadViewState())
    //val viewState = _viewState.asStateFlow()

    private val viewState: MutableStateFlow<CreatePostViewState> = MutableStateFlow(CreatePostViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<CreatePostOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    var threadId = -1L


    fun createPost(){
        coroutineScope.launch(Dispatchers.IO) {
            val threadPost = ThreadPost(
                title = viewState.value.postTitle,
                description = viewState.value.description,
                postType = viewState.value.postType,
                file = viewState.value.file,
            )
            threadInteractor.createPost(threadId, threadPost){ errorText ->
                coroutineScope.launch(Dispatchers.IO) {
                if (errorText.isNotBlank()) {
                    _oneShotEvents.send(CreatePostOneShotEvent.ShowToastMessage(errorText))
                } else {
                    _oneShotEvents.send(CreatePostOneShotEvent.PostCreated())
                }
                }

            }
        }
    }

    fun onTitleTextChange(title : String) {
        viewState.update { it.copy(postTitle = title) }
    }

    fun onDescriptionTextChange(description : String) {
        viewState.update { it.copy(description = description) }
    }

    fun onPostFileChange(file  : ByteArray) {
        viewState.update { it.copy(file = file) }
    }

    fun setPostType(postType: PostType){
        viewState.update { it.copy(postType = postType) }
    }
}
