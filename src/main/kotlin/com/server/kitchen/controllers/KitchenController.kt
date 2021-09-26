package com.server.kitchen.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class KitchenController {

    @GetMapping("/ping")
    fun ping(): String {
        return "Hello, I am the kitchen, We're ready to serve you!"
    }
}