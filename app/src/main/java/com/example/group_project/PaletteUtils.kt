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
        var generatedPalettes : Array<Palette> = Array<Palette>(6, { i -> generatePalette(colors) })

        return generatedPalettes
    }

    // take palette string and convert it to a Array<Couleur>
    // ex: “(255,0,9),black,green” --> [ Couleur(red), Couleur(black), Couleur(green), null, null, null ]

    fun paletteStringToCouleurArray(paletteString : String) : Array<Couleur> {
        var couleurArray = Array<Couleur>(6, { i -> Couleur() })

        return couleurArray
    }

}