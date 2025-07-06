package com.example.group_project

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.LinearLayout

import java.lang.Integer
import androidx.core.graphics.toColorInt


// When you want to test stuff without affecting MainActivity please call it here and in MainActivity
//run your test.
//
// Note: if you want to call findViewById(), you can activity.findViewById()

class Tester {
    private lateinit var context: Context
    private lateinit var activity: MainActivity

    constructor( providedContext : Context) {
        context = providedContext
        activity = providedContext as MainActivity
    }

    // This is test #0, basic test to see if everything works
    fun test0() {
        Log.w("Tester", "Please do not blow up" )
    }

    // This is test #1 and does pallete generation
    // This code ws previously created by Neb in MainActivity to test the palette generation
    fun test1() {

        val utils = PaletteUtils()

        val testPaletteStrings = listOf(
            "red,blue,green,yellow",
            "(255,0,0),(0,255,0),(0,0,255),(255,255,0)",
            // biased average
            "_,_,red,_,_",
            "red,(123,45,67),green,(0,255,0),_,_",
            "(100,100,100),black,(50,50,50),white,gray,(200,200,200)",
            // should all be black.... average among these will be 0
            "_,_,_,_,_,_"
        )

        for ((idx, paletteString) in testPaletteStrings.withIndex()) {
            println("Test case $idx: input = \"$paletteString\"")

            val stringArray = utils.paletteStringToColorStringArray(paletteString)
            Log.w("MainActivity", "String Array: ${stringArray.joinToString(prefix = "[", postfix = "]")}")

            val couleurArray = utils.paletteStringToCouleurArray(paletteString)
            Log.w("MainActivity", "GENERATING COULEURARRAY")
            couleurArray.forEachIndexed { i, couleur ->
                val rgbString: String
                val name: String

                if (!couleur.isEmpty()) {
                    val color = couleur.getColor()
                    val rInt = (color.red() * 255).toInt()
                    val gInt = (color.green() * 255).toInt()
                    val bInt = (color.blue() * 255).toInt()
                    val aInt = (color.alpha() * 255).toInt()
                    rgbString = "(R:$rInt G:$gInt B:$bInt A:$aInt)"
                    name = couleur.getName()
                } else {
                    rgbString = "Empty"
                    name = "Empty"
                }

                Log.w("MainActivity", "Currently on $i, Color: $rgbString and Name: $name")
            }

            val palette = utils.generatePalette(couleurArray)
            val paletteArray = palette.getPalette()

            Log.w("MainActivity", "GENERATING PALETTEARRAY")
            paletteArray.forEachIndexed { i, couleur ->
                val color = couleur.getColor()
                val rInt = (color.red() * 255).toInt()
                val gInt = (color.green() * 255).toInt()
                val bInt = (color.blue() * 255).toInt()
                val aInt = (color.alpha() * 255).toInt()
                val rgbString = "(R:$rInt G:$gInt B:$bInt A:$aInt)"
                val name = couleur.getName()

                Log.w("MainActivity", "Generated $i, Color: $rgbString and Name: $name")
            }

            displayPalette(palette)
        }
    }

    // used for testing, not final
    private fun displayPalette(palette: Palette) {
        val container = activity.findViewById<LinearLayout>(R.id.main)

        val paletteRow = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                300
            ).apply {
                setMargins(0, 0, 0, 20)
            }
            orientation = LinearLayout.HORIZONTAL
        }

        for (couleur in palette.getPalette()) {
            val color = couleur.getColor()
            val colorInt = Color.argb(
                (color.alpha() * 255).toInt(),
                (color.red() * 255).toInt(),
                (color.green() * 255).toInt(),
                (color.blue() * 255).toInt()
            )

            val colorView = View(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1f
                )
                setBackgroundColor(colorInt)
            }

            paletteRow.addView(colorView)
        }

        container.addView(paletteRow)
    }

    // This is test2, hexcode
    fun test2() {
        var milk : Couleur = Couleur(132f, 86f, 60f, "Milk")

        Log.w("Tester", "Testing hex code: ${milk.getColorHexCode()}")

        var savedHex: Int = milk.getColorHexCodeInt()
        var newColor : Color = Color.valueOf(milk.getColorHexCodeInt())
        Log.w("Tester", "Testing new color is the same as old: ${newColor == milk.getColor()}")
        Log.w("Tester", "Testing what s color int: ${savedHex}")




    }

    // This is test3, Preferences
    fun test3() {

        var pref = Preferences(context)
        pref.addPaletteToFavorites(Palette(arrayOf(Couleur("A"), Couleur("B")), "dog"))
        pref.addPaletteToFavorites(Palette(arrayOf(Couleur("A"), Couleur("B")), "dog44"))
        Log.w("Tester", "Testing prefer greet code: ${pref.getFavoritePaletteList().contentToString()}")






    }

    // This is test4, serializable palette
    fun test4() {
        var pref = Preferences(context)
        pref.addPaletteToFavorites(Palette(arrayOf(Couleur("A"), Couleur("B")), "dog"))
        pref.addPaletteToFavorites(Palette(arrayOf(Couleur("C"), Couleur("D")), "dog44"))

        var pal = Palette(arrayOf(Couleur("A"), Couleur("B")), "dog")
        Log.w("Tester", pref.saveFavoriteList().toString())
    }

    // This is test5, serializable palette
    fun test5() {
        var pref = Preferences(context)

        Log.w("Tester", "Before: ${pref.getFavoritePaletteList().contentToString()}")

        pref.addPaletteToFavorites(Palette(arrayOf(Couleur("RED", "RED!"), Couleur("BLUE","BLUE!")), "Fun Colors"))
//        pref.addPaletteToFavorites(Palette(arrayOf(Couleur("C"), Couleur("D")), "dog44"))
//
        Log.w("Tester", "After adding: ${pref.getFavoritePaletteList().contentToString()}")
//
//        Log.w("Tester", "Saving: ${pref.saveFavoriteList()}")
//        pref.emptyFavoriteList()
//        Log.w("Tester", "Just emptied array, check empty: ${pref.getFavoritePaletteList().isEmpty()}")
//
//        pref.addPaletteToFavorites(Palette(arrayOf(Couleur("C"), Couleur("D")), "cat"))
//        pref.addPaletteToFavorites(Palette(arrayOf(Couleur("E"), Couleur("F")), "bird"))
//
//        Log.w("Tester", "Added new palettes: ${pref.getFavoritePaletteList().contentToString()}")
//        pref.loadFavoritePalette()
//        Log.w("Tester", "Loaded in favorites: ${pref.getFavoritePaletteList().contentToString()}")

//        Log.w("Tester", pref.loadFavoritePalette())
    }

    fun test6() {
        var pref = Preferences(context)

        Log.w("Tester", "Before: ${pref.getSearchHistory().contentToString()}")

        pref.addStringToSearchHistory("black,_,_,_,_")
//        pref.addPaletteToFavorites(Palette(arrayOf(Couleur("C"), Couleur("D")), "dog44"))
//
        Log.w("Tester", "After: ${pref.getSearchHistory().contentToString()}")

        Log.w("Tester", "Saving: ${pref.saveSearchHistory()}")
        pref.emptySaveHistory()
        Log.w("Tester", "Just emptied array, check empty: ${pref.getSearchHistory().isEmpty()}")

        pref.addStringToSearchHistory("black,red,_,_,_")
        pref.addStringToSearchHistory("black,green,blue,purple,_")

        Log.w("Tester", "Added new palettes: ${pref.getSearchHistory().contentToString()}")
        pref.loadSearchHistory()
        Log.w("Tester", "Loaded in favorites: ${pref.getSearchHistory().contentToString()}")

//        Log.w("Tester", pref.loadFavoritePalette())
    }

}