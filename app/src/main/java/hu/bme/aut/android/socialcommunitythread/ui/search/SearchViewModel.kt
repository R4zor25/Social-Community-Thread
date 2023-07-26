package hu.bme.aut.android.socialcommunitythread.ui.search

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.repository.Repository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
    private val coroutineScope = MainScope()

    //private val _viewState: MutableStateFlow<SearchViewState> = MutableStateFlow(SearchViewState())
    //val viewState = _viewState.asStateFlow()
    var viewState by mutableStateOf(SearchViewState())

    private val _oneShotEvents = Channel<SearchOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    private val repository = Repository

    init {
        viewModelScope.launch {
            val list = repository.getAllThread()
            viewState = viewState.copy(isLoading = false, items = list)
            _oneShotEvents.send(SearchOneShotEvent.initAutoComplete)
        }
    }

    fun onAction(searchUiAction: SearchUiAction){
        when (searchUiAction){

            else -> {}
        }
    }
}