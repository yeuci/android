package com.example.group_project

import android.content.Intent
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


class GeneratedPalettesController : AppCompatActivity() {
    lateinit var goBackButton : Button


    companion object {
        lateinit var chosenPalette : Palette
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.generated_palettes)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.palettes_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        goBackButton = findViewById<Button>(R.id.genBackButton)
        goBackButton.setOnClickListener { finish() }

        // get all palettes and display
        for (palette in MainActivity.palettes) {
            displayPalette(palette)
        }
    }

    fun displayPalette(palette: Palette) {
        val container = findViewById<LinearLayout>(R.id.palettes_main)

        val paletteRow = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                300
            ).apply {
                setMargins(0, 0, 0, 20)
                setOnClickListener { goToPalleteInfoPage(palette) }
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

    fun goToPalleteInfoPage(palette: Palette) {
        chosenPalette = palette

        Log.w("Gen", "$palette and check chosen $chosenPalette");
        var intent : Intent = Intent(this, Palette_info::class.java)
        startActivity(intent)
    }

//    inner class LinearLayoutPalette : LinearLayout {
//        lateinit var myPalette : Palette
//
//        constructor(context: AppCompatActivity, palette: Palette) : super(context) {
//            this.myPalette = palette
//        }
//
//
//    }

}