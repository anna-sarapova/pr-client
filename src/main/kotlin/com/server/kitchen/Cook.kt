package com.server.kitchen

import com.server.kitchen.models.Distribution
import com.server.kitchen.models.FoodItem
import com.server.kitchen.services.Kitchen
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Cook(private val cookId: Int, private val rank: Int, private val proficiency: Int, private val cook_name: String,
           private val catch_phrase: String, private val kitchen: Kitchen, private val url: String?,
           private val menu: List<FoodItem>): Thread() {

    private val logger = LoggerFactory.getLogger(Cook::class.java)
    private val lazyFactor: Int = 2

    override fun run() {
        logger.info("${currentThread()} has run. Cook $cookId is starting his work!\tproficiency: ${proficiency}\trank: ${rank}")
        while (true){
            sleep(20000)
            try {
                val order = kitchen.getOrderFromBoardOrderList(rank, proficiency, cookId)
                if(order != null) {

                }
            } catch (e: Exception) {
                logger.info("Ups, something went wrong, I don't know what: " + e.message)
            }
        }
    }

    private fun distributeOrders(client: HttpClient, distribution: Distribution) {
        val request = HttpRequest.newBuilder()
                .uri(URI.create("http://${url}:8080/distribution"))
                .POST(HttpRequest.BodyPublishers.ofString(distribution.toJSON()))
                .header("Content-Type", "application/json")
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}