package com.example.group_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
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

    //Go back button
    lateinit var goBackButton : Button

    lateinit var seek : SeekBar
    lateinit var seekText : TextView


    //View List and stuff needed to get search history working
    lateinit var searchListView: ListView
    var searchHist : Array<String> = emptyArray()
    var searchList = emptyList<String>().toMutableList()



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

        goBackButton = findViewById<Button>(R.id.searchBackButton)
        goBackButton.setOnClickListener { finish() }

        searchListView = findViewById<ListView>(R.id.searchHistoryListView)

        seek = findViewById<SeekBar>(R.id.seekbar)
        seekText = findViewById(R.id.seekText)

        generateButton.setOnClickListener{ generatePalette() }

        seek.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            var progressChangedValue: Int = 0

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                progressChangedValue = progress
                seekText.text = progressChangedValue.toString().trim()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    override fun onStart() {
        super.onStart()

        Preferences.getInstance().loadSearchHistory()
        searchHist = Preferences.getInstance().getSearchHistory()
        searchList = searchHist.toList().toMutableList()

        var adapter = ArrayAdapter<String>(this,  R.layout.list_item_view, R.id.itemTextView, searchList)

        searchListView.setAdapter(adapter)
        var handler : itemInListClickHandler = itemInListClickHandler()
        searchListView.onItemClickListener = handler

    }

    inner class itemInListClickHandler : AdapterView.OnItemClickListener {
        override fun onItemClick(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            colorInput.setText(searchList.get(position).toString())
            Log.w("Gen", "Testing position: $position and in list: ${searchList.get(position)}")
        }

    }

    fun generatePalette() {
        var intent : Intent = Intent(this, GeneratedPalettesController::class.java)

        // generate palettes
//        var n : Int = numberOfPalettesInput.value.toInt()
//        val n: Int = numberOfPalettesInput.text.toString().trim().toIntOrNull() ?: 1
        var n : Int = seek.getProgress()
        if (n <= 0) {
            n += 1
        }
        var input : String = colorInput.text.toString().trim()
        var colorStringArray = utils.paletteStringToColorStringArray(input)
        var couleurArray = utils.paletteStringArrayToCouleurArray(colorStringArray)
        var flags : Byte = 0b00000111

        Log.w("MainActivity", input)
        Log.w("MainActivity", n.toString())

        //Save input to search history
        if (!input.isEmpty()) {
            var prefer = Preferences.getInstance()
            prefer.addStringToSearchHistory(input)
            prefer.saveSearchHistory()
        }

        var palettes = utils.generatePalettes(couleurArray, flags, n)

        // set companion object to new generated
        MainActivity.palettes = palettes

        // change to generated palettes
        startActivity(intent)
    }

}