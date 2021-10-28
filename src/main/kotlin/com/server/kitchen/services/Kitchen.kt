package com.server.kitchen.services

import com.server.kitchen.Cook
import com.server.kitchen.models.BoardOrder
import com.server.kitchen.models.FoodItem
import com.server.kitchen.models.Order
import com.server.kitchen.utils.Utils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class Kitchen {

    private val logger = LoggerFactory.getLogger(Kitchen::class.java)

    private val url: String = "localhost"

    private var cooks: MutableList<Cook> = arrayListOf()
    private var cookSize: Int = 2
    private var orderList: MutableList<Order> = arrayListOf()
    private var doneOrderList: MutableList<Int> = arrayListOf()
    private var orderBoard = HashMap<Int, MutableList<BoardOrder>>()
    private val lazyFactor: Int = 4
    private var menu: List<FoodItem> = Utils.getMeThatMenu()


    init {
        for(i in 0..cookSize) {
            cooks.add(Cook(i, 3, (1..4).random(), "Cook${i}", "Let this food be prepared!", this, url, menu))
            cooks[i].start()
        }
        for(i in 1..3) {
            orderBoard[i] = arrayListOf()
        }
    }

    fun addWaitingOrderList(order: Order) {
        distributeOrderBoard(order.items, order.order_id)
        orderList.add(order)
    }

    private fun distributeOrderBoard(items: MutableList<Int>, orderId: Int) {
        for (item in items) {
            val foodItem = menu[item - 1]
            orderBoard[foodItem.complexity]?.add(BoardOrder(orderId, foodItem.id))
        }
    }

    fun getOrderFromBoardOrderList(rank: Int): BoardOrder? {
        for (i in rank downTo 1) {
            if (!orderBoard[i].isNullOrEmpty()) {
                removeWaitingOrderList(orderBoard[i]?.get(0)?.orderId, i)
                return orderBoard[i]?.get(0)
            }
        }
        return null
    }

    fun removeWaitingOrderList(id: Int?, complexityKey: Int) {
        orderList.filter{
            (it.order_id == id)
        }
        orderBoard[complexityKey]?.filter {
            (it.orderId == id)
        }
    }

    fun getWaitingOrderList(): MutableList<Order> {
        return orderList
    }

    fun addDoneOrderList(id: Int) {
        doneOrderList.add(id)
    }

    // DEBUG HELPERS
    fun printList(mutableList: MutableList<BoardOrder>?) {
        if (mutableList != null) {
            for (mutable in mutableList) {
                print(mutable.foodItemId.toString() + ", ")
            }
        }
        print("\n")
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
