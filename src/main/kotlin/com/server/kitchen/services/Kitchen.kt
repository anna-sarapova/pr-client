package com.server.kitchen.services

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.server.kitchen.models.FoodItem
import com.server.kitchen.utils.Utils
import org.springframework.stereotype.Service
import javax.swing.tree.MutableTreeNode

@Service
class Kitchen {

    init {
        // Creating menu
        var menu: List<FoodItem> = Utils.getMeThatMenu()

    }
}