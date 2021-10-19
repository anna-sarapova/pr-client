package com.server.kitchen

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.server.kitchen.controllers", "com.server.kitchen.services"] )
class KitchenApplication

fun main(args: Array<String>) {
	runApplication<KitchenApplication>(*args)
}
