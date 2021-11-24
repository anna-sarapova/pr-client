package com.server.kitchen

import org.slf4j.LoggerFactory

class CookingApparatus(private val apparatusName: String) : Thread() {

    private val logger = LoggerFactory.getLogger(Cook::class.java)

    private var isOccupied = false

    override fun run() {
        logger.info("${apparatusName} is ready to be used")
    }

    @Synchronized
    fun useApparatus(timeToWait: Long) {
        sleep(timeToWait)
    }
}