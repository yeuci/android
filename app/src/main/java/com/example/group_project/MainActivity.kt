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
import android.provider.ContactsContract.Data
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize


class MainActivity : AppCompatActivity() {
    lateinit var generateButton : Button
    lateinit var favoritedButton : Button
    lateinit var uploadedButton : Button

    var db : Database = Database()
    private lateinit var adView : AdView


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

        lifecycleScope.launch {
            db.deleteAllPalettes()
        }

        adView = AdView( this )
        val adSize : AdSize = AdSize( AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT )
        adView.setAdSize( adSize )
        val adUnitId : String = "ca-app-pub-3940256099942544/6300978111"
        adView.adUnitId = adUnitId
        val builder : AdRequest.Builder = AdRequest.Builder( )
        builder.addKeyword( "fitness" )
        builder.addKeyword( "workout" ).addKeyword( "gym" )
        val request : AdRequest = builder.build()

        val adLayout :LinearLayout = findViewById( R.id.ad_view )
        adLayout.addView( adView )

        adView.loadAd( request )
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
        var intent : Intent = Intent(this, Uploads::class.java)
        startActivity(intent)
    }


    companion object {
        lateinit var palettes : Array<Palette>

        suspend fun uploadPaletteBtn(palette : Palette, name : String) {
            var db : Database = Database()
            db.uploadPalette(palette, name)
        }

        //Local saving class

    }


}