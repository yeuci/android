package com.example.group_project

class PaletteSave() {
    var colors: ArrayList<Int> = ArrayList()
    var name: String = ""
    var timestamp: Long = System.currentTimeMillis()

    constructor(palette: Palette) : this() {
        val couleurs = palette.getPalette()
        for (couleur in couleurs) {
            val color = couleur.getColor()
            colors.add(color.toArgb())
        }
        this.name = palette.getPaletteName()
        this.timestamp = System.currentTimeMillis()
    }
}