package com.furkanbzkurt.locationpermissionhandler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.furkanbzkurt.locationpermissionhandler.databinding.FragmentLocationPermissionBinding
import com.furkanbzkurt.locationpermissionhandler.ext.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class LocationPermissionFragment : Fragment() {

    lateinit var binding: FragmentLocationPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_permission, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {
        binding.buttonLocationPermission.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        handlePermission(
            AppPermission.ACCESS_COARSE_LOCATION,
            onGranted = { checkDeviceLocationSetting() },
            onDenied = { requestPermission(AppPermission.ACCESS_COARSE_LOCATION) },
            onRationaleNeeded = {
                showWarningDialogWithAction(
                    title = "location permission Rationale title",
                    message = "location permission rationale description",
                    positiveButton = "ok",
                    isCancellable = false,
                    actionDone = { requestPermission(AppPermission.ACCESS_COARSE_LOCATION) }
                )
            }
        )
    }

    private fun checkDeviceLocationSetting() {
        isLocationEnabled(
            onLocationReady = { location ->
                showToast("Lat: ${location.latitude},\nLong: ${location.longitude}")
            },
            onLocationEmpty = {
                showToast("location is empty")
            },
            notEnabled = {
                showWarningDialogWithAction(
                    title = "Attention",
                    message = "Location settings must be enabled from the settings to use the application",
                    positiveButton = "Open settings",
                    isCancellable = false,
                    actionDone = { openLocationSettings() }
                )
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        handlePermissionsResult(requestCode, permissions, grantResults,
            onPermissionGranted = { showToast("onPermissionGranted") },
            onPermissionDenied = { showToast("onPermissionDenied") },
            onPermissionDeniedPermanently = { showToast("onPermissionDeniedPermanently") }
        )
    }
}