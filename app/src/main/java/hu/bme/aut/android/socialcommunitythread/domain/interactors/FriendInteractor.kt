package hu.bme.aut.android.socialcommunitythread.domain.interactors

import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.network.BackendService
import javax.inject.Inject

class FriendInteractor @Inject constructor(
    private val backendService: BackendService,
    private val authInteractor: AuthInteractor,
) {

    suspend fun getAllFriends(callback: (String, List<AppUser>?) -> Unit) {
        backendService.getAllFriends(AuthInteractor.currentLoggedInUser!!.userId) { errorMessage, friendList ->
            callback.invoke(errorMessage, friendList)
        }
    }

    suspend fun getIncomingFriendRequest(callback: (String, List<AppUser>?) -> Unit) {
        backendService.getIncomingFriendRequest(AuthInteractor.currentLoggedInUser!!.userId) { errorMessage, incomingFriendRequestList ->
            callback.invoke(errorMessage, incomingFriendRequestList)
        }
    }

    suspend fun getOutgoingFriendRequest(callback: (String, List<AppUser>?) -> Unit) {
        backendService.getOutgoingFriendRequest(AuthInteractor.currentLoggedInUser!!.userId) { errorMessage, outgoingFriendRequestList ->
            callback.invoke(errorMessage, outgoingFriendRequestList)
        }
    }

    suspend fun sendFriendRequest(friendId: Long, callback: (String) -> Unit) {
        backendService.sendFriendRequest(AuthInteractor.currentLoggedInUser!!.userId, friendId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun acceptRequest(friendId: Long, callback: (String) -> Unit) {
        backendService.acceptRequest(AuthInteractor.currentLoggedInUser!!.userId, friendId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun declineRequest(friendId: Long, callback: (String) -> Unit) {
        backendService.declineRequest(AuthInteractor.currentLoggedInUser!!.userId, friendId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun revokeRequest(friendId: Long, callback: (String) -> Unit) {
        backendService.revokeRequest(AuthInteractor.currentLoggedInUser!!.userId, friendId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun deleteFriend(friendId: Long, callback: (String) -> Unit) {
        backendService.deleteFriend(AuthInteractor.currentLoggedInUser!!.userId, friendId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

}