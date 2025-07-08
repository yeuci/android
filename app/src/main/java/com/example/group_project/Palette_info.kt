package com.example.group_project

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Palette_info : AppCompatActivity() {
    var palette : Palette = GeneratedPalettesController.chosenPalette
    lateinit var goBackButton : Button
    lateinit var favoriteButton : Button
    lateinit var uploadButton : Button
    lateinit var emailButton : Button
    lateinit var nameInput : EditText

    lateinit var db : Database

    lateinit var emailText : EditText

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

        alreadyFavorited = false

        displayPalette(palette)

        goBackButton = findViewById<Button>(R.id.backButton)
        favoriteButton = findViewById<Button>(R.id.favoriteButton)
        emailButton = findViewById<Button>(R.id.emailButton)
        uploadButton = findViewById<Button>(R.id.uploadButton)
        nameInput = findViewById<EditText>(R.id.nameEditText)

        db = Database()

        //Go back
        goBackButton.setOnClickListener { finish() }

        //Update favorite list
        favoriteButton.setOnClickListener { addToFavoties() }

        emailText = findViewById<EditText>(R.id.emailEditText)
        emailButton.setOnClickListener { sendEmail() }

        uploadButton.setOnClickListener {
            lifecycleScope.launch {
                uploadPaletteBtnClicked()
            }
        }

//        Log.w("Gen", "Checking to see if right${palette}")
    }

    suspend fun uploadPaletteBtnClicked() {
        if (checkIfNameIsEmpty()) {
            val text = "Please input a name!"
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(this, text, duration)
            toast.show()
            return
        }
        palette.setPaletteName(nameInput.text.trim().toString())
        MainActivity.uploadPaletteBtn(palette, nameInput.text.trim().toString())
        val text = "Palette: ${nameInput.text.trim()} uploaded!"
        val duration = Toast.LENGTH_LONG

        val toast = Toast.makeText(this, text, duration)
        toast.show()
    }

    fun checkIfNameIsEmpty() : Boolean {
        return nameInput.text.trim().isEmpty()
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
        if (checkIfNameIsEmpty()) {
            val text = "Please input a name!"
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(this, text, duration)
            toast.show()
            return
        }

        if (!alreadyFavorited) {


            var prefer = Preferences.getInstance()
            prefer.loadFavoritePalette()
            palette.setPaletteName(emailText.text.toString())
            prefer.addPaletteToFavorites(palette)
            prefer.saveFavoriteList()
            alreadyFavorited = true

            Log.w("Gen", "Added palette")
        }
    }

    fun sendEmail() {
        if (checkIfNameIsEmpty()) {
            val text = "Please input a name!"
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(this, text, duration)
            toast.show()
            return
        }

        val recipient: String = emailText.text.toString()
        val subject = "Palette Hexcode"
        val body = palette.toString()

        Log.w("Gen", "Enter email and body is $body")

        val mailtoUri = ("mailto:${Uri.encode(recipient)}" +
                "?subject=${Uri.encode(subject)}" +
                "&body=${Uri.encode(body)}").toUri()

        val intent = Intent(Intent.ACTION_SENDTO, mailtoUri).apply {
            // Add extras as backup (some clients may prefer these)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        try {
            startActivity(Intent.createChooser(intent, "Send email using..."))
            emailText.text.clear()
            Log.w("Gen", "Email intent launched")

        } catch (e: Exception) {

        }
    }

}