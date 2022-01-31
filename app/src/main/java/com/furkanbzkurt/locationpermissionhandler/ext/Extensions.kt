package com.furkanbzkurt.locationpermissionhandler.ext

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog

/**
 * Created by Furkan Bozkurt on 31.01.2022.
 */

fun Fragment.showToast(message: String) =
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun Fragment.showWarningDialogWithAction(
    title: String,
    message: String,
    positiveButton: String,
    isCancellable: Boolean,
    actionDone: () -> Unit
) {
    val dialog = MaterialDialog(requireContext())
    dialog.title(text = title)
    dialog.message(text = message)
    dialog.positiveButton(text = positiveButton)
    dialog.cancelable(isCancellable)
    dialog.positiveButton {
        actionDone()
    }
    dialog.show()
}

fun Fragment.openLocationSettings() {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    startActivity(intent)
}