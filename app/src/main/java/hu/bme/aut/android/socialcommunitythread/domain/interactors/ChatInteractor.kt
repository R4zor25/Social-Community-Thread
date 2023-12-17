package hu.bme.aut.android.socialcommunitythread.domain.interactors

import hu.bme.aut.android.socialcommunitythread.domain.dto.ChatMessage
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalChatConversation
import hu.bme.aut.android.socialcommunitythread.network.BackendService
import javax.inject.Inject

class ChatInteractor @Inject constructor(
    private val backendService: BackendService,
    private val authInteractor: AuthInteractor,
) {

    suspend fun getAllChatConversationForUser(callback: (String, List<PersonalChatConversation>?) -> Unit) {
        backendService.getAllChatConversationForUser(AuthInteractor.currentLoggedInUser!!.userId) { errorMessage, conversationList ->
            callback.invoke(errorMessage, conversationList)
        }
    }

    suspend fun getChatConversation(conversationId: Long, callback: (String, PersonalChatConversation?) -> Unit) {
        backendService.getChatConversation(AuthInteractor.currentLoggedInUser!!.userId, conversationId) { errorMessage, conversation ->
            callback.invoke(errorMessage, conversation)
        }
    }

    suspend fun createChatConversation(personalChatConversation: PersonalChatConversation, callback: (String) -> Unit) {
        backendService.createChatConversation(AuthInteractor.currentLoggedInUser!!.userId, personalChatConversation) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun sendMessage(conversationId: Long, message: ChatMessage, callback: (String, ChatMessage?) -> Unit) {
        backendService.sendMessage(AuthInteractor.currentLoggedInUser!!.userId, conversationId, message) { errorMessage, message ->
            callback.invoke(errorMessage, message)
        }
    }

    suspend fun addParticipants(conversationId: Long, userIds: List<Long>, callback: (String) -> Unit) {
        backendService.addParticipants(conversationId, userIds) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun removeParticipants(conversationId: Long, userIds: List<Long>, callback: (String) -> Unit) {
        backendService.removeParticipants(conversationId, userIds) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }
}