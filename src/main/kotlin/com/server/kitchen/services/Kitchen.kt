package com.server.kitchen.services

import com.server.kitchen.Cook
import com.server.kitchen.CookingApparatus
import com.server.kitchen.models.*
import com.server.kitchen.utils.Utils
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.function.Predicate

@Service
class Kitchen {

    private val logger = LoggerFactory.getLogger(Kitchen::class.java)

    private val url: String = "localhost"

    private var cookSize: Int = 2
    private var stoveSize: Int = 0
    private var ovenSize: Int = 0

    private var cooks: MutableList<Cook> = arrayListOf()
    private var orderList: MutableList<Order> = arrayListOf()
    private var progressDistributionList: MutableList<Distribution> = arrayListOf()
    private var orderItemBoard = HashMap<Int, MutableList<BoardItem>>()
    private var ovenList: MutableList<CookingApparatus> = arrayListOf()
    private var stoveList: MutableList<CookingApparatus> = arrayListOf()

    private var menu: List<FoodItem> = Utils.getMeThatMenu()

    init {
        for(i in 0..cookSize) {
            cooks.add(Cook(i, (i % 3) + 1, (i % 3) + 1, "Cook${i}", "Let this food be prepared!", this, url, menu))
//            cooks.add(Cook(i, 3, 3, "Cook${i}", "Let this food be prepared!", this, url, menu))
            cooks[i].start()
        }
        for(i in 0..ovenSize) {
            ovenList.add(CookingApparatus("Oven"))
            ovenList[i].start()
        }
        for(i in 0..stoveSize) {
            stoveList.add(CookingApparatus("Stove"))
            stoveList[i].start()
        }
        for(i in 1..3) {
            orderItemBoard[i] = arrayListOf()
        }
    }

    fun addWaitingOrderList(order: Order) {
        distributeOrderBoard(order.items, order.order_id)
//        printList(orderItemBoard[1])
//        printList(orderItemBoard[2])
//        printList(orderItemBoard[3])
        orderList.add(order)

        progressDistributionList.add(Distribution(order.order_id, order.table_id,
                order.waiter_id, order.items, order.priority,
                order.max_wait, System.currentTimeMillis()))
    }

    private fun distributeOrderBoard(items: MutableList<Int>, orderId: Int) {
        for (item in items) {
            val foodItem = menu[item - 1]
            orderItemBoard[foodItem.complexity]?.add(BoardItem(orderId, foodItem.id))
//            println("Added food item" + orderItemBoard)
//            orderItemBoard[foodItem.complexity]?.remove(BoardItem(orderId, foodItem.id))
//            println("Removed food item" + orderItemBoard)
        }
    }

    @Synchronized
    fun getOrderFromBoardOrderList(rank: Int, proficiency: Int, cookId: Int): MutableList<BoardItem>? {
        var cookPreparationList: MutableList<BoardItem> = arrayListOf()
        var key = 0
        var itemsToTake = proficiency
        while (itemsToTake > 0) {
            //SAFETY
            if(key == rank) break;
            if (!orderItemBoard[rank - key].isNullOrEmpty()) {
                orderItemBoard[rank - key]?.get(0)?.let { cookPreparationList.add(it) }
                // Remove the first element we encounter with the order id we just added
                orderItemBoard[rank - key]?.removeAt(0)
                itemsToTake--
//                println(cookPreparationList)
            } else {
                key++
            }
        }
//        print(" ATTENTION MY (${cookId}) ITEM LIST: ")
//        printList(cookPreparationList)
//        print("\n")
        return cookPreparationList
    }

    @Synchronized
    fun addOrderToProcessedOrdersWhichAreDoneFoodConvertedToDistributedType(boardItem: BoardItem, cookId: Int) {
        val distribution: Distribution? = progressDistributionList.find { it.order_id == boardItem.orderId }
        distribution?.cooking_details?.add(CookingDetail(boardItem.foodItemId, cookId))

        if (distribution?.items?.size == distribution?.cooking_details?.size) {
            if (distribution != null) {
                logger.info("!!!!!!!  Order with id ${distribution.order_id} is done, sending to dinninghall")
                distribution.cooking_time = System.currentTimeMillis() - distribution.pick_up_time
                distributeOrders(distribution)
            }
            val condition: Predicate<Order> = Predicate<Order> { order -> order.order_id == boardItem.orderId }
            progressDistributionList.remove(distribution)
            orderList.removeIf(condition)
        }
    }


    private fun distributeOrders(distribution: Distribution) {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
                .uri(URI.create("http://${url}:8081/distribution"))
                .POST(HttpRequest.BodyPublishers.ofString(distribution.toJSON()))
                .header("Content-Type", "application/json")
                .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString());
        logger.info(response.body())
    }

    // DEBUG HELPERS
    fun printList(mutableList: MutableList<BoardItem>?) {
        if (mutableList != null) {
            for (mutable in mutableList) {
                print(mutable.foodItemId.toString() + ", ")
            }
        }
        print("\n")
    }

    fun useApparatus(time: Long, apparatus: String?) {
        if (apparatus == "oven") {
            ovenList[(0..ovenSize).random()].useApparatus(time)
        } else if (apparatus == "stove") {
            stoveList[(0..stoveSize).random()].useApparatus(time)
        }
    }

    @Scheduled(fixedDelay = 5000)
    fun statusUpdate () {
        print("Current order list of IDs: ")
        for (order in orderList) {
            print(order.order_id.toString() + ", ")
        }
        print("\n")
    }
}
