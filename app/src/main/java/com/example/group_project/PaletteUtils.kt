package com.example.group_project

class PaletteUtils {
    private var S_DELTA = 50
    private var H_DELTA = 30
    private var V_DELTA = 25


    // generate one color palette from an array of colors
    fun generatePalette(colors : Array<Couleur>) : Palette {
        var generatedPalette = Palette(colors, "")

        return generatedPalette
    }

    // generate multiple color palette from an array of colors
    fun generatePalette(colors : Array<Couleur>, palettesToGenerate : Int) : Array<Palette> {
        var generatedPalettes : Array<Palette> = Array<Palette>(palettesToGenerate, { i -> generatePalette(colors) })
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
    fun paletteStringToCouleurArray(paletteString: String): Array<Couleur> {
        val colorStrings = paletteStringToColorStringArray(paletteString)

        return Array(6) { i ->
            val s = colorStrings[i]

            when {
                s == "_" -> {
                    Couleur()
                }
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
                else -> {
                    Couleur(s, s)
                }
            }
        }
    }

}