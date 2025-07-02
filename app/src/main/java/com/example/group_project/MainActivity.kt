package com.example.group_project

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var utils : PaletteUtils = PaletteUtils()

        val testPaletteStrings = listOf(
            "red,blue,green,yellow",
            "(255,0,0),(0,255,0),(0,0,255),(255,255,0)",
            "_,_,red,_,_",
            "red,(123,45,67),green,(0,255,0),_,_",
            "(100,100,100),black,(50,50,50),white,gray,(200,200,200)"
        )

        for ((idx, palette) in testPaletteStrings.withIndex()) {
            println("Test case $idx: input = \"$palette\"")

            val stringArray = utils.paletteStringToColorStringArray(palette)
            Log.w("MainActivity","String Array: ${stringArray.joinToString(prefix = "[", postfix = "]")}")

            val couleurArray = utils.paletteStringToCouleurArray(palette)
            couleurArray.forEachIndexed { i, couleur ->
                var rgbString : String
                var name : String

                if (!couleur.isEmpty()) {
                    var color = couleur.getColor()
                    val r = color.red()
                    val g = color.green()
                    val b = color.blue()
                    val a = color.alpha()

                    val rInt = (r * 255).toInt()
                    val gInt = (g * 255).toInt()
                    val bInt = (b * 255).toInt()
                    val aInt = (a * 255).toInt()


                    rgbString = "(R:$rInt G:$gInt B:$bInt A:$aInt)"
                    name = couleur.getName()
                } else {
                    rgbString = "Empty"
                    name = "Empty"
                }
                Log.w("MainActivity", "Currently on ${i}, Color: ${rgbString} and Name: ${name}")
            }
        }

    }
}