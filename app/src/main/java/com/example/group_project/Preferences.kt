package com.example.group_project

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class Preferences {

    private lateinit var context: Context

    // Search history & favorite list are initially empty
    private var searchHistory : Array<String> = emptyArray()
    private var favoriteList : Array<Palette> = emptyArray()


    constructor(context : Context) {
        this.context = context
    }

    // Return favorite list
    fun getFavoritePaletteList() : Array<Palette> {
        return favoriteList
    }

    // Return search history
    fun getSearchHistory() : Array<String> {
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
    fun addStringToSearchHistory( str : String) {
        searchHistory += str
    }

    // Save the favorite list as a JSON string as persistent local data
    fun saveFavoriteList() : String {
        var root : JSONArray = JSONArray()

        // For each palette in the list turn it into a JSONObject
        for(palette in favoriteList) {
            root.put(getPaletteJSON(palette))
        }

        // Try to save it as persistent data
        try {
            var pref : SharedPreferences =
                context.getSharedPreferences( context.packageName, Context.MODE_PRIVATE )

            var editor : SharedPreferences.Editor = pref.edit()
            editor.putString( "FAVORITES", root.toString())

            editor.commit()

            return root.toString()
        }
        catch (e : Exception) {
            return "false"
        }
    }

    // Add any saved palettes to the current favorite list
    fun loadFavoritePalette() : String {

        // Grab the array of palettes that are saved
        var pref : SharedPreferences =
            context.getSharedPreferences( context.packageName, Context.MODE_PRIVATE )

        var lst : String? = pref.getString("FAVORITES", "")
        var lstAsJSONArray : JSONArray = JSONArray(lst!!)

        // For each palette in the array, re-create the palette with its color and add it to the favorite list
        for (i in 0..lstAsJSONArray.length() - 1) {
            lateinit var palette: Palette
            lateinit var color : Color

            var palleteJSONObj =lstAsJSONArray.getJSONObject(i)
            var paletteName =  palleteJSONObj.keys().next()
            palette = Palette(emptyArray(), palleteJSONObj.keys().next())

            var colorsJSONArray  = palleteJSONObj.getJSONArray(paletteName)

            for (x in 0..colorsJSONArray.length() - 1) {
                var colorInfo : JSONObject = colorsJSONArray.getJSONObject(x)
                var color : Couleur = Couleur(colorInfo.getString("color"), colorInfo.getString("name"))

                palette.addCouleurToPalette(color)
            }

            favoriteList += palette

        }

        return favoriteList.contentToString()
    }


    // Given a palette turn into a JSONObject
    fun getPaletteJSON( palette : Palette) : JSONObject {
        val paletteArr = palette.getPalette()
        var paletteInfo : JSONObject = JSONObject()

        var colorInfo : JSONArray = JSONArray()
        for (c in 0..paletteArr.size - 1) {
            var color : JSONObject = JSONObject()

            color.put("name", paletteArr[c].getName())
            color.put("color", paletteArr[c].getColorHexCode())
            colorInfo.put(color)
        }

        paletteInfo.put(palette.getPaletteName(), colorInfo)

        return paletteInfo

    }

    // Add any saved string search to the current search history
    fun saveSearchHistory() : String {
        var root : JSONArray = JSONArray(searchHistory)

        try {
            var pref : SharedPreferences =
                context.getSharedPreferences( context.packageName, Context.MODE_PRIVATE )

            var editor : SharedPreferences.Editor = pref.edit()
            editor.putString( "SEARCH_HISTORY", root.toString())

            editor.commit()

            return root.toString()
        }
        catch (e : Exception) {
            return "false"
        }
    }

    // Load any saved string searches to the current search history
    fun loadSearchHistory() : String {
        var pref : SharedPreferences =
            context.getSharedPreferences( context.packageName, Context.MODE_PRIVATE )

        var lst : String? = pref.getString("SEARCH_HISTORY", "")
        var lstAsJSONArray : JSONArray = JSONArray(lst!!)

        for (i in 0..lstAsJSONArray.length() - 1) {
            searchHistory += lstAsJSONArray.getString(i)
        }

        return searchHistory.contentToString()
    }

}