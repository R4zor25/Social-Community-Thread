package hu.bme.aut.android.socialcommunitythread.domain.model

data class CommentModel(
    var id: Int,
    var userName: String,
    var commentTime: String = "2 hours ago",
    var voteNumber: Int,
    var profileImageUrl: String,
    var voteType: VoteType = VoteType.CLEAR,
    var commentText: String
)