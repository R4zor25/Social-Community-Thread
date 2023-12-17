package hu.bme.aut.android.socialcommunitythread.ui.createthread

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.socialcommunitythread.domain.interactors.ThreadInteractor
import hu.bme.aut.android.socialcommunitythread.domain.model.ThreadPublicity
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.ui.mainthread.MainThreadViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class CreateThreadViewModel @Inject constructor(
    private val threadInteractor: ThreadInteractor
) : ViewModel() {
    private val coroutineScope = MainScope()

    private val viewState: MutableStateFlow<CreateThreadViewState> = MutableStateFlow(CreateThreadViewState())
    val uiState = viewState.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        viewState.value
    )

    private val _oneShotEvents = Channel<CreateThreadOneShotEvent>(Channel.BUFFERED)
    val oneShotEvent = _oneShotEvents.receiveAsFlow()

    fun createThread(){
        coroutineScope.launch(Dispatchers.IO) {
            val topicThread = TopicThread().apply {
                this.name = viewState.value.threadName
                this.description = viewState.value.description
                //this.publicity = if(viewState.value.isPublic) ThreadPublicity.PUBLIC else ThreadPublicity.PRIVATE
                this.threadImage = viewState.value.threadImage
                }
            threadInteractor.createThread(topicThread){ errorMessage ->
                coroutineScope.launch(Dispatchers.IO) {
                    if (errorMessage.isBlank()) {
                        _oneShotEvents.send(CreateThreadOneShotEvent.ThreadCreated())
                    } else {
                        _oneShotEvents.send(CreateThreadOneShotEvent.ShowToastMessage(errorMessage))
                    }
                }
            }
        }
    }

    fun onDescriptionTextChange(description : String) {
        viewState.update { it.copy(description = description) }
    }

    fun onThreadNameTextChange(threadName : String) {
        viewState.update { it.copy(threadName = threadName) }
    }

    fun onThreadImageChange(threadImage : Bitmap) {
        val stream = ByteArrayOutputStream()
        threadImage.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        viewState.update { it.copy(threadImage = image) }
    }

    fun onThreadPublicityChange(isPublic : Boolean){

    }
}