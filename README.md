
# Location Permission Handler

Easy way to check location permissions for Android 9/10/11 devices. Project can be integrated with your existing ones thanks to kotlin extension functions.





## Usage

#### In your fragment, to check any permissions (not only location permissions) you can call directly with :

```![#0a192f]http
fun Fragment.handlePermission(
    permission: AppPermission,
    onGranted: (AppPermission) -> Unit,
    onDenied: (AppPermission) -> Unit,
    onRationaleNeeded: ((AppPermission) -> Unit)? = null
)
```

#### AppPermission class takes two parameters :

```![#0a192f]http
sealed class AppPermission(
    val permissionName: String,
    val requestCode: Int
)
```

#### Example usage in fragments :

```![#0a192f]http
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    handlePermission(
        AppPermission.ACCESS_COARSE_LOCATION,
        onGranted = { checkDeviceLocationSetting() },
        onDenied = { requestPermission(AppPermission.ACCESS_COARSE_LOCATION) },
        onRationaleNeeded = {  showToast("onRationaleNeeded")}
    )
}
```

#### Afterwards override onRequestPermissionsResult() in fragment :

```![#0a192f]http
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
```

  
## Acknowledgement

- [@Ace](https://stackoverflow.com/a/66051728/8516127)'s answer on stackoverflow.
- [@amsterdatech](https://gist.github.com/amsterdatech/d2435828514c9f4efa12577926d0e5f0)'s gist file.

  
## To-Do

- Deprecated onRequestPermissionsResult will be replaced with registerForActivityResult()

  
## Screenshots

<a href="https://i.ibb.co/HYWB1Sp/location-1.png"><img src="https://i.ibb.co/HYWB1Sp/location-1.png" align="left" height="400" ></a>
<a href="2"><img src="https://i.ibb.co/C7mKZkG/location-2.png" align="left" height="400" ></a>
<a href="3"><img src="https://i.ibb.co/MpKLLST/location-3.png" align="left" height="400" ></a>

  
