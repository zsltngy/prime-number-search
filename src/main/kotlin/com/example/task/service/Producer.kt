package com.example.task.service

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.atomic.AtomicInteger

class Producer(private val dataQueue: DataQueue) : Runnable {
    private var logger = KotlinLogging.logger {}
    private var running: Boolean = false
    private var counter: AtomicInteger = AtomicInteger(0)

    override fun run() {
        running = true
        produce()
    }

    fun stop() {
        running = false
    }

    private fun produce() {
        logger.info{ "Producer started thread id: ${Thread.currentThread().id}" }
        while (running) {
            if (dataQueue.isFull()) {
                try {
                    dataQueue.waitIsNotFull()
                } catch (e: InterruptedException) {
                    logger.error(e) { "Error while waiting to produce number" }
                }
            }

            if (!running) {
                break;
            }
            val generatedNumber = generateNumber()
            dataQueue.add(generatedNumber)

            sleep((Math.random() * 10).toLong())
        }
        logger.info{ "Producer stopped" }
    }

    private fun generateNumber(): Int {
        return counter.incrementAndGet()
    }
}