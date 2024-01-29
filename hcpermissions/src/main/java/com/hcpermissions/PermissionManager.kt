package com.hcpermissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat

class PermissionManager {

    fun requestCameraPermission(activity: Activity, callback: (granted: Boolean) -> Unit) {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        requestPermissions(activity, permissions, callback)
    }

    fun requestLocationPermission(activity: Activity, callback: (granted: Boolean) -> Unit) {
        val permissions: Array<String> =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // For Android 13 and above, use NEARBY_WIFI_DEVICES for Wi-Fi related tasks
                arrayOf(Manifest.permission.NEARBY_WIFI_DEVICES)
            } else {
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        requestPermissions(activity, permissions, callback)
    }

    // Function for requesting storage permissions (Android 12 and below)
    fun requestStoragePermissions(activity: Activity, callback: (granted: Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            requestPermissions(activity, permissions, callback)
        } else {
            // For Android 13 and above, handle scoped storage permissions
            callback(true) // Assume granted for now, handle scoped storage logic separately
        }
    }

    // Function for requesting phone state permissions (Android 13 and above)
    fun requestPhoneStatePermission(activity: Activity, callback: (granted: Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissions = arrayOf(Manifest.permission.READ_PHONE_NUMBERS)
            requestPermissions(activity, permissions, callback)
        } else {
            // For Android 12 and below, use READ_PHONE_STATE
            val permissions = arrayOf(Manifest.permission.READ_PHONE_STATE)
            requestPermissions(activity, permissions, callback)
        }
    }

    // Function for requesting contacts permissions
    fun requestContactsPermissions(activity: Activity, callback: (granted: Boolean) -> Unit) {
        val permissions =
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
        requestPermissions(activity, permissions, callback)
    }

    // Function for requesting calendar permissions
    fun requestCalendarPermissions(activity: Activity, callback: (granted: Boolean) -> Unit) {
        val permissions =
            arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
        requestPermissions(activity, permissions, callback)
    }

    // Function for requesting microphone permissions
    fun requestMicrophonePermission(activity: Activity, callback: (granted: Boolean) -> Unit) {
        val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
        requestPermissions(activity, permissions, callback)
    }

    private fun requestPermissions(
        activity: Activity, permissions: Array<String>, callback: (granted: Boolean) -> Unit
    ) {
        if (checkPermissions(activity, permissions)) {
            callback(true)
        } else {
            ActivityCompat.requestPermissions(activity, permissions, 100)
        }
    }

    private fun checkPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    context, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        callback: (granted: Boolean) -> Unit
    ) {
        if (requestCode == 100 && grantResults.isNotEmpty()) {
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            callback(allGranted)
        }


    }
}

