package com.example.firebaseornek

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import java.io.IOException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WallpaperAdapter
    private val wallpapers = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = WallpaperAdapter(wallpapers) { imageUrl ->
            setWallpaperFromUrl(imageUrl)
        }

        recyclerView.adapter = adapter

        loadWallpapersFromDatabase()
    }

    private fun loadWallpapersFromDatabase() {
        val databaseRef = FirebaseDatabase.getInstance()
            .reference
            .child("wallpapers")

        databaseRef.get()
            .addOnSuccessListener { snapshot ->
                wallpapers.clear()

                for (item in snapshot.children) {
                    val imageUrl = item.getValue(String::class.java)

                    if (imageUrl != null) {
                        wallpapers.add(imageUrl)
                    }
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Resimler alinamadi", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setWallpaperFromUrl(imageUrl: String) {
        Toast.makeText(this, "Duvar kagidi ayarlaniyor...", Toast.LENGTH_SHORT).show()

        thread {
            try {
                val bitmap: Bitmap = Picasso.get()
                    .load(imageUrl)
                    .get()

                val wallpaperManager = WallpaperManager.getInstance(applicationContext)
                wallpaperManager.setBitmap(bitmap)

                runOnUiThread {
                    Toast.makeText(this, "Duvar kagidi degistirildi", Toast.LENGTH_SHORT).show()
                }

            } catch (e: IOException) {
                runOnUiThread {
                    Toast.makeText(this, "Duvar kagidi ayarlanamadi", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}