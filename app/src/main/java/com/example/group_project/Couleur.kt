package com.example.group_project

import android.graphics.Color
import kotlin.random.Random
import androidx.core.graphics.toColorInt
import java.lang.Integer

class Couleur {
    private lateinit var color: Color
    private lateinit var colorName: String
    private lateinit var colorHexCodeName : String

    private var emptyColor : Boolean = false

    constructor() {
        this.emptyColor = true
    }

    constructor(colorString: String, colorName: String) {
        this.color = Color.valueOf(colorString.toColorInt())
        this.colorName = colorName
        this.colorHexCodeName = "#" + Integer.toHexString(color.toArgb()).uppercase()
    }

    constructor(r: Float, g: Float, b: Float, colorName: String) {
        this.color = Color.valueOf(r / 255f, g / 255f, b / 255f, 1f)
        this.colorName = colorName
        this.colorHexCodeName = "#" + Integer.toHexString(color.toArgb()).uppercase()
    }

    constructor(colorName: String) {
        val r = Random.nextInt(0, 256)
        val g = Random.nextInt(0, 256)
        val b = Random.nextInt(0, 256)
        val a = Random.nextInt(0, 256)

        this.color = Color.valueOf(
            r / 255f,
            g / 255f,
            b / 255f,
            1f
        )
        this.colorName = colorName

        this.colorHexCodeName = "#" + Integer.toHexString(color.toArgb()).uppercase()
    }

    fun getColor() : Color = this.color
    fun getName() : String = this.colorName
    fun getColorHexCode() : String = this.colorHexCodeName
    fun getColorHexCodeInt() : Int = this.colorHexCodeName.toColorInt()
    fun isEmpty() : Boolean = this.emptyColor
}
