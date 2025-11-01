package com.pthw.food.expects

import android.util.Log

actual object Logger {

    actual fun e(message: String, throwable: Throwable?) {
        if (throwable != null) {
            Log.e("", message, throwable)
        } else {
            Log.e("", message)
        }
    }

    actual fun d(message: String) {
        Log.d("", message)
    }

    actual fun i(message: String) {
        Log.i("", message)
    }
}