package com.example.group_project

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log

class Favorite : AppCompatActivity() {
    lateinit var goBackButton : Button
    var prefer : Preferences = Preferences.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorite)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        goBackButton = findViewById<Button>(R.id.favoriteBackButton)

        goBackButton.setOnClickListener { finish() }



    }

    override fun onStart() {
        super.onStart()

        prefer.loadFavoritePalette()
        var paletteList : Array<Palette> = prefer.getFavoritePaletteList()
        Log.w("Gen", paletteList.size.toString())
        for (palette in paletteList) {
            displayPalette(palette)
        }

        Log.w("Gen", "Was called!!")
    }

    fun displayPalette(palette: Palette) {
        val container = findViewById<LinearLayout>(R.id.favoriteColorLayout)


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