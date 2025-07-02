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
}