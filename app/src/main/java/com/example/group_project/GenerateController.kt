package com.example.group_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GenerateController : AppCompatActivity() {
    var utils : PaletteUtils = PaletteUtils()

    //    lateinit var colorInput : AutoCompleteTextView
    lateinit var colorInput : EditText
    lateinit var generateButton : Button
//    lateinit var numberOfPalettesInput : NumberPicker
    lateinit var numberOfPalettesInput : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        colorInput = findViewById(R.id.colorSearchView)
        generateButton = findViewById(R.id.generateButton)
        numberOfPalettesInput = findViewById(R.id.numberPicker)

        generateButton.setOnClickListener{ generatePalette() }
    }

    fun generatePalette() {
        var intent : Intent = Intent(this, GeneratedPalettesController::class.java)

        // generate palettes
//        var n : Int = numberOfPalettesInput.value.toInt()
        var n : Int = numberOfPalettesInput.text.toString().toInt()
        var input : String = colorInput.text.toString().trim()
        var colorStringArray = utils.paletteStringToColorStringArray(input)
        var couleurArray = utils.paletteStringArrayToCouleurArray(colorStringArray)
        var flags : Byte = 0b00000111

        Log.w("MainActivity", input)
        Log.w("MainActivity", n.toString())

        var palettes = utils.generatePalettes(couleurArray, flags, n)

        // set companion object to new generated
        MainActivity.palettes = palettes

        // change to generated palettes
        startActivity(intent)
    }
}