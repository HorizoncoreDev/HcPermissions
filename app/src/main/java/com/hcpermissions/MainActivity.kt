package com.hcpermissions

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageSelect = findViewById<TextView>(R.id.txt)
        val imageView = findViewById<ImageView>(R.id.imageview)

        val permissionManager = PermissionManager()

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val imageUri = data?.data  // Get the selected image URI
                    Glide.with(this).load(imageUri).into(imageView)
                }
            }

        imageSelect.setOnClickListener {
            selectImageFromGallery(permissionManager)
        }
    }

    private fun selectImageFromGallery(
        permissionManager: PermissionManager
    ) {
        permissionManager.requestStoragePermissions(this) { granted ->
            if (granted) {
                openGallery()
            } else {
                // Handle permission denial appropriately
                Toast.makeText(
                    this, "Storage permission required to select images.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager().onRequestPermissionsResult(
            requestCode, grantResults
        ) { granted ->
            // Handle permission results here
            if (granted) {
                // Camera permission granted, proceed with camera usage
                openGallery()
                Log.e("TAG", "onRequestPermissionsResult: $granted")
            } else {
                Log.e("TAG", "onRequestPermissionsResult: $granted")
            }
        }
    }

    private fun openGallery() {
        // Launch gallery intent to select image
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }
}