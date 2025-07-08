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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Uploads : AppCompatActivity() {
    var db : Database = Database()
    var utils : PaletteUtils = PaletteUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_uploads)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var backButton : Button = findViewById<Button>(R.id.uploadBackButton)
        backButton.setOnClickListener { finish() }


        lifecycleScope.launch {
            var paletteSaves : Array<PaletteSave> = db.getAllPalettes()
            for (paletteSave in paletteSaves) {
                var palette : Palette = utils.paletteSaveToPalette(paletteSave)
                displayPalette(palette)
            }
        }
    }


    fun displayPalette(palette: Palette) {
        val container = findViewById<LinearLayout>(R.id.main_container)


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