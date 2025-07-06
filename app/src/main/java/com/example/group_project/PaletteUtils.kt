package com.example.group_project

import android.graphics.Color
import kotlin.random.Random

class PaletteUtils {
    private var S_DELTA = 50
    private var H_DELTA = 45
    private var V_DELTA = 22

    // generate one color palette from an array of colors
    fun generatePalette(colors: Array<Couleur>, flags : Byte): Palette {
        val hsvList = mutableListOf<FloatArray>()

        // cache current hsv, will use later for color generation and saturation computations
        // this way, all palette strings are considered, even the empty palette --> ("","","","","","")
        for (couleur in colors) {
            if (!couleur.isEmpty()) {
                val c = couleur.getColor()
                val r = (c.red() * 255).toInt()
                val g = (c.green() * 255).toInt()
                val b = (c.blue() * 255).toInt()

                val hsv = FloatArray(3)
                Color.RGBToHSV(r, g, b, hsv)
                hsvList.add(hsv)
            }
        }

        // get average of hsvList, emptyList sum and size will be 0, in that case no need to get average
        val avgHSV = FloatArray(3) { 0f }
        for (hsv in hsvList) {
            avgHSV[0] += hsv[0]
            avgHSV[1] += hsv[1]
            avgHSV[2] += hsv[2]
        }

        if (hsvList.isNotEmpty()) {
            val size = hsvList.size.toFloat()
            avgHSV[0] /= size
            avgHSV[1] /= size
            avgHSV[2] /= size
        }

        // if average is 0 OR list is empty, this means we're at a empty palette (ex.. "_,_,_,_,_,_")
        // re-run algo with random colors
        if (hsvList.isEmpty()) {
            var newColors = Array<Couleur>(6, { i -> Couleur("random${i.toString()}Color") } )
            return generatePalette(newColors, flags)
        }

        val newColors = Array(6) { Couleur() }

        for (i in colors.indices) {
            val original = colors[i]

            // will be considered the new current palette color variation
            val newHSV = FloatArray(3)

            if (!original.isEmpty()) {
                val c = original.getColor()
                val r = (c.red() * 255).toInt()
                val g = (c.green() * 255).toInt()
                val b = (c.blue() * 255).toInt()

                val hsv = FloatArray(3)
                Color.RGBToHSV(r, g, b, hsv)

                // we have a color to work with, we can adjust the current color
                // in general...
                // 1- get a random number from 1 - S_DELTA
                // 2- randomize an increase or decrease to saturation
                // 3- add/subtract calculated delta to hsv, and clamp

                val hDelta = Random.nextInt(1, H_DELTA + 1).toFloat()
                val sDelta = Random.nextInt(1, S_DELTA + 1) / 100f
                val vDelta = Random.nextInt(1, V_DELTA + 1) / 100f

                val increase = Random.nextBoolean()

                // check byte flags for each channel
                // can either take excess or just wrap if over
                if ((flags.toInt() and (1 shl 0)) != 0) {
                    hsv[0] = (hsv[0] + if (increase) hDelta else -hDelta + 360f) % 360f
                }
                if ((flags.toInt() and (1 shl 1)) != 0) {
                    hsv[1] = (hsv[1] + if (increase) sDelta else -sDelta).coerceIn(0f, 1f)
                }
                if ((flags.toInt() and (1 shl 2)) != 0) {
                    hsv[2] = (hsv[2] + if (increase) vDelta else -vDelta).coerceIn(0f, 1f)
                }

                newHSV[0] = hsv[0]
                newHSV[1] = hsv[1]
                newHSV[2] = hsv[2]
            } else {
                // come across some empty color ("_")
                // we've saved previous hsv averages, so we can still generate a color, when not given one
                // we generate a color by using previous averages and randomly picking a value from each value delta (-, +)
                // flags aren't used here, since these are random...
                newHSV[0] = (avgHSV[0] + Random.nextInt(-H_DELTA, H_DELTA + 1)).coerceIn(0f, 360f)
                newHSV[1] = (avgHSV[1] + Random.nextInt(-S_DELTA, S_DELTA + 1) / 100f).coerceIn(0f, 1f)
                newHSV[2] = (avgHSV[2] + Random.nextInt(-V_DELTA, V_DELTA + 1) / 100f).coerceIn(0f, 1f)
            }

            val colorInt = Color.HSVToColor(newHSV)
            val color = Color.valueOf(colorInt)
            val r = color.red() * 255f
            val g = color.green() * 255f
            val b = color.blue() * 255f

            // incomplete
            newColors[i] = Couleur(r, g, b, "")
        }

        // will come back to later, will need to generate a name
        // first color gives ancestor
        // second can be based off of first-sixth (anywhere in between)
        return Palette(newColors, "")
    }

    // generate multiple color palette from an array of colors
    fun generatePalettes(colors : Array<Couleur>, flags : Byte, palettesToGenerate : Int) : Array<Palette> {
        var generatedPalettes : Array<Palette> = Array<Palette>(palettesToGenerate, { i -> generatePalette(colors, flags) })
        return generatedPalettes
    }

    // take palette string and convert it to a Array<String>
    // ex: “(255,0,9),black,green” --> [ "(255,0,9)", "black", "green", "_", "_", "_" ]
    fun paletteStringToColorStringArray(paletteString: String): Array<String> {
        val parts = mutableListOf<String>()
        var bracketLevel = 0
        val current = StringBuilder()

        for (char in paletteString) {
            when (char) {
                '(' -> {
                    bracketLevel++
                    current.append(char)
                }
                ')' -> {
                    bracketLevel--
                    current.append(char)
                }
                ',' -> {
                    if (bracketLevel == 0) {
                        parts.add(current.toString().trim())
                        current.clear()
                    } else {
                        current.append(char)
                    }
                }
                else -> current.append(char)
            }
        }

        if (current.isNotEmpty()) {
            parts.add(current.toString().trim())
        }

        val resultList = parts.toMutableList()
        while (resultList.size < 6) {
            resultList.add("_")
        }

        return resultList.take(6).toTypedArray()
    }

    // takes in a ColorStringArray and converts it to a Array<Couleur>
    // [ "(255,0,9)", "black", "green", "_", "_", "_" ] --> [ Couleur(255,0,9), Couleur(black), Couleur(green), Couleur(), Couleur(), Couleur() ]
    fun paletteStringArrayToCouleurArray(paletteStringArray: Array<String>): Array<Couleur> {
        val result = Array(6) { Couleur() }

        for (i in 0 until minOf(6, paletteStringArray.size)) {
            val s = paletteStringArray[i].trim()

            result[i] = when {
                s == "_" || s.isEmpty() -> Couleur()

                s.startsWith("(") && s.endsWith(")") -> {
                    val rgbValues = s.drop(1).dropLast(1).split(",").map { it.trim() }
                    if (rgbValues.size >= 3) {
                        val r = rgbValues[0].toFloatOrNull() ?: 0f
                        val g = rgbValues[1].toFloatOrNull() ?: 0f
                        val b = rgbValues[2].toFloatOrNull() ?: 0f
                        Couleur(r, g, b, "")
                    } else {
                        Couleur()
                    }
                }

                else -> Couleur(s, s)
            }
        }

        return result
    }

    fun paletteSaveToPalette(paletteSave : PaletteSave) : Palette {
        var couleurs : Array<Couleur> = Array<Couleur>(6, { Couleur() })

        for (i in 0 until couleurs.size) {
            var currentColor = paletteSave.colors[i]
            couleurs[i] = Couleur(currentColor)
        }

        var palette = Palette(couleurs, "")

        return palette
    }
}