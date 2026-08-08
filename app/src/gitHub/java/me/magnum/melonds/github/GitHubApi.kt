package me.magnum.melonds.github

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.Json
import me.magnum.melonds.github.dtos.ReleaseDto
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GitHubApi(
    private val client: OkHttpClient,
    private val json: Json,
) {
    companion object {
        private const val RELEASES_URL =
            "https://api.github.com/repos/rafaelvcaetano/melonDS-android/releases"
    }

    suspend fun getLatestRelease(): ReleaseDto = getRelease("$RELEASES_URL/latest")

    suspend fun getLatestNightlyRelease(): ReleaseDto =
        getRelease("$RELEASES_URL/tags/nightly-release")

    private suspend fun getRelease(url: String): ReleaseDto {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return client.newCall(request).await().use { response ->
            if (!response.isSuccessful) {
                throw IOException("GitHub request failed: ${response.code} ${response.message}")
            }
            json.decodeFromString(response.body.string())
        }
    }
}

private suspend fun Call.await(): Response = suspendCancellableCoroutine { continuation ->
    continuation.invokeOnCancellation { cancel() }
    enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            continuation.resumeWithException(e)
        }

        override fun onResponse(call: Call, response: Response) {
            continuation.resume(response) { _, cancelledResponse, _ ->
                cancelledResponse.close()
            }
        }
    })
}
