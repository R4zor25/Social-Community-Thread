package hu.bme.aut.android.socialcommunitythread.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
class SearchViewModel @Inject constructor(
    private val threadInteractor: ThreadInteractor
) : ViewModel() {
    private val coroutineScope = MainScope()

    private val viewState: MutableStateFlow<SearchViewState> = MutableStateFlow(SearchViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<SearchOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            threadInteractor.getFilteredThreads("", ){ errorMessage, topicThreadList ->
                viewModelScope.launch(Dispatchers.IO) {
                    if(errorMessage.isBlank() && topicThreadList != null) {
                        viewState.update { it.copy(isLoading = false, items = topicThreadList) }
                        _oneShotEvents.send(SearchOneShotEvent.InitAutoComplete)
                    } else {
                        _oneShotEvents.send(SearchOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }

        }
    }
}