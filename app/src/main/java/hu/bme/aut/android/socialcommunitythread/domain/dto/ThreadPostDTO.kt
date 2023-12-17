package hu.bme.aut.android.socialcommunitythread.domain.dto

data class ThreadPostDTO(
    //@JsonProperty("topicThreadId")
    val topicThreadId: Long,
    //@JsonProperty("postTime")
    val postTime: String,

   // @JsonProperty("title")
    val title: String,

   // @JsonProperty("postType")
    val postType: PostType,

    //@JsonProperty("postText")
    val postText: String = "",
)