package com.server.kitchen.controllers

import com.server.kitchen.models.Order
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class KitchenController {

    @GetMapping("/ping")
    fun ping(): String {
        return "Hello, I am the kitchen, We're ready to serve you!"
    }

    @PostMapping("/order")
    fun order(@RequestBody order: Order) : String {
        return "Order with id: " + order.order_id + " has been received. To be processed..."
    }
}