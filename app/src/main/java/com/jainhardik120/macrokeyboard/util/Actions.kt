package com.jainhardik120.macrokeyboard.util

import org.json.JSONArray
import org.json.JSONObject

sealed class Actions {
    data class StringInput(val string: String = "") : Actions()
    data class KeyCombos(val combos: List<Pair<Int, String>> = listOf()) : Actions()
    data class MouseMove(val x: Int = 0, val y: Int = 0) : Actions()
    data class MouseClick(var keyCode: Int = 0) : Actions()
    data class Delay(var delay: Int = 0) : Actions()

    fun toJsonString(): String {
        val jsonObject = JSONObject()
        when (this) {
            is Delay -> {
                jsonObject.put("delay", this.delay)
            }

            is KeyCombos -> {
                val jsonArray = JSONArray()
                for (i in this.combos) {
                    val tempObject = JSONObject()
                    tempObject.put("key", i.first)
                    tempObject.put("keyName", i.second)
                    jsonArray.put(tempObject)
                }
                jsonObject.put("keys", jsonArray)
            }

            is MouseClick -> {

            }

            is MouseMove -> {
                jsonObject.put("x", this.x)
                jsonObject.put("y", this.y)
            }

            is StringInput -> {
                jsonObject.put("string", this.string)
            }
        }
        return jsonObject.toString()
    }

    fun typeName(): String {
        return when (this) {
            is Delay -> "Delay"
            is KeyCombos -> "Keyboard Shortcut"
            is MouseClick -> "Mouse Key Click"
            is MouseMove -> "Move Mouse"
            is StringInput -> "String Input"
        }
    }

    fun actionTypeCode(): Int {
        return when (this) {
            is Delay -> 5
            is KeyCombos -> 2
            is MouseClick -> 4
            is MouseMove -> 3
            is StringInput -> 1
        }
    }
}

fun actionFromJson(actionType: Int, string: String): Actions? {
    val jsonObject = JSONObject(string)
    return when (actionType) {
        1 -> {
            Actions.StringInput(string = jsonObject.getString("string"))
        }

        2 -> {
            val array = jsonObject.getJSONArray("keys")
            val map = keyMaps()
            val list = List(array.length()) {
                val keyName = array.getJSONObject(it).getString("keyName") ?: ""
                Pair(map[keyName] ?: 0, keyName)
            }
            Actions.KeyCombos(combos = list)
        }

        3 -> {
            Actions.MouseMove(
                x = jsonObject.getInt("x"),
                y = jsonObject.getInt("y")
            )
        }

        4 -> {
            Actions.MouseClick(keyCode = jsonObject.getInt("button"))
        }

        5 -> {
            Actions.Delay(delay = jsonObject.getInt("delay"))
        }

        else -> {
            null
        }
    }
}