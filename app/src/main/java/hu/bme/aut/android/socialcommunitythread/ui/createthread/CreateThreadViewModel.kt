package hu.bme.aut.android.socialcommunitythread.ui.createthread

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateThreadViewModel @Inject constructor() : ViewModel() {
    private val coroutineScope = MainScope()

    private val _viewState: MutableStateFlow<CreateThreadViewState> = MutableStateFlow(CreateThreadViewState())
    val viewState = _viewState.asStateFlow()

    private val _oneShotEvents = Channel<CreateThreadOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    private val repository = Repository

    fun onAction(createThreadUiAction: CreateThreadUiAction){
        when (createThreadUiAction){
            is CreateThreadUiAction.CreateThread -> {
                coroutineScope.launch (Dispatchers.IO) {
                    repository.createThread(createThreadUiAction.thread)
                    _oneShotEvents.send(CreateThreadOneShotEvent.ThreadCreated())
                }
            }
        }
    }
}