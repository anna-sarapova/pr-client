package com.server.kitchen.controllers

import com.server.kitchen.models.Distribution
import com.server.kitchen.models.Order
import com.server.kitchen.services.Kitchen
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class KitchenController(val kitchen: Kitchen) {

    private val logger = LoggerFactory.getLogger(KitchenController::class.java)

    @GetMapping("/ping")
    fun ping(): String {
        logger.info("LOG info")
        return "Hello, I am the kitchen, We're ready to serve you!"
    }

    @PostMapping("/order")
    fun order(@RequestBody order: Order) : String {
        logger.info("Order received")
        kitchen.addWaitingOrderList(order)
        return "Order with id: " + order.order_id + " has been received. To be processed..."
    }

    @PostMapping("/distribution")
    fun distribution(@RequestBody distribution: Distribution) : String {
        logger.info("Order has been done")
        return "Order with id: " + distribution.order_id + " it's ready to be eaten"
    }

}