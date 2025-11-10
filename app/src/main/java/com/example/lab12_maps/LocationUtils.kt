package com.example.lab12_maps

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
suspend fun FusedLocationProviderClient.awaitLastLocation(): Location? =
    suspendCancellableCoroutine { cont ->
        lastLocation.addOnSuccessListener { loc ->
            cont.resume(loc)
        }.addOnFailureListener {
            cont.resume(null)
        }
    }
