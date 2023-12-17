package hu.bme.aut.android.socialcommunitythread.domain.interactors

import com.google.android.play.integrity.internal.t
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.PersonalTopicThread
import hu.bme.aut.android.socialcommunitythread.domain.dto.ThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.ThreadPostDTO
import hu.bme.aut.android.socialcommunitythread.domain.dto.TopicThreadDTO
import hu.bme.aut.android.socialcommunitythread.domain.model.CommentModel
import hu.bme.aut.android.socialcommunitythread.domain.model.PersonalCommentModel
import hu.bme.aut.android.socialcommunitythread.domain.model.TopicThread
import hu.bme.aut.android.socialcommunitythread.network.BackendService
import javax.inject.Inject

class ThreadInteractor @Inject constructor(
    private val backendService: BackendService,
    private val authInteractor: AuthInteractor,
) {

    suspend fun getRecommendedPosts(callback: (String, List<PersonalThreadPost>?) -> Unit) {
        backendService.getRecommendedPosts(AuthInteractor.currentLoggedInUser!!.userId) { errorMessage, recommendedPosts ->
            callback.invoke(errorMessage, recommendedPosts)
        }
    }

    suspend fun getTopicThreadPosts(threadId: Long, callback: (String, List<PersonalThreadPost>?) -> Unit) {
        backendService.getTopicThreadPosts(AuthInteractor.currentLoggedInUser!!.userId, threadId) { errorMessage, posts ->
            callback.invoke(errorMessage, posts)
        }
    }

    suspend fun getPostDetails(threadId: Long, postId: Long, callback: (String, PersonalThreadPost?) -> Unit) {
        backendService.getPostDetails(AuthInteractor.currentLoggedInUser!!.userId, threadId, postId) { errorMessage, postDetails ->
            callback.invoke(errorMessage, postDetails)
        }
    }

    suspend fun getTopicThreadDetails(threadId: Long, callback: (String, PersonalTopicThread?) -> Unit) {
        backendService.getTopicThreadDetails(AuthInteractor.currentLoggedInUser!!.userId, threadId){ errorMessage, topicThread ->
            callback.invoke(errorMessage, topicThread)
        }
    }

    suspend fun getFollowedThreads(userId: Long, callback: (String, List<TopicThread>?) -> Unit) {
        backendService.getFollowedThreads(AuthInteractor.currentLoggedInUser!!.userId) { errorMessage, followedList ->
            callback.invoke(errorMessage, followedList)
        }
    }

    suspend fun getSavedPosts(callback: (String, List<PersonalThreadPost>?) -> Unit) {
        backendService.getSavedPosts(AuthInteractor.currentLoggedInUser!!.userId) { errorMessage, savePosts ->
            callback.invoke(errorMessage, savePosts)
        }
    }

    suspend fun getUpvotedPosts(callback: (String, List<PersonalThreadPost>?) -> Unit) {
        backendService.getUpvotedPosts(AuthInteractor.currentLoggedInUser!!.userId) { errorMessage, savePosts ->
            callback.invoke(errorMessage, savePosts)
        }
    }

    suspend fun getDownvotedPosts(callback: (String, List<PersonalThreadPost>?) -> Unit) {
        backendService.getDownvotedPosts(AuthInteractor.currentLoggedInUser!!.userId) { errorMessage, savePosts ->
            callback.invoke(errorMessage, savePosts)
        }
    }

    suspend fun getUsersPosts(callback: (String, List<PersonalThreadPost>?) -> Unit) {
        backendService.getUsersPosts(AuthInteractor.currentLoggedInUser!!.userId) { errorMessage, savePosts ->
            callback.invoke(errorMessage, savePosts)
        }
    }

    suspend fun savePost(threadId: Long, postId: Long, callback: (String) -> Unit) {
        backendService.savePost(AuthInteractor.currentLoggedInUser!!.userId, threadId, postId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun unsavePost(threadId: Long, postId: Long, callback: (String) -> Unit){
        backendService.unsavePost(AuthInteractor.currentLoggedInUser!!.userId, threadId, postId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun followThread(threadId: Long, callback: (String) -> Unit) {
        backendService.followThread(AuthInteractor.currentLoggedInUser!!.userId, threadId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun unfollowThread(threadId: Long, callback: (String) -> Unit) {
        backendService.unfollowThread(AuthInteractor.currentLoggedInUser!!.userId, threadId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun deletePost(threadId: Long, postId: Long, callback: (String) -> Unit) {
        backendService.deletePost(AuthInteractor.currentLoggedInUser!!.userId, threadId, postId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun deleteThread(threadId: Long, callback: (String) -> Unit) {
        backendService.deleteThread(AuthInteractor.currentLoggedInUser!!.userId, threadId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun postComment(threadId: Long, postId: Long, commentModel: CommentModel, callback: (String, PersonalCommentModel?) -> Unit) {
        backendService.postComment(AuthInteractor.currentLoggedInUser!!.userId, threadId, postId, commentModel) { errorMessage, comment ->
            callback.invoke(errorMessage, comment)
        }
    }

    suspend fun upvoteComment(threadId: Long, postId: Long, commentId: Long, callback: (String) -> Unit){
        backendService.upvoteComment(AuthInteractor.currentLoggedInUser!!.userId, threadId, postId, commentId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }
    suspend fun downvoteComment(threadId: Long, postId: Long, commentId: Long, callback: (String) -> Unit){
        backendService.downvoteComment(AuthInteractor.currentLoggedInUser!!.userId, threadId, postId, commentId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun modifyThreadData(threadId: Long, topicThread: TopicThread, callback: (String, TopicThread?) -> Unit) {
        backendService.modifyThreadData(AuthInteractor.currentLoggedInUser!!.userId, threadId, topicThread) { errorMessage, thread ->
            callback.invoke(errorMessage, thread)
        }
    }

    suspend fun getFilteredPosts(threadId: Long, containsString: String, callback: (String, List<PersonalThreadPost>?) -> Unit) {
        backendService.getFilteredPosts(AuthInteractor.currentLoggedInUser!!.userId, threadId, containsString) { errorMessage, filteredPosts ->
            callback.invoke(errorMessage, filteredPosts)
        }
    }

    suspend fun getFilteredThreads(containsString: String, callback: (String, List<TopicThread>?) -> Unit) {
        backendService.getFilteredThreads(containsString) { errorMessage, filteredThreads ->
            callback.invoke(errorMessage, filteredThreads)
        }
    }

    suspend fun upvoteThreadPost(threadId: Long, postId: Long, callback: (String) -> Unit) {
        backendService.upvoteThreadPost(AuthInteractor.currentLoggedInUser!!.userId, threadId, postId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun downvoteThreadPost(threadId: Long, postId: Long, callback: (String) -> Unit) {
        backendService.downvoteThreadPost(AuthInteractor.currentLoggedInUser!!.userId, threadId, postId) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun createPost(threadId: Long, threadPost: ThreadPost, callback: (String) -> Unit) {
        backendService.createPost(AuthInteractor.currentLoggedInUser!!.userId, threadId, threadPost) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }

    suspend fun createThread(topicThread: TopicThread, callback: (String) -> Unit) {
        backendService.createThread(AuthInteractor.currentLoggedInUser!!.userId, topicThread) { errorMessage ->
            callback.invoke(errorMessage)
        }
    }
}