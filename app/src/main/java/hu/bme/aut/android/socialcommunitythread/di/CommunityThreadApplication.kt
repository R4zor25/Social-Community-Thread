package hu.bme.aut.android.socialcommunitythread.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.socialcommunitythread.domain.auth.AuthApi
import hu.bme.aut.android.socialcommunitythread.domain.auth.AuthRepository
import hu.bme.aut.android.socialcommunitythread.domain.auth.AuthRepositoryImplementation
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@HiltAndroidApp
class CommunityThreadApplication : Application() {
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder().baseUrl("http://152.66.181.24:8080/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi, preferences: SharedPreferences): AuthRepository {
        return AuthRepositoryImplementation(api, preferences)
    }
}
