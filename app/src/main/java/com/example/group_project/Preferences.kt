package com.example.group_project

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Preferences private constructor(context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: Preferences? = null

        fun initialize(context: Context) {
            INSTANCE = Preferences(context.applicationContext)
        }

        fun getInstance(): Preferences {
            return INSTANCE ?: throw IllegalStateException("Preferences must be initialized first.")
        }
    }

    private val context: Context = context.applicationContext

    // Stateful data
    private var searchHistory: Array<String> = emptyArray()
    private var favoriteList: Array<Palette> = emptyArray()



    // Return favorite list
    fun getFavoritePaletteList(): Array<Palette> {
        return favoriteList
    }

    // Return search history
    fun getSearchHistory(): Array<String> {
        return searchHistory
    }

    // Empties favorite list
    fun emptyFavoriteList() {
        favoriteList = emptyArray()
    }

    // Empties search history
    fun emptySaveHistory() {
        searchHistory = emptyArray()
    }

    // Add a palette to favorite list
    fun addPaletteToFavorites(palette: Palette) {
        favoriteList += palette
    }

    // Add a palette string search to search history
    fun addStringToSearchHistory(str: String) {
        searchHistory += str
    }

    // Save the favorite list as a JSON string as persistent local data
    fun saveFavoriteList(): String {
        val root = JSONArray()
        favoriteList.forEach { root.put(getPaletteJSON(it)) }

        return try {
            val pref: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            pref.edit().putString("FAVORITES", root.toString()).apply()
            root.toString()
        } catch (e: Exception) {
            "false"
        }
    }

    // Add any saved palettes to the current favorite list
    fun loadFavoritePalette(): String {
        val pref: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val lst: String? = pref.getString("FAVORITES", "")

        if (lst.isNullOrEmpty()) return favoriteList.contentToString()

        try {
            val lstAsJSONArray = JSONArray(lst)
            val loadedList = mutableListOf<Palette>()

            for (i in 0 until lstAsJSONArray.length()) {
                val paletteObj = lstAsJSONArray.getJSONObject(i)
                val paletteName = paletteObj.keys().next()
                val palette = Palette(emptyArray(), paletteName)

                val colorsJSONArray = paletteObj.getJSONArray(paletteName)
                for (x in 0 until colorsJSONArray.length()) {
                    val colorInfo = colorsJSONArray.getJSONObject(x)
                    val color = Couleur(colorInfo.getString("color"), colorInfo.getString("name"))
                    palette.addCouleurToPalette(color)
                }
                loadedList.add(palette)
            }
            favoriteList = loadedList.toTypedArray()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return favoriteList.contentToString()
    }

    // Given a palette turn into a JSONObject
    private fun getPaletteJSON(palette: Palette): JSONObject {
        val paletteInfo = JSONObject()
        val colorInfo = JSONArray()

        palette.getPalette().forEach { color ->
            JSONObject().apply {
                put("name", color.getName())
                put("color", color.getColorHexCode())
            }.also { colorInfo.put(it) }
        }
        paletteInfo.put(palette.getPaletteName(), colorInfo)
        return paletteInfo
    }

    // Save search history
    fun saveSearchHistory(): String {
        val root = JSONArray(searchHistory)
        return try {
            val pref: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
            pref.edit().putString("SEARCH_HISTORY", root.toString()).apply()
            root.toString()
        } catch (e: Exception) {
            "false"
        }
    }

    // Load search history
    fun loadSearchHistory(): String {
        val pref: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val lst: String? = pref.getString("SEARCH_HISTORY", "")

        if (lst.isNullOrEmpty()) return searchHistory.contentToString()

        try {
            val lstAsJSONArray = JSONArray(lst)
            val loadedHistory = mutableListOf<String>()
            for (i in 0 until lstAsJSONArray.length()) {
                loadedHistory.add(lstAsJSONArray.getString(i))
            }
            searchHistory = loadedHistory.toTypedArray()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return searchHistory.contentToString()
    }
}