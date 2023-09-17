package com.example.task.service

fun waitForAllThreadToComplete(threads: List<Thread>) {
    for (t in threads) {
        try {
            t.join();
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}

fun sleep(interval: Long) {
    try {
        Thread.sleep(interval)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}