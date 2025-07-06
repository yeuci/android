package com.example.group_project

class Palette {
    private lateinit var palette : Array<Couleur>
    private lateinit var paletteName : String


    constructor(palette : Array<Couleur>, paletteName : String) {
        this.palette = palette
        this.paletteName = paletteName
    }

    fun getPalette() : Array<Couleur> = palette
    fun getPaletteName() : String = paletteName

    fun addCouleurToPalette( couleur : Couleur) {
        palette += couleur
    }

    override fun toString(): String {
        var res = "$paletteName {"

        for(i in 0..palette.size - 2) {
            res += palette[i].getColorHexCode()  + ","
        }

        res += palette[palette.size - 1].getColorHexCode() + "}"

        return res
    }
}