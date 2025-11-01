package com.pthw.food.expects

import platform.Foundation.NSLog

actual object Logger {
    actual fun e(message: String, throwable: Throwable?) {

        if (throwable != null) {
            NSLog("ERROR: $message. Throwable: $throwable CAUSE ${throwable.cause}")
        } else {
            NSLog("ERROR: $message")
        }
    }

    actual fun d(message: String) {
        NSLog("DEBUG: $message")
    }

    actual fun i(message: String) {
        NSLog("INFO: $message")
    }
}