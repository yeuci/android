package com.example.group_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var generateButton : Button
    lateinit var favoritedButton : Button
    lateinit var uploadedButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Preferences.initialize(this)

        generateButton = findViewById<Button>(R.id.generatePaletteButton)
        favoritedButton = findViewById<Button>(R.id.favoritePaletteButton)
        uploadedButton = findViewById<Button>(R.id.uploadedPaletteButton)


        generateButton.setOnClickListener{ generatePalettePage() }
        favoritedButton.setOnClickListener{ favoritedPalettePage() }
        uploadedButton.setOnClickListener{ uploadedPalettePage() }

    }

    fun generatePalettePage() {
        var intent : Intent = Intent(this, GenerateController::class.java)
        startActivity(intent)
    }

    fun favoritedPalettePage() {
        var intent : Intent = Intent(this, Favorite::class.java)
        startActivity(intent)
    }

    fun uploadedPalettePage() {
        var intent : Intent = Intent(this, GenerateController::class.java)
        startActivity(intent)
    }

    companion object {
        lateinit var palettes : Array<Palette>

        //Local saving class

    }


}