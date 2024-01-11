package com.hcpermissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

class PermissionManager {

    fun requestCameraPermission(activity: Activity, callback: (granted: Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and above, no permission needed for camera
            callback(true)
            return
        }

        val permissions = arrayOf(Manifest.permission.CAMERA)
        requestPermissions(activity, permissions, callback)
    }

    fun requestLocationPermission(activity: Activity, callback: (granted: Boolean) -> Unit) {
        val permissions: Array<String>
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and above, use NEARBY_WIFI_DEVICES for Wi-Fi related tasks
            permissions = arrayOf(Manifest.permission.NEARBY_WIFI_DEVICES)
        } else {
            permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        requestPermissions(activity, permissions, callback)
    }

    private fun requestPermissions(activity: Activity, permissions: Array<String>, callback: (granted: Boolean) -> Unit) {
        if (checkPermissions(activity, permissions)) {
            callback(true)
        } else {
            ActivityCompat.requestPermissions(activity, permissions, 100)
        }
    }

    private fun checkPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, callback: (granted: Boolean) -> Unit) {
        if (requestCode == 100 && grantResults.isNotEmpty()) {
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            callback(allGranted)
        }
    }
}

