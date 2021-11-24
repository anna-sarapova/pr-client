package com.server.kitchen

import com.server.kitchen.models.BoardItem
import com.server.kitchen.models.FoodItem
import com.server.kitchen.services.Kitchen
import org.slf4j.LoggerFactory

class Cook(private val cookId: Int, private val rank: Int, private val proficiency: Int, private val cook_name: String,
           private val catch_phrase: String, private val kitchen: Kitchen, private val url: String?,
           private val menu: List<FoodItem>): Thread() {

    private val logger = LoggerFactory.getLogger(Cook::class.java)
    private val lazyFactor: Int = 2

    override fun run() {
        logger.info("${currentThread()} has run. Cook $cookId is starting his work!\tproficiency: ${proficiency}\trank: ${rank}")
        while (true){
            try {
                val boardItems = kitchen.getOrderFromBoardOrderList(rank, proficiency, cookId)
                if(boardItems != null) {
                    prepareOrders(boardItems)
                }
            } catch (e: Exception) {
                logger.info("Ups, something went wrong, I don't know what: " + e.message)
            }
        }
    }

    private fun prepareOrders(boardItems: MutableList<BoardItem> ) {
        for (boardItem in boardItems) {
            if(menu[boardItem.foodItemId - 1].`cooking-apparatus` != null) {
                kitchen.useApparatus(menu[boardItem.foodItemId - 1].`preparation-time`.toLong(), menu[boardItem.foodItemId - 1].`cooking-apparatus`)
            } else {
                sleep(menu[boardItem.foodItemId - 1].`preparation-time`.toLong())
            }

            kitchen.addOrderToProcessedOrdersWhichAreDoneFoodConvertedToDistributedType(boardItem, cookId)
            // @Synchronized - should be.
            // find order from orderList by boardItem's orderId
            // add to the Distribution.cooking_details the item that was just done
            // after add check if Distribution.items.size() == Distribution.cooking_details.size()
            // if equal construct the distribution distributeOrders(orderId, System.currentMillis() - timestampHandlers[get the index of current orderId].receivedTime)
        }
    }
}