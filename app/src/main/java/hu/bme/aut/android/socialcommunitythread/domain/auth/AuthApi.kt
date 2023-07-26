package hu.bme.aut.android.socialcommunitythread.domain.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


//TODO KTORFY
interface AuthApi {

    @POST("signup")
    suspend fun signUp(@Body request: AuthRequest)

    @POST("signin")
    suspend fun signIn(@Body request: LoginRequest): TokenResponse

    @GET("authenticate")
    suspend fun authenticate(@Header("Authorization") token: String)
}