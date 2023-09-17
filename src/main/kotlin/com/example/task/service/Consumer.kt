package com.example.task.service

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.ConcurrentSkipListSet

class Consumer(private val dataQueue: DataQueue, private val primeNumbers: ConcurrentSkipListSet<Int>) : Runnable {
    private var logger = KotlinLogging.logger {}
    private var running: Boolean = false

    override fun run() {
        running = true
        consume()
    }

    fun stop() {
        running = false
    }

    private fun consume() {
        logger.info{ "Consumer started thread id: ${Thread.currentThread().id}" }
        while (running) {
            if (dataQueue.isEmpty()) {
                try {
                    dataQueue.waitIsNotEmpty()
                } catch (e: InterruptedException) {
                    logger.error(e) { "Error while waiting for number" }
                    break
                }
            }

            if (!running) {
                break
            }

            val number = dataQueue.poll()

            if (number != null && isPrime(number)) {
                primeNumbers.add(number)
            }

            sleep((Math.random() * 100).toLong())
        }
        logger.info{ "Consumer stopped" }
    }

    private fun isPrime(number: Int): Boolean {
        if (number <= 1) {
            return false
        }

        for (i in 2 until number) {
            if (number % i == 0) {
                return false
            }
        }

        return true
    }
}