# HcPermissions

HcPermissions is an Android library that simplifies permission handling in Android applications. It
provides version-specific methods to request and check permissions, making it easier to manage the
changes in the Android permission system across different API levels.

## Key Features

- Version-wise permission handling.
- Easy-to-use API for requesting and checking permissions.

## Getting Started

### Installation

Add the following dependency to your app's `build.gradle` file:

```gradle

dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

implementation 'com.github.AlpeshSolanki2001:HcPermissions:latest-version'
```

### Usage

- Example: Selecting an Image from Gallery
    - I have implemented an example for select image from gallery

1. Add Required Permissions in Manifest:
    - Add the necessary permissions in your AndroidManifest.xml file

2. Implement in Your Activity:

```
 private lateinit var resultLauncher: ActivityResultLauncher<Intent>
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
            selectImageFromGallery(permissionManager, resultLauncher)
        }
 
 private fun selectImageFromGallery(
        permissionManager: PermissionManager, resultLauncher: ActivityResultLauncher<Intent>
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
            requestCode, permissions, grantResults
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
 
```

