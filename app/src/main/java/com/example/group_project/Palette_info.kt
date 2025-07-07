package com.example.group_project

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Palette_info : AppCompatActivity() {
    var palette : Palette = GeneratedPalettesController.chosenPalette
    lateinit var goBackButton : Button
    lateinit var favoriteButton : Button
    lateinit var uploadButton : Button

    var alreadyFavorited : Boolean= false

    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_palette_info)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        displayPalette(palette)

        goBackButton = findViewById<Button>(R.id.backButton)
        favoriteButton = findViewById<Button>(R.id.favoriteButton)

        goBackButton.setOnClickListener { finish() }
        favoriteButton.setOnClickListener { addToFavoties() }

        Log.w("Gen", "Checking to see if right${palette}")
    }

    fun displayPalette(palette: Palette) {
        val container = findViewById<LinearLayout>(R.id.colorLayout)


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

    fun addToFavoties() {
        if (!alreadyFavorited) {
            var prefer =Preferences.getInstance()
            prefer.addPaletteToFavorites(palette)
            prefer.saveFavoriteList()
        }
    }

}