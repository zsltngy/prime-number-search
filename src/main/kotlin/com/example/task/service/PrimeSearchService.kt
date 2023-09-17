package com.example.task.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentSkipListSet

@Service
class PrimeSearchService {
    var isRunning: Boolean = false;
    private var threadNumber: Int = 1;
    private var primeNumbers: ConcurrentSkipListSet<Int> = ConcurrentSkipListSet()
    private var dataQueue: DataQueue = DataQueue(1000)
    private var threads: ArrayList<Thread> = ArrayList()
    private var producers: ArrayList<Producer> = ArrayList()
    private var consumers: ArrayList<Consumer> = ArrayList()

    fun start(threadNumber: Int) {
        this.threadNumber = threadNumber
        primeNumbers.clear()

        val producer = Producer(dataQueue)
        producers.add(producer)
        val producerThread = Thread(producer)
        producerThread.start()
        threads.add(producerThread)

        for (i in 1..threadNumber) {
            val consumer = Consumer(dataQueue, primeNumbers)
            consumers.add(consumer)
            val consumerThread = Thread(consumer)
            consumerThread.start()
            threads.add(consumerThread)
        }
        isRunning = true;
    }

    fun getPrimeNumberWithinTheInterval(start: Int, end: Int): List<Int> {
        return primeNumbers.filter { e -> (e in start..end) }
    }

    fun getLastFoundPrimeNumber(): Int {
        return if(!primeNumbers.isEmpty()) primeNumbers.last() else 0
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            for (consumer in consumers) {
                consumer.stop()
                dataQueue.notifyIsNotEmpty()
            }
            for (producer in producers) {
                producer.stop()
                dataQueue.notifyIsNotFull()
            }
            waitForAllThreadToComplete(threads)
            dataQueue.clear()
        }
    }
}