package com.server.kitchen

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.random.Random

@Component
class Scheduler {
    private val logger = LoggerFactory.getLogger(Scheduler::class.java)

    @Value("\${app.url}")
    private val url: String? = null

    @Scheduled(fixedRate = 5000)
    fun fixedRateScheduledTask() {

        logger.info("STARTING TO CALL OTHER CONTAINER BRO")
        logger.info(url)
        val client = HttpClient.newBuilder().build();

        val data = """
        {
            "order_id": ${Random.nextInt(0, 100)},
            "table_id": 1,
            "waiter_id": 1,
            "items": [ 3, 4, 4, 2 ],
            "priority": 3,
            "max_wait": 45,
            "pick_up_time": 1631453140,
            "cooking_time": 65,
            "cooking_details": [
                {
                    "food_id": 2,
                    "cook_id": 3
                }
            ]
        }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
                .uri(URI.create("http://${url}:8081/distribution"))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .header("Content-Type", "application/json")
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info(response.body())

        return
    }
}