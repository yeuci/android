package com.example.group_project

import android.graphics.Color
import kotlin.random.Random
import androidx.core.graphics.toColorInt

class Couleur {
    private lateinit var color: Color
    private lateinit var colorName: String

    private var emptyColor : Boolean = false

    constructor() {
        this.emptyColor = true
    }

    constructor(colorString: String, colorName: String) {
        this.colorName = colorName
        this.color = try {
            Color.valueOf(colorString.toColorInt())
        } catch (e: Exception) {
            val r = Random.nextFloat()
            val g = Random.nextFloat()
            val b = Random.nextFloat()
            Color.valueOf(Color.rgb((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt()))
        }
    }

    constructor(r: Float, g: Float, b: Float, colorName: String) {
        this.color = Color.valueOf(r / 255f, g / 255f, b / 255f, 1f)
        this.colorName = colorName
    }

    constructor(colorName: String) {
        val r = Random.nextInt(0, 256)
        val g = Random.nextInt(0, 256)
        val b = Random.nextInt(0, 256)

        this.color = Color.valueOf(
            r / 255f,
            g / 255f,
            b / 255f,
            1f
        )
        this.colorName = colorName
    }

    fun getColor() : Color = this.color
    fun getName() : String = this.colorName
    fun isEmpty() : Boolean = this.emptyColor
}