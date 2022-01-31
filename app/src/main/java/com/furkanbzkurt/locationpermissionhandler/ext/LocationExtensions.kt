package com.furkanbzkurt.locationpermissionhandler.ext

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

/**
 * Created by Furkan Bozkurt on 30.01.2022.
 */

// To use PRIORITY_HIGH_ACCURACY, you must have ACCESS_FINE_LOCATION permission.
// Any other priority will require just ACCESS_COARSE_LOCATION,
// but will not guarantee a location update
@ExperimentalCoroutinesApi
@SuppressLint("MissingPermission")
suspend fun FusedLocationProviderClient.awaitCurrentLocation(priority: Int): Location? {
    return suspendCancellableCoroutine {
        // to use for request cancellation upon coroutine cancellation
        val cts = CancellationTokenSource()
        getCurrentLocation(priority, cts.token)
            .addOnSuccessListener { location ->
                // remember location is nullable, this happens sometimes
                // when the request expires before an update is acquired
                it.resume(location) {
                    cts.cancel()
                }
            }.addOnFailureListener { e ->
                it.resumeWithException(e)
            }

        it.invokeOnCancellation {
            cts.cancel()
        }
    }
}

@ExperimentalCoroutinesApi
fun Fragment.isLocationEnabled(
    notEnabled: () -> Unit,
    onLocationReady: (Location) -> Unit,
    onLocationEmpty: () -> Unit
) {
    val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (LocationManagerCompat.isLocationEnabled(lm)) {
        // you can do this your own way, eg. from a viewModel
        // but here is where you wanna start the coroutine.
        // Choose your priority based on the permission you required
        val priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        lifecycleScope.launch {
            val location = LocationServices
                .getFusedLocationProviderClient(requireContext())
                .awaitCurrentLocation(priority)
            // do whatever with this location, notice that it's nullable
            location?.let {
                onLocationReady.invoke(it)
            } ?: kotlin.run {
                onLocationEmpty.invoke()
            }
        }
    } else {
        // prompt user to enable location or launch location settings check
        notEnabled.invoke()
    }
}