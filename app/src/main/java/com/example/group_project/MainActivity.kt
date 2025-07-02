package com.example.group_project

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color

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
        val container = findViewById<LinearLayout>(R.id.main)

        val paletteRow = LinearLayout(this).apply {
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

            val colorView = View(this).apply {
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
}
