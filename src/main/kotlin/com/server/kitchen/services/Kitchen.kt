package com.server.kitchen.services

import com.server.kitchen.Cook
import com.server.kitchen.models.FoodItem
import com.server.kitchen.models.Order
import com.server.kitchen.utils.Utils
import org.springframework.stereotype.Service

@Service
class Kitchen {

    private var cooks: MutableList<Cook> = arrayListOf()
    private var cookSize: Int = 2
    private var waitingOrderList: MutableList<Order> = arrayListOf()
    private var doneOrderList: MutableList<Int> = arrayListOf()

    private val lazyFactor: Int = 4

    private var menu: List<FoodItem> = Utils.getMeThatMenu()

    init {
        for(i in 0..cookSize) {
            cooks.add(Cook(3, 3, "Dom", "Let this food be prepared!", this))
            cooks[i].start()
        }
    }

    fun addWaitingOrderList(id: Order) {
        waitingOrderList.add(id)
        print(waitingOrderList)
    }
    fun removeWaitingOrderList(id:Int) {
        waitingOrderList.filter{
            (it.order_id == id)
        }
    }

    fun getWaitingOrderList(): MutableList<Order> {
        return waitingOrderList
    }

        fun addDoneOrderList(id: Int) {
        doneOrderList.add(id)
    }

    fun removeDoneOrderList(id:Int) {
        doneOrderList.remove(id)
    }

    fun getDoneOrderList(): MutableList<Int> {
        return doneOrderList
    }


}
