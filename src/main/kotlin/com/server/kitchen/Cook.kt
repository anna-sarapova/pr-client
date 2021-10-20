package com.server.kitchen

import com.server.kitchen.models.Order
import com.server.kitchen.services.Kitchen
import org.slf4j.LoggerFactory
import java.lang.Exception

class Cook(val rank: Int, val proficiency: Int, val cook_name: String, val catch_phrase: String, private val kitchen: Kitchen): Thread() {

    private val logger = LoggerFactory.getLogger(Cook::class.java)
    private val cookId: Int = 0
    private val lazyFactor: Int = 2

    override fun run() {
        logger.info("${currentThread()} has run. Cook $cookId is starting his work!")
        while (true){
            try {
                var waitingOrder: MutableList<Order> = kitchen.getWaitingOrderList()

                for(item in waitingOrder) {
                    if (waitingOrder == null) {
                        Thread.sleep(((0..lazyFactor).random() * 1000).toLong())
                    } else {
                        kitchen.removeWaitingOrderList(item.order_id)
                        logger.info("The Cook is cooking, wait a bit..")
                        kitchen.addDoneOrderList(item.order_id)
                    }
                    println(kitchen.getWaitingOrderList())
                    println(kitchen.getDoneOrderList())
                }
            } catch (e: Exception) {
                logger.info("Ups, something went wrong, I don't know what")
            }
        }
    }
}