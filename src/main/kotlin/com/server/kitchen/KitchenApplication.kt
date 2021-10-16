package com.server.kitchen

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = ["com.server.kitchen.controllers", "com.server.kitchen"] )
class KitchenApplication

fun main(args: Array<String>) {
	runApplication<KitchenApplication>(*args)
}
