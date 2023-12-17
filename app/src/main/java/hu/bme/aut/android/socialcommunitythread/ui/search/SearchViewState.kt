package hu.bme.aut.android.socialcommunitythread.ui.search

import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread

data class SearchViewState(val isLoading: Boolean = true, var items: List<TopicThread> = emptyList() )

sealed class SearchOneShotEvent {
    data class ShowToastMessage(val errorText: String) : SearchOneShotEvent()
    object InitAutoComplete : SearchOneShotEvent()
}