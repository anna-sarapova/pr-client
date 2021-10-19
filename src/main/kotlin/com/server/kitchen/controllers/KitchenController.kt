package com.server.kitchen.controllers

import com.server.kitchen.models.Order
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpClient
import java.net.http.HttpResponse

@RestController
class KitchenController {

    private val logger = LoggerFactory.getLogger(KitchenController::class.java)

    @GetMapping("/ping")
    fun ping(): String {
        logger.info("LOG info")
        return "Hello, I am the kitchen, We're ready to serve you!"
    }

    @PostMapping("/order")
    fun order(@RequestBody order: Order) : String {
        logger.info("Order received")
        return "Order with id: " + order.order_id + " has been received. To be processed..."
    }
}