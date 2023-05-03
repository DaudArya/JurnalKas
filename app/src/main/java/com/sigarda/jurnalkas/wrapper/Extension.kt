package com.sigarda.jurnalkas.wrapper

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Extension {

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun String.isPasswordValid(): Boolean {
        return !TextUtils.isEmpty(this) && this.length >= 6
    }

    fun String.isUsernameValid(): Boolean {
        return !TextUtils.isEmpty(this) && this.length >= 3
    }

    fun Context.showLongToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    fun Context.loadImage(url: Any, into: ImageView) {
        Glide.with(this)
            .load(url)
            .into(into)
    }

    fun View.showSnackbar(msg: String) {
        Snackbar.make(this, msg, 2500).show()
    }
    fun View.showSnackbarWithButton(msg: String, navigation:()-> Unit) {
        Snackbar.make(this, msg, 10000).setAction("Ok", View.OnClickListener {
            navigation()
        }).show()
    }


    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun Date.toFormat(dateFormat: String): String {
        return SimpleDateFormat(dateFormat, Locale.getDefault()).format(this)
    }
}
