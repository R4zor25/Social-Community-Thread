package hu.bme.aut.android.socialcommunitythread.network

import hu.bme.aut.android.socialcommunitythread.domain.auth.AuthRequest
import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.domain.dto.ChatMessage
import hu.bme.aut.android.socialcommunitythread.domain.dto.JwtResponse
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
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface IRetrofitService {

    //AUTH-SERVICE
    @POST("api/auth/register")
    fun register(@Body userRequest: UserRequest): Call<Void>

    @POST("api/auth/login")
    fun login(@Body authRequest: AuthRequest): Call<JwtResponse>

    @GET("api/auth/validate")
    fun validateToken(@Query("token") token: String): Call<String>

    @POST("api/auth/refreshToken")
    fun getAccessToken(@Body refreshTokenRequest: RefreshTokenRequest): Call<JwtResponse>

    @GET("api/auth/users")
    fun getAllUsers(@Header("Authorization") accessToken: String): Call<List<AppUser>>

    @GET("api/auth/users/id")
    fun findUserById(@Header("Authorization") accessToken: String, @Path("id") id: Long): Call<AppUser>

    @GET("api/auth/users/{username}")
    fun findUserByUsername(@Header("Authorization") accessToken: String, @Path("username") username: String): Call<AppUser>

    @PUT("api/auth/users/{id}/update")
    fun updateUser(@Header("Authorization") accessToken: String, @Path("id") id: Long, @Body appUser: AppUser): Call<AppUser>

    //CHAT-SERVICE

    @GET("api/chat/{userId}/conversations")
    fun getAllChatConversationForUser(@Header("Authorization") accessToken: String, @Path("userId") userId: Long): Call<List<PersonalChatConversation>>

    @GET("api/chat/{userId}/{conversationId}")
    fun getChatConversation(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("conversationId") conversationId: Long): Call<PersonalChatConversation>

    @POST("api/chat/{userId}/{conversationId}/send")
    fun sendMessage(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("conversationId") conversationId: Long, @Body message: ChatMessage): Call<ChatMessage>

    @POST("api/chat/{userId}/create")
    fun createChatConversation(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Body personalChatConversation: PersonalChatConversation): Call<PersonalChatConversation>

    @POST("api/chat/{conversationId}/addParticipants")
    fun addParticipants(@Header("Authorization") accessToken: String, @Path("conversationId") conversationId: Long, @Body userIds: List<Long>): Call<Void>

    @POST("api/chat/{conversationId}/removeParticipants")
    fun removeParticipants(@Header("Authorization") accessToken: String, @Path("conversationId") conversationId: Long, @Body userIds: List<Long>): Call<Void>

    //FRIEND-SERVICE
    @GET("api/friend/{userId}")
    fun getAllFriends(@Header("Authorization") accessToken: String, @Path("userId") userId: Long): Call<List<AppUser>>

    @GET("api/friend/{userId}/incoming")
    fun getIncomingFriendRequest(@Header("Authorization") accessToken: String, @Path("userId") userId: Long): Call<List<AppUser>>

    @GET("api/friend/{userId}/outgoing")
    fun getOutgoingFriendRequest(@Header("Authorization") accessToken: String, @Path("userId") userId: Long): Call<List<AppUser>>

    @POST("api/friend/{userId}/send/{friendId}")
    fun sendFriendRequest(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("friendId") friendId: Long): Call<Void>

    @POST("api/friend/{userId}/accept/{friendId}")
    fun acceptRequest(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("friendId") friendId: Long): Call<Void>

    @POST("api/friend/{userId}/decline/{friendId}")
    fun declineRequest(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("friendId") friendId: Long): Call<Void>

    @POST("api/friend/{userId}/revoke/{friendId}")
    fun revokeRequest(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("friendId") friendId: Long): Call<Void>

    @POST("api/friend/{userId}/delete/{friendId}")
    fun deleteFriend(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("friendId") friendId: Long): Call<Void>

    //THREAD-SERVICE

    @GET("api/thread/{userId}/posts/recommended")
    fun getRecommendedPosts(@Header("Authorization") accessToken: String, @Path("userId") userId: Long): Call<List<PersonalThreadPost>>

    @GET("api/thread/{userId}/{threadId}/posts")
    fun getTopicThreadPosts(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long): Call<List<PersonalThreadPost>>

    @GET("api/thread/{userId}/{threadId}/details")
    fun getTopicThreadDetails(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long): Call<PersonalTopicThread>

    @GET("api/thread/{userId}/{threadId}/{postId}/details")
    fun getPostDetails(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Path("postId") postId: Long): Call<PersonalThreadPost>

    @GET("api/thread/{userId}/followed")
    fun getFollowedThreads(@Header("Authorization") accessToken: String, @Path("userId") userId: Long): Call<List<TopicThread>>

    @GET("api/thread/{userId}/saved")
    fun getSavedPosts(@Header("Authorization") accessToken: String, @Path("userId") userId: Long): Call<List<PersonalThreadPost>>

    @GET("api/thread/{userId}/upvoted")
    fun getUpvotedPosts(@Header("Authorization") accessToken: String, @Path("userId") userId: Long): Call<List<PersonalThreadPost>>

    @GET("api/thread/{userId}/downvoted")
    fun getDownvotedPosts(@Header("Authorization") accessToken: String, @Path("userId") userId: Long): Call<List<PersonalThreadPost>>

    @GET("api/thread/{userId}/posts")
    fun getUsersPosts(@Header("Authorization") accessToken: String, @Path("userId") userId: Long): Call<List<PersonalThreadPost>>

    @PUT("api/thread/{userId}/{threadId}/{postId}/save")
    fun savePost(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Path("postId") postId: Long): Call<Void>

    @PUT("api/thread/{userId}/{threadId}/{postId}/unsave")
    fun unsavePost(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Path("postId") postId: Long): Call<Void>

    @PUT("api/thread/{userId}/{threadId}/follow")
    fun followThread(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long): Call<Void>

    @PUT("api/thread/{userId}/{threadId}/unfollow")
    fun unfollowThread(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long): Call<Void>

    @DELETE("api/thread/{userId}/{threadId}/{postId}/delete")
    fun deletePost(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Path("postId") postId: Long): Call<Void>

    @DELETE("api/thread/{userId}/{threadId}/delete")
    fun deleteThread(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long): Call<Void>

    @POST("api/thread/{userId}/{threadId}/{postId}/comment")
    fun postComment(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Path("postId") postId: Long, @Body commentModel: CommentModel): Call<PersonalCommentModel>

    @PUT("/api/thread/{userId}/{threadId}/{postId}/{commentId}/upvote")
    fun upvoteComment(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Path("postId") postId: Long, @Path("commentId") commentId: Long): Call<Void>

    @PUT("/api/thread/{userId}/{threadId}/{postId}/{commentId}/downvote")
    fun downvoteComment(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Path("postId") postId: Long, @Path("commentId") commentId: Long): Call<Void>

    @PUT("api/thread/{userId}/{threadId}/modify")
    fun modifyThreadData(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Body topicThread: TopicThread): Call<TopicThread>

    @GET("api/thread/search")
    fun getFilteredThreads(@Header("Authorization") accessToken: String, @Query("containsString") containsString: String): Call<List<TopicThread>>

    @GET("api/thread/{userId}/{threadId}/post/search")
    fun getFilteredPosts(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Query("containsString") containsString: String): Call<List<PersonalThreadPost>>

    @PUT("api/thread/{userId}/{threadId}/{postId}/upvote")
    fun upvoteThreadPost(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Path("postId") postId: Long): Call<Void>

    @PUT("api/thread/{userId}/{threadId}/{postId}/downvote")
    fun downvoteThreadPost(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Path("postId") postId: Long): Call<Void>

    @POST("api/thread/{userId}/{threadId}/create")
    fun createPost(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Path("threadId") threadId: Long, @Body threadPost: ThreadPost): Call<Void>

    @POST("api/thread/{userId}/create")
    fun createThread(@Header("Authorization") accessToken: String, @Path("userId") userId: Long, @Body topicThread: TopicThread): Call<Void>

}