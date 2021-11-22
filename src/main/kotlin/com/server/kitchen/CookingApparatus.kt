package com.server.kitchen

import org.slf4j.LoggerFactory
import java.lang.Exception

class CookingApparatus(private val apparatusName: String) : Thread() {

    private val logger = LoggerFactory.getLogger(Cook::class.java)

    private var isOccupied = false

    override fun run() {
        logger.info("${apparatusName} is ready to be used")
    }

    fun occupyApparatus(occupy: Boolean, cookId: Int): Boolean {
        if (isOccupied && occupy) {
            // Apparatus is already occupied
            return false
        }
        if (occupy) {
            logger.info("${cookId} occupied the ${apparatusName}")
        } else {
            logger.info("${cookId} freed the ${apparatusName}")
        }
        isOccupied = occupy
        return isOccupied;
    }
}