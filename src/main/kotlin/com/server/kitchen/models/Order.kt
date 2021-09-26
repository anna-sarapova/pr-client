package com.server.kitchen.models

data class Order (
    val order_id: Int,
    val table_id: Int,
    val waiter_id: Int,
    val items: MutableList<Int>,
    val priority: Int,
    val max_wait: Int,
    val pick_up_time: Long,
)