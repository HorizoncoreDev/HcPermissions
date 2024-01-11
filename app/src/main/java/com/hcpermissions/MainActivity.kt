package com.hcpermissions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionManager = PermissionManager()
        permissionManager.requestCameraPermission(this) { granted ->
            if (granted) {
                // Camera permission granted, proceed with camera usage
            } else {
                // Handle permission denial
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager().onRequestPermissionsResult(requestCode, permissions, grantResults) { granted ->
            // Handle permission results here
        }
    }
}