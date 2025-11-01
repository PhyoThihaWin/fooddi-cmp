package com.pthw.food.expects

expect object Logger {
    fun e(message: String, throwable: Throwable? = null)
    fun d(message: String)
    fun i(message: String)

}