package me.magnum.melonds.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import me.magnum.melonds.domain.services.UpdateInstallManager
import me.magnum.melonds.github.GitHubApi
import me.magnum.melonds.github.services.GitHubUpdateInstallManager
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GitHubModule {
    @Provides
    @Singleton
    fun provideGitHubOkHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    @Singleton
    fun provideGitHubApi(client: OkHttpClient, json: Json): GitHubApi {
        return GitHubApi(client, json)
    }

    @Provides
    @Singleton
    fun provideUpdateInstallManager(@ApplicationContext context: Context): UpdateInstallManager {
        return GitHubUpdateInstallManager(context)
    }
}
