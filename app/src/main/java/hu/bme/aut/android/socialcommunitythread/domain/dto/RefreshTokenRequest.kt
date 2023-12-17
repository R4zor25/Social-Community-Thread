package hu.bme.aut.android.socialcommunitythread.domain.dto

import java.util.UUID

data class RefreshTokenRequest(
    var token : String = UUID.randomUUID().toString()
)