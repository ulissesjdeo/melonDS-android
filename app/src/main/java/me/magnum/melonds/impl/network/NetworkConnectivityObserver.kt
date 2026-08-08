package me.magnum.melonds.impl.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityObserver @Inject constructor(@ApplicationContext context: Context) {

    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    enum class NetworkState {
        CONNECTED,
        DISCONNECTED,
    }

    val networkState: Flow<NetworkState> = callbackFlow {
        val connectivityManager = connectivityManager
        if (connectivityManager == null) {
            trySend(NetworkState.DISCONNECTED)
            close()
            return@callbackFlow
        }
        val activeNetwork = connectivityManager.activeNetwork
        if (activeNetwork != null) {
            val activeNetworkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            if (activeNetworkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true) {
                trySend(NetworkState.CONNECTED)
            } else {
                trySend(NetworkState.DISCONNECTED)
            }
        } else {
            trySend(NetworkState.DISCONNECTED)
        }

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                val hasInternetConnection = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                if (hasInternetConnection) {
                    trySend(NetworkState.CONNECTED)
                } else {
                    trySend(NetworkState.DISCONNECTED)
                }
            }

            override fun onLost(network: Network) {
                trySend(NetworkState.DISCONNECTED)
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}
