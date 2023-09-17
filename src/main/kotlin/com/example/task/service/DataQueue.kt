package com.example.task.service

import java.util.concurrent.ConcurrentLinkedQueue

class DataQueue(private val maxSize: Int) {
    private val queue: ConcurrentLinkedQueue<Int> = ConcurrentLinkedQueue()
    private val isNotFull = Object()
    private val isNotEmpty = Object()

    fun isFull(): Boolean {
        return queue.size == maxSize
    }

    fun isEmpty(): Boolean {
        return queue.isEmpty()
    }

    fun waitIsNotFull() {
        synchronized(isNotFull) {
            isNotFull.wait()
        }
    }

    fun waitIsNotEmpty() {
        synchronized(isNotEmpty) {
            isNotEmpty.wait();
        }
    }

    fun add(number: Int) {
        queue.add(number);
        notifyIsNotEmpty()
    }

    fun poll(): Int? {
        val number: Int? = queue.poll()
        notifyIsNotFull()
        return number
    }

    fun clear() {
        queue.clear()
    }

    fun notifyIsNotFull() {
        synchronized(isNotFull) {
            isNotFull.notify()
        }
    }

    fun notifyIsNotEmpty() {
        synchronized(isNotEmpty) {
            isNotEmpty.notify()
        }
    }
}