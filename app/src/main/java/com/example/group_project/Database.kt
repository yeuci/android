package com.example.group_project
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class Database {
    private val db = FirebaseFirestore.getInstance()
    private val paletteCollection = db.collection("palettes")

    suspend fun deleteAllPalettes() {
        try {
            val snapshot = paletteCollection.get().await()
            for (doc in snapshot.documents) {
                paletteCollection.document(doc.id).delete().await()
            }
            Log.d("DB", "All palettes deleted successfully.")
        } catch (e: Exception) {
            Log.e("DB", "Failed to delete palettes", e)
        }
    }

    suspend fun getAllPalettes(): Array<PaletteSave> {
        return try {
            val snapshot = paletteCollection
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.ASCENDING)
                .get().await()
            snapshot.toObjects(PaletteSave::class.java).toTypedArray()
        } catch (e: Exception) {
            emptyArray()
        }
    }

    suspend fun checkIfPaletteExists(palette: Palette): Boolean {
        val targetColors = palette.getPalette()
            .map { it.getColor().toArgb() }
            .toList()

        return try {
            val snapshot = paletteCollection.get().await()
            val allPalettes = snapshot.toObjects(PaletteSave::class.java)

            allPalettes.any { saved ->
                saved.colors == targetColors
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun uploadPalette(palette: Palette): Boolean {
        val paletteSave = PaletteSave(palette)
        return try {
            val docId = if (paletteSave.name.isNotBlank()) paletteSave.name else UUID.randomUUID().toString()
            Log.d("DB", "Uploading palette named '$docId' with colors: ${paletteSave.colors}")
            paletteCollection.document(docId).set(paletteSave).await()
            Log.d("DB", "Upload successful")
            true
        } catch (e: Exception) {
            Log.e("DB", "Upload failed", e)
            false
        }
    }
}