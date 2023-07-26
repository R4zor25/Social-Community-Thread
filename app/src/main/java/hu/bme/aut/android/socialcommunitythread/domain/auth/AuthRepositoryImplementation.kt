package hu.bme.aut.android.socialcommunitythread.domain.auth

import android.content.SharedPreferences
import retrofit2.HttpException

class AuthRepositoryImplementation(
    private val api: AuthApi,
    private val preferences: SharedPreferences
    ): AuthRepository {
    override suspend fun signUp(username: String, email: String, password: String): LoginResult<Unit> {
        return try {
            api.signUp(
                request = AuthRequest(
                    username = username,
                    email = email,
                    password = password
                )
            )
            signIn(username, password)
        }catch (e:HttpException){
            if(e.code() == 401){
                LoginResult.Unauthorized()
            }else{
                LoginResult.UnknownError()
            }
        }catch (e:java.lang.Exception){
            LoginResult.UnknownError()
        }
    }

    override suspend fun signIn(username: String, password: String): LoginResult<Unit> {
        return try {
            val response = api.signIn(
                request = LoginRequest(
                    username = username,
                    password = password
                )
            )
            preferences.edit().putString("jwt", response.token)
                .apply()
            LoginResult.Authorized()
        }catch (e:HttpException){
            if(e.code() == 401){
                LoginResult.Unauthorized()
            }else{
                LoginResult.UnknownError()
            }
        }catch (e:Exception){
            LoginResult.UnknownError()
        }
    }

    override suspend fun authenticate(): LoginResult<Unit> {
        return try {
           val token = preferences.getString("jwt", null) ?: return LoginResult.Unauthorized()
            api.authenticate("Bearer $token")
            LoginResult.Authorized()
        }catch (e:HttpException){
            if(e.code() == 401){
                LoginResult.Unauthorized()
            }else{
                LoginResult.UnknownError()
            }
        }catch (e:Exception){
            LoginResult.UnknownError()
        }
    }

}