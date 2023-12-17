package hu.bme.aut.android.socialcommunitythread.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.util.Base64
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.socialcommunitythread.di.GsonHelper.customGson
import hu.bme.aut.android.socialcommunitythread.domain.interactors.AuthInteractor
import hu.bme.aut.android.socialcommunitythread.domain.interactors.ChatInteractor
import hu.bme.aut.android.socialcommunitythread.domain.interactors.FriendInteractor
import hu.bme.aut.android.socialcommunitythread.domain.interactors.ThreadInteractor
import hu.bme.aut.android.socialcommunitythread.network.BackendService
import hu.bme.aut.android.socialcommunitythread.network.IBackendService
import hu.bme.aut.android.socialcommunitythread.network.IRetrofitService
import hu.bme.aut.android.socialcommunitythread.network.ignoreAllSSLErrors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.lang.reflect.Type
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@HiltAndroidApp
class CommunityThreadApplication : Application()


object GsonHelper {
    val customGson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        .registerTypeHierarchyAdapter(
            ByteArray::class.java,
            ByteArrayToBase64TypeAdapter()
        ).create()

    // Using Android's base64 libraries. This can be replaced with any base64 library.
    private class ByteArrayToBase64TypeAdapter : JsonSerializer<ByteArray?>, JsonDeserializer<ByteArray?> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): ByteArray {
            return Base64.decode(json.asString, Base64.NO_WRAP)
        }

        override fun serialize(src: ByteArray?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP))
        }
    }
}

object DateFormatter {
    fun format(date: Date): String {
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return df.format(date)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideRetrofitService(): IRetrofitService {
        val intercepter = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        return Retrofit.Builder()
            .baseUrl("https://152.66.181.18:8765")
            .client(
                OkHttpClient.Builder()
                    .ignoreAllSSLErrors()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(intercepter)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .build()
            .create()
    }


    @Provides
    @Singleton
    fun provideBackendService(api: IRetrofitService, preferences: SharedPreferences): IBackendService {
        return BackendService(api, preferences)
    }

    @Provides
    @Singleton
    fun provideAuthInteractor(backendService: BackendService): AuthInteractor {
        return AuthInteractor(backendService)
    }

    @Provides
    @Singleton
    fun provideFriendInteractor(backendService: BackendService, authInteractor: AuthInteractor): FriendInteractor {
        return FriendInteractor(backendService, authInteractor)
    }

    @Provides
    @Singleton
    fun provideThreadInteractor(backendService: BackendService, authInteractor: AuthInteractor): ThreadInteractor {
        return ThreadInteractor(backendService, authInteractor)
    }

    @Provides
    @Singleton
    fun provideChatInteractor(backendService: BackendService, authInteractor: AuthInteractor): ChatInteractor {
        return ChatInteractor(backendService, authInteractor)
    }
}
