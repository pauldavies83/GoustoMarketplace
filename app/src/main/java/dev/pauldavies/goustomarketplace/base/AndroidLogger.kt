package dev.pauldavies.goustomarketplace.base

import android.util.Log

interface Logger {
    fun debug(tag: String, message: String, throwable: Throwable?)
}

class AndroidLogger : Logger {
    override fun debug(tag: String, message: String, throwable: Throwable?) {
        if (throwable !=  null) {
            Log.d(tag, message, throwable)
        } else {
            Log.d(tag, message)
        }
    }
}