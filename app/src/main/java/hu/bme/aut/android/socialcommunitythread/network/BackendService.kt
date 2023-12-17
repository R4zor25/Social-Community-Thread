package hu.bme.aut.android.socialcommunitythread.network

import android.content.SharedPreferences
import hu.bme.aut.android.socialcommunitythread.domain.auth.AuthRequest
import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.domain.dto.ChatMessage
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalTopicThread
import hu.bme.aut.android.socialcommunitythread.domain.dto.RefreshTokenRequest
import hu.bme.aut.android.socialcommunitythread.domain.dto.ThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.ThreadPostDTO
import hu.bme.aut.android.socialcommunitythread.domain.dto.TopicThreadDTO
import hu.bme.aut.android.socialcommunitythread.domain.dto.UserRequest
import hu.bme.aut.android.socialcommunitythread.domain.model.CommentModel
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalCommentModel
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalChatConversation
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import javax.inject.Inject

interface IBackendService {
    //AUTH-SERVICE
    suspend fun register(userRequest: UserRequest, callback: (String) -> Unit)
    suspend fun login(authRequest: AuthRequest, callback: (String, AppUser?) -> Unit)
    suspend fun validateToken(token: String, callback: (String) -> Unit)
    suspend fun getAccessToken(refreshTokenRequest: RefreshTokenRequest, callback: (String, AppUser?) -> Unit)
    suspend fun getAllUser(callback: (String, List<AppUser>?) -> Unit)
    suspend fun getUserByUsername(username: String, callback: (String, AppUser?) -> Unit)
    suspend fun getUserById(id: Long, callback: (String, AppUser?) -> Unit)

    suspend fun updateUser(userId: Long, appUser: AppUser, callback: (String, AppUser?) -> Unit)

    //CHAT-SERVICE

    suspend fun getAllChatConversationForUser(userId: Long, callback: (String, List<PersonalChatConversation>?) -> Unit)
    suspend fun getChatConversation(userId: Long, conversationId: Long, callback: (String, PersonalChatConversation?) -> Unit)
    suspend fun createChatConversation(userId: Long, personalChatConversation: PersonalChatConversation, callback: (String) -> Unit)
    suspend fun sendMessage(userId: Long, conversationId: Long, message: ChatMessage, callback: (String, ChatMessage?) -> Unit)
    suspend fun addParticipants(conversationId: Long, userIds: List<Long>, callback: (String) -> Unit)
    suspend fun removeParticipants(conversationId: Long, userIds: List<Long>, callback: (String) -> Unit)

    //FRIEND-SERVICE
    suspend fun getAllFriends(userId: Long, callback: (String, List<AppUser>?) -> Unit)
    suspend fun getIncomingFriendRequest(userId: Long, callback: (String, List<AppUser>?) -> Unit)
    suspend fun getOutgoingFriendRequest(userId: Long, callback: (String, List<AppUser>?) -> Unit)
    suspend fun sendFriendRequest(userId: Long, friendId: Long, callback: (String) -> Unit)
    suspend fun acceptRequest(userId: Long, friendId: Long, callback: (String) -> Unit)
    suspend fun declineRequest(userId: Long, friendId: Long, callback: (String) -> Unit)
    suspend fun revokeRequest(userId: Long, friendId: Long, callback: (String) -> Unit)
    suspend fun deleteFriend(userId: Long, friendId: Long, callback: (String) -> Unit)

    //THREAD-SERVICE
    suspend fun getRecommendedPosts(userId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit)
    suspend fun getTopicThreadPosts(userId: Long, threadId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit)
    suspend fun getPostDetails(userId: Long, threadId: Long, postId: Long, callback: (String, PersonalThreadPost?) -> Unit)
    suspend fun getTopicThreadDetails(userId: Long, threadId: Long, callback: (String, PersonalTopicThread?) -> Unit)
    suspend fun getFollowedThreads(userId: Long, callback: (String, List<TopicThread>?) -> Unit)
    suspend fun getSavedPosts(userId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit)
    suspend fun getUpvotedPosts(userId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit)
    suspend fun getDownvotedPosts(userId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit)
    suspend fun getUsersPosts(userId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit)
    suspend fun savePost(userId: Long, threadId: Long, postId: Long, callback: (String) -> Unit)
    suspend fun unsavePost(userId: Long, threadId: Long, postId: Long, callback: (String) -> Unit)
    suspend fun followThread(userId: Long, threadId: Long, callback: (String) -> Unit)
    suspend fun unfollowThread(userId: Long, threadId: Long, callback: (String) -> Unit)
    suspend fun deletePost(userId: Long, threadId: Long, postId: Long, callback: (String) -> Unit)
    suspend fun deleteThread(userId: Long, threadId: Long, callback: (String) -> Unit)
    suspend fun postComment(userId: Long, threadId: Long, postId: Long, commentModel: CommentModel, callback: (String, PersonalCommentModel?) -> Unit)
    suspend fun upvoteComment(userId: Long, threadId: Long, postId: Long, commentId: Long, callback: (String) -> Unit)
    suspend fun downvoteComment(userId: Long, threadId: Long, postId: Long, commentId: Long, callback: (String) -> Unit)
    suspend fun modifyThreadData(userId: Long, threadId: Long, @Body topicThread: TopicThread, callback: (String, TopicThread?) -> Unit)
    suspend fun getFilteredPosts(userId: Long, threadId: Long, containsString: String, callback: (String, List<PersonalThreadPost>?) -> Unit)
    suspend fun getFilteredThreads(containsString: String, callback: (String, List<TopicThread>?) -> Unit)
    suspend fun upvoteThreadPost(userId: Long, threadId: Long, postId: Long, callback: (String) -> Unit)
    suspend fun downvoteThreadPost(userId: Long, threadId: Long, postId: Long, callback: (String) -> Unit)
    suspend fun createPost(userId: Long, threadId: Long, threadPost: ThreadPost, callback: (String) -> Unit)
    suspend fun createThread(userId: Long, topicThread: TopicThread, callback: (String) -> Unit)

}

class BackendService @Inject constructor(
    private val retrofit: IRetrofitService,
    private val preferences: SharedPreferences,
) : IBackendService {

    //AUTH-SERVICE
    override suspend fun register(userRequest: UserRequest, callback: (String) -> Unit) {
        val response = retrofit.register(userRequest).execute()
        if (response.isSuccessful) {
            retrofit.login(AuthRequest().apply {
                this.username = userRequest.username
                this.password = userRequest.password
            })
            callback.invoke("")
        } else {
            callback.invoke(response.errorBody().toString())
        }


    }

    override suspend fun login(authRequest: AuthRequest, callback: (String, AppUser?) -> Unit) {
        val response = retrofit.login(authRequest).execute()
        if (response.isSuccessful) {
            val jwtResponse = response.body()
            preferences.edit().putString("access", jwtResponse?.accessToken).apply()
            preferences.edit().putString("refresh", jwtResponse?.token).apply()
            callback.invoke("", jwtResponse!!.user)
        } else {
            callback.invoke("Invalid username or password!", null)
        }
    }

    override suspend fun validateToken(token: String, callback: (String) -> Unit) {
        val response = retrofit.validateToken(token).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun getAccessToken(refreshTokenRequest: RefreshTokenRequest, callback: (String, AppUser?) -> Unit) {
        val response = retrofit.getAccessToken(refreshTokenRequest).execute()
        if (response.isSuccessful) {
            val jwtResponse = response.body()
            preferences.edit().putString("access", jwtResponse?.accessToken).apply()
            preferences.edit().putString("refresh", jwtResponse?.token).apply()
            callback.invoke("", jwtResponse!!.user)
        } else {
            callback.invoke("Please sign in!", null)
        }
    }

    override suspend fun getAllUser(callback: (String, List<AppUser>?) -> Unit) {
        val response = retrofit.getAllUsers("Bearer ${preferences.getString("access", "")}").execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body())
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getUserByUsername(username: String, callback: (String, AppUser?) -> Unit) {
        val response = retrofit.findUserByUsername("Bearer ${preferences.getString("access", "")}", username).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getUserById(id: Long, callback: (String, AppUser?) -> Unit) {
        val response = retrofit.findUserById("Bearer ${preferences.getString("access", "")}", id).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun updateUser(userId: Long, appUser: AppUser, callback: (String, AppUser?) -> Unit) {
        val response = retrofit.updateUser("Bearer ${preferences.getString("access", "")}", userId, appUser).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }
    //FRIEND-SERVICE

    override suspend fun getAllChatConversationForUser(userId: Long, callback: (String, List<PersonalChatConversation>?) -> Unit) {
        val response = retrofit.getAllChatConversationForUser("Bearer ${preferences.getString("access", "")}", userId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getChatConversation(userId: Long, conversationId: Long, callback: (String, PersonalChatConversation?) -> Unit) {
        val response = retrofit.getChatConversation("Bearer ${preferences.getString("access", "")}", userId, conversationId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun createChatConversation(userId: Long, personalChatConversation: PersonalChatConversation, callback: (String) -> Unit) {
        val response = retrofit.createChatConversation("Bearer ${preferences.getString("access", "")}", userId, personalChatConversation).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun sendMessage(userId: Long, conversationId: Long, message: ChatMessage, callback: (String, ChatMessage?) -> Unit) {
        val response = retrofit.sendMessage("Bearer ${preferences.getString("access", "")}", userId, conversationId, message).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun addParticipants(conversationId: Long, userIds: List<Long>, callback: (String) -> Unit) {
        val response = retrofit.addParticipants("Bearer ${preferences.getString("access", "")}", conversationId, userIds).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun removeParticipants(conversationId: Long, userIds: List<Long>, callback: (String) -> Unit) {
        val response = retrofit.removeParticipants("Bearer ${preferences.getString("access", "")}", conversationId, userIds).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun getAllFriends(userId: Long, callback: (String, List<AppUser>?) -> Unit) {
        val response = retrofit.getAllFriends("Bearer ${preferences.getString("access", "")}", userId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getIncomingFriendRequest(userId: Long, callback: (String, List<AppUser>?) -> Unit) {
        val response = retrofit.getIncomingFriendRequest("Bearer ${preferences.getString("access", "")}", userId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getOutgoingFriendRequest(userId: Long, callback: (String, List<AppUser>?) -> Unit) {
        val response = retrofit.getOutgoingFriendRequest("Bearer ${preferences.getString("access", "")}", userId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun sendFriendRequest(userId: Long, friendId: Long, callback: (String) -> Unit) {
        val response = retrofit.sendFriendRequest("Bearer ${preferences.getString("access", "")}", userId, friendId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun acceptRequest(userId: Long, friendId: Long, callback: (String) -> Unit) {
        val response = retrofit.acceptRequest("Bearer ${preferences.getString("access", "")}", userId, friendId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun declineRequest(userId: Long, friendId: Long, callback: (String) -> Unit) {
        val response = retrofit.declineRequest("Bearer ${preferences.getString("access", "")}", userId, friendId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun revokeRequest(userId: Long, friendId: Long, callback: (String) -> Unit) {
        val response = retrofit.revokeRequest("Bearer ${preferences.getString("access", "")}", userId, friendId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun deleteFriend(userId: Long, friendId: Long, callback: (String) -> Unit) {
        val response = retrofit.deleteFriend("Bearer ${preferences.getString("access", "")}", userId, friendId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun getRecommendedPosts(userId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit) {
        val response = retrofit.getRecommendedPosts("Bearer ${preferences.getString("access", "")}", userId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke(response.errorBody().toString(), null)
        }
    }

    override suspend fun getTopicThreadPosts(userId: Long, threadId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit) {
        val response = retrofit.getTopicThreadPosts("Bearer ${preferences.getString("access", "")}", userId, threadId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getPostDetails(userId: Long, threadId: Long, postId: Long, callback: (String, PersonalThreadPost?) -> Unit) {
        val response = retrofit.getPostDetails("Bearer ${preferences.getString("access", "")}", userId, threadId, postId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getTopicThreadDetails(userId: Long, threadId: Long, callback: (String, PersonalTopicThread?) -> Unit) {
        val response = retrofit.getTopicThreadDetails("Bearer ${preferences.getString("access", "")}", userId, threadId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getFollowedThreads(userId: Long, callback: (String, List<TopicThread>?) -> Unit) {
        val response = retrofit.getFollowedThreads("Bearer ${preferences.getString("access", "")}", userId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getSavedPosts(userId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit) {
        val response = retrofit.getSavedPosts("Bearer ${preferences.getString("access", "")}", userId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getUpvotedPosts(userId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit) {
        val response = retrofit.getUpvotedPosts("Bearer ${preferences.getString("access", "")}", userId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getDownvotedPosts(userId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit) {
        val response = retrofit.getDownvotedPosts("Bearer ${preferences.getString("access", "")}", userId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getUsersPosts(userId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit) {
        val response = retrofit.getUsersPosts("Bearer ${preferences.getString("access", "")}", userId).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun savePost(userId: Long, threadId: Long, postId: Long, callback: (String) -> Unit) {
        val response = retrofit.savePost("Bearer ${preferences.getString("access", "")}", userId, threadId, postId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun unsavePost(userId: Long, threadId: Long, postId: Long, callback: (String) -> Unit) {
        val response = retrofit.unsavePost("Bearer ${preferences.getString("access", "")}", userId, threadId, postId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun followThread(userId: Long, threadId: Long, callback: (String) -> Unit) {
        val response = retrofit.followThread("Bearer ${preferences.getString("access", "")}", userId, threadId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun unfollowThread(userId: Long, threadId: Long, callback: (String) -> Unit) {
        val response = retrofit.unfollowThread("Bearer ${preferences.getString("access", "")}", userId, threadId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun deletePost(userId: Long, threadId: Long, postId: Long, callback: (String) -> Unit) {
        val response = retrofit.deletePost("Bearer ${preferences.getString("access", "")}", userId, threadId, postId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun deleteThread(userId: Long, threadId: Long, callback: (String) -> Unit) {
        val response = retrofit.deleteThread("Bearer ${preferences.getString("access", "")}", userId, threadId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun postComment(userId: Long, threadId: Long, postId: Long, commentModel: CommentModel, callback: (String, PersonalCommentModel?) -> Unit) {
        val response = retrofit.postComment("Bearer ${preferences.getString("access", "")}", userId, threadId, postId, commentModel).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun upvoteComment(userId: Long, threadId: Long, postId: Long, commentId: Long, callback: (String) -> Unit) {
        val response = retrofit.upvoteComment("Bearer ${preferences.getString("access", "")}", userId, threadId, postId, commentId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun downvoteComment(userId: Long, threadId: Long, postId: Long, commentId: Long, callback: (String) -> Unit) {
        val response = retrofit.downvoteComment("Bearer ${preferences.getString("access", "")}", userId, threadId, postId, commentId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun modifyThreadData(userId: Long, threadId: Long, topicThread: TopicThread, callback: (String, TopicThread?) -> Unit) {
        val response = retrofit.modifyThreadData("Bearer ${preferences.getString("access", "")}", userId, threadId, topicThread).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body())
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getFilteredPosts(userId: Long, threadId: Long, containsString: String, callback: (String, List<PersonalThreadPost>?) -> Unit) {
        val response = retrofit.getFilteredPosts("Bearer ${preferences.getString("access", "")}", userId, threadId, containsString).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body())
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun getFilteredThreads(containsString: String, callback: (String, List<TopicThread>?) -> Unit) {
        val response = retrofit.getFilteredThreads("Bearer ${preferences.getString("access", "")}", containsString).execute()
        if (response.isSuccessful) {
            callback.invoke("", response.body()!!)
        } else {
            callback.invoke("Something went wrong!", null)
        }
    }

    override suspend fun upvoteThreadPost(userId: Long, threadId: Long, postId: Long, callback: (String) -> Unit) {
        val response = retrofit.upvoteThreadPost("Bearer ${preferences.getString("access", "")}", userId, threadId, postId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun downvoteThreadPost(userId: Long, threadId: Long, postId: Long, callback: (String) -> Unit) {
        val response = retrofit.downvoteThreadPost("Bearer ${preferences.getString("access", "")}", userId, threadId, postId).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun createPost(userId: Long, threadId: Long, threadPost: ThreadPost, callback: (String) -> Unit) {
        val response = retrofit.createPost("Bearer ${preferences.getString("access", "")}", userId, threadId, threadPost).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }

    override suspend fun createThread(userId: Long, topicThread: TopicThread, callback: (String) -> Unit) {
        val response = retrofit.createThread("Bearer ${preferences.getString("access", "")}", userId, topicThread).execute()
        if (response.isSuccessful) {
            callback.invoke("")
        } else {
            callback.invoke("Something went wrong!")
        }
    }
}