package hu.bme.aut.android.socialcommunitythread.domain.repository

import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.domain.dto.PostType
import hu.bme.aut.android.socialcommunitythread.domain.dto.ThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.VoteType
import hu.bme.aut.android.socialcommunitythread.domain.model.*
import kotlinx.coroutines.delay
import java.util.Date
/*
object Repository {

    private val threadItemRemoteDataSource = (1..100).map {
        Post(id = it, topicThread = TopicThread(it, "BME_AUT$it", "https://picsum.photos/100"), postedBy = if(it % 2 == 0)"Alfred" else "Leonard", "$it hours ago",it, it,
            "Mocked Post Title$it", "asdasdsegvrhlonumosgvepnmrhjnse5gvlohimjuosgvjinmeprhsegvrojnpimopsgvjimnersopgverjnimopgvsjmneropgvjnsreimgvnrseopjmgvrjnoepsimrgvosejnimpé", tags = listOf("TAG1", "TAG2", "TAG3"), false,
             PostType.GIF, VoteType.CLEAR, "", "", videoUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4")

    }
    private val commentItemRemoteDataSource: MutableList<PersonalCommentModel> = (1..100).map {
        PersonalCommentModel(it, ThreadPost(), AppUser(), it, Date(), mutableListOf(), mutableListOf(), "https://picsum.photos/100", VoteType.CLEAR, "This is my really thoughtful comment!")
    }.toMutableList()

    private val followedThreadItems = mutableListOf<String>(threadItemRemoteDataSource[0].topicThread.name, threadItemRemoteDataSource[10].topicThread.name, threadItemRemoteDataSource[25].topicThread.name)

    private val savedPosts = mutableListOf<Post>()

    private val threadList = (1..100).map {
        TopicThread(id = it, name = "BME_AUT$it", threadImageUrl = "https://picsum.photos/100", description = "This is the example description of this topicThread! And some text after it aderpiugojhepgruiogprofiujhedfwsedjoipfjsedoiwjoefdsiw$it \n  \n Ez nem kéne, csak egy helyen")
    }.toMutableList()

    private val chatList = (1..100).map{
       // PersonalChatConversation(id = it.toLong(), conversationName = "Sample User $it", imageUrl = "https://picsum.photos/100", seen = it % 2 == 0, messagePreview = "Thank you Sample User ${it + 1}", date ="$it hours ago")
    }

    private val topicThreadPostList : MutableList<Post> = mutableListOf()

    suspend fun getAllChat(): List<PersonalChatConversation>{
        delay(500)
        return chatList
    }

    suspend fun getAllThread(): List<TopicThread>{
        delay(1000L)
        return threadList
    }

    suspend fun getThreadWithId(threadId: Int): TopicThread{
        return threadList.find { it.id == threadId }!!
    }

    suspend fun getThreadItemsWithPaging(page: Int, pageSize: Int): Result<List<Post>> {
        delay(3000L)
        val startingIndex = page * pageSize
        return if (startingIndex + pageSize <= threadItemRemoteDataSource.size) {
            Result.success(
                threadItemRemoteDataSource.slice(startingIndex until startingIndex + pageSize)
            )
        } else Result.success(emptyList())
    }

    suspend fun getTopicThreadPosts(page: Int, pageSize: Int, threadId: Int): Result<List<Post>>{
        delay(1000L)
        topicThreadPostList.clear()
        for(post in threadItemRemoteDataSource){
            if(post.topicThread.id == threadId)
                topicThreadPostList.add(post)
        }
        val startingIndex = page * pageSize
        return if (startingIndex + pageSize <= topicThreadPostList.size) {
            Result.success(
                topicThreadPostList.slice(startingIndex until startingIndex + pageSize)
            )
        } else Result.success(emptyList())
    }

    suspend fun getThreadItemWithId(id: Int): Post {
        delay(1000L)
        return threadItemRemoteDataSource.find {
            it.id == id
        }!!
    }

    suspend fun getCommentModels(): List<PersonalCommentModel> {
        delay(1000L)
        return commentItemRemoteDataSource
    }

    suspend fun addCommentModel(userName: String, message: String){
        delay(1000L)
       /*commentItemRemoteDataSource.add(PersonalCommentModel(
            id = commentItemRemoteDataSource.size + 1,
            userName = userName,
            commentTime = "2 hours ago",
            voteNumber = 1,
            profileImageUrl = "https://picsum.photos/100",
            voteType = VoteType.UPVOTED,
            commentText = message
        ))*/
    }

    suspend fun getFollowedThreadItems(): List<String>{
        delay(1000L)
        return followedThreadItems
    }

    suspend fun followThread(threadName: String){
        delay(1000L)
    }

    suspend fun unfollowThread(threadName: String){
        delay(1000L)
    }

    suspend fun createThread(topicThread: TopicThread){
        delay(1000L)
        topicThread.id = threadList.size + 1
        threadList.add(topicThread)
    }

    suspend fun deleteThread(post: Post){
        delay(1000L)
    }

    suspend fun addFriend(){
        delay(1000L)
    }

    suspend fun deleteFriend(){
        delay(1000L)
    }

    suspend fun savePost(post: Post){
        delay(1000L)
        savedPosts.add(post)
    }

    suspend fun unsavePost(post: Post){
        delay(1000L)
        savedPosts.remove(post)
    }

    suspend fun getSavedPosts(): MutableList<Post>{
        delay(1000L)
        return savedPosts
    }

}*/