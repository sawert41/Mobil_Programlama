package com.example.veritabaniornek

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var veritabani: Veritabani

    private lateinit var editTextId: EditText
    private lateinit var editTextAd: EditText
    private lateinit var editTextSoyad: EditText
    private lateinit var editTextYas: EditText
    private lateinit var editTextSehir: EditText
    private lateinit var textViewSonuc: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        veritabani = Veritabani(this)

        editTextId = findViewById(R.id.editTextId)
        editTextAd = findViewById(R.id.editTextAd)
        editTextSoyad = findViewById(R.id.editTextSoyad)
        editTextYas = findViewById(R.id.editTextYas)
        editTextSehir = findViewById(R.id.editTextSehir)
        textViewSonuc = findViewById(R.id.textViewSonuc)

        val buttonEkle = findViewById<Button>(R.id.buttonEkle)
        val buttonGoster = findViewById<Button>(R.id.buttonGoster)
        val buttonSil = findViewById<Button>(R.id.buttonSil)
        val buttonGuncelle = findViewById<Button>(R.id.buttonGuncelle)

        buttonEkle.setOnClickListener {
            val ad = editTextAd.text.toString()
            val soyad = editTextSoyad.text.toString()
            val yas = editTextYas.text.toString()
            val sehir = editTextSehir.text.toString()

            val basarili = veritabani.kayitEkle(ad, soyad, yas, sehir)

            if (basarili) {
                mesajGoster("Kayıt eklendi.")
                alanlariTemizle()
                textViewSonuc.text = veritabani.kayitlariGetir()
            } else {
                mesajGoster("Kayıt eklenemedi. Ad boş olamaz.")
            }
        }

        buttonGoster.setOnClickListener {
            textViewSonuc.text = veritabani.kayitlariGetir()
        }

        buttonSil.setOnClickListener {
            val id = editTextId.text.toString()

            val silinenKayitSayisi = veritabani.kayitSil(id)

            if (silinenKayitSayisi > 0) {
                mesajGoster("Kayıt silindi.")
                alanlariTemizle()
                textViewSonuc.text = veritabani.kayitlariGetir()
            } else {
                mesajGoster("Silinecek kayıt bulunamadı. Geçerli ID gir.")
            }
        }

        buttonGuncelle.setOnClickListener {
            val id = editTextId.text.toString()
            val ad = editTextAd.text.toString()
            val soyad = editTextSoyad.text.toString()
            val yas = editTextYas.text.toString()
            val sehir = editTextSehir.text.toString()

            val guncellenenKayitSayisi = veritabani.kayitGuncelle(
                id,
                ad,
                soyad,
                yas,
                sehir
            )

            if (guncellenenKayitSayisi > 0) {
                mesajGoster("Kayıt güncellendi.")
                alanlariTemizle()
                textViewSonuc.text = veritabani.kayitlariGetir()
            } else {
                mesajGoster("Güncellenecek kayıt bulunamadı. Geçerli ID gir.")
            }
        }
    }

    private fun alanlariTemizle() {
        editTextId.text.clear()
        editTextAd.text.clear()
        editTextSoyad.text.clear()
        editTextYas.text.clear()
        editTextSehir.text.clear()
    }

    private fun mesajGoster(mesaj: String) {
        Toast.makeText(this, mesaj, Toast.LENGTH_SHORT).show()
    }
}