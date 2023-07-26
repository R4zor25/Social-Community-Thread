package hu.bme.aut.android.socialcommunitythread.ui.threaddetails

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject

@HiltViewModel
class ThreadDetailsViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
) : ViewModel() {
    private val coroutineScope = MainScope()

   // private val _viewState: MutableStateFlow<ThreadDetailsViewState> = MutableStateFlow(ThreadDetailsViewState(true, null))
    //val viewState = _viewState.asStateFlow()
     var viewState by mutableStateOf(ThreadDetailsViewState(true, null))

    private val _oneShotEvents = Channel<ThreadDetailsOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    private val repository = Repository

    var id: Int = 0

    init{
        coroutineScope.launch {
            _oneShotEvents.send(ThreadDetailsOneShotEvent.AcquireId)
        }
    }

    fun onAction(threadDetailsUiAction: ThreadDetailsUiAction){
        when (threadDetailsUiAction){
            is ThreadDetailsUiAction.OnInit ->{
                coroutineScope.launch(Dispatchers.IO) {
                    viewState = viewState.copy(isLoading = true, commentList = repository.getCommentModels())
                    val threadItem = repository.getThreadItemWithId(id)
                    _oneShotEvents.send(ThreadDetailsOneShotEvent.ThreadItemAcquired(post = threadItem))
                    viewState = viewState.copy(isLoading = false, commentList = repository.getCommentModels())
                }
            }
            is ThreadDetailsUiAction.SavePostUiAction -> {
                coroutineScope.launch(Dispatchers.IO) {
                    repository.savePost(threadDetailsUiAction.post)
                }
            }
            is ThreadDetailsUiAction.UnsavePostUiAction -> {
                coroutineScope.launch(Dispatchers.IO) {
                    repository.unsavePost(threadDetailsUiAction.post)
                }
            }
            is ThreadDetailsUiAction.SendComment ->{
                coroutineScope.launch(Dispatchers.IO) {
                    viewState = viewState.copy(isLoading = true)
                    val userName = authInteractor.getCurrentUserName()
                    repository.addCommentModel(userName!!, threadDetailsUiAction.message)
                    viewState = viewState.copy(isLoading = false, commentList = repository.getCommentModels())
                }
            }
        }
    }
}