package com.server.kitchen.services

import com.server.kitchen.Cook
import com.server.kitchen.models.BoardItem
import com.server.kitchen.models.FoodItem
import com.server.kitchen.models.Order
import com.server.kitchen.utils.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import org.slf4j.LoggerFactory
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
    private var orderItemBoard = HashMap<Int, MutableList<BoardItem>>()
    private val lazyFactor: Int = 4
    private var menu: List<FoodItem> = Utils.getMeThatMenu()
//    private val mutex = ReentrantLock()
    private val mutex = Mutex()



    init {
        for(i in 0..cookSize) {
            cooks.add(Cook(i, (i % 3) + 1, (i % 3) + 1, "Cook${i}", "Let this food be prepared!", this, url, menu))
//            cooks.add(Cook(i, 3, 3, "Cook${i}", "Let this food be prepared!", this, url, menu))

            cooks[i].start()
        }
        for(i in 1..3) {
            orderItemBoard[i] = arrayListOf()
        }
    }

    fun addWaitingOrderList(order: Order) {
        distributeOrderBoard(order.items, order.order_id)
        printList(orderItemBoard[1])
        printList(orderItemBoard[2])
        printList(orderItemBoard[3])
        orderList.add(order)
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


        print(" ATTENTION MY (${cookId}) ITEM LIST: ")
        printList(cookPreparationList)
        print("\n")
        return cookPreparationList
    }

    fun removeEntries(id: Int?,complexityKey: Int){
//        val entrySet = HashMap<String, String>()
        orderItemBoard[complexityKey]?.removeIf() {
            it.foodItemId == id
        }
    }

//    fun removeBoardOrder(id: Int?, complexityKey: Int) {
//        var filteredMap = orderItemBoard[complexityKey]?.filter {
//            (it.foodItemId == id)
//        }
//    }

    fun getWaitingOrderList(): MutableList<Order> {
        return orderList
    }

    fun addDoneOrderList(id: Int) {
        doneOrderList.add(id)
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

    @Scheduled(fixedDelay = 5000)
    fun statusUpdate () {
        print("Current order list of IDs: ")
        for (order in orderList) {
            print(order.order_id.toString() + ", ")
        }
        print("\n")
    }
}
