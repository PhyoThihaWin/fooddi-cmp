package com.pthw.food

expect object Logger {
    fun e(message: String, throwable: Throwable? = null)
    fun d(message: String)
    fun i(message: String)

}