package hu.bme.aut.android.socialcommunitythread.domain.model

import hu.bme.aut.android.socialcommunitythread.domain.dto.AppUser
import hu.bme.aut.android.socialcommunitythread.domain.dto.ThreadPost
import hu.bme.aut.android.socialcommunitythread.domain.dto.VoteType
import java.util.Date

data class PersonalCommentModel(
    var id: Long = 0L,
    var threadPost : ThreadPost,
    var author : AppUser,
    var voteNumber: Int,
    var commentTime: Date = Date(),
    var voteType: VoteType = VoteType.CLEAR,
    var commentText: String
)

data class CommentModel(
    var id: Long = 0L,
    var threadPost : ThreadPost,
    var author : AppUser,
    var voteNumber: Int,
    var commentTime: Date = Date(),
    var commentText: String
)