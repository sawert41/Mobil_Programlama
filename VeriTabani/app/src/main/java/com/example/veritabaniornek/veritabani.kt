package com.example.veritabaniornek

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Veritabani(context: Context) :
    SQLiteOpenHelper(context, "ogrenciler.db", null, 1) {

    companion object {
        private const val TABLE_NAME = "ogrenciler"
        private const val COL_ID = "id"
        private const val COL_AD = "ad"
        private const val COL_SOYAD = "soyad"
        private const val COL_YAS = "yas"
        private const val COL_SEHIR = "sehir"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_AD TEXT,
                $COL_SOYAD TEXT,
                $COL_YAS INTEGER,
                $COL_SEHIR TEXT
            )
        """.trimIndent()

        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun kayitEkle(ad: String, soyad: String, yas: String, sehir: String): Boolean {
        if (ad.isBlank()) {
            return false
        }

        val values = ContentValues()
        values.put(COL_AD, ad)
        values.put(COL_SOYAD, soyad)
        values.put(COL_YAS, yas.toIntOrNull() ?: 0)
        values.put(COL_SEHIR, sehir)

        val sonuc = writableDatabase.insert(TABLE_NAME, null, values)

        return sonuc != -1L
    }

    fun kayitlariGetir(): String {
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.count == 0) {
            cursor.close()
            return "Kayıt yok."
        }

        val sonuc = StringBuilder()

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
            val ad = cursor.getString(cursor.getColumnIndexOrThrow(COL_AD))
            val soyad = cursor.getString(cursor.getColumnIndexOrThrow(COL_SOYAD))
            val yas = cursor.getInt(cursor.getColumnIndexOrThrow(COL_YAS))
            val sehir = cursor.getString(cursor.getColumnIndexOrThrow(COL_SEHIR))

            sonuc.append("ID: $id\n")
            sonuc.append("Ad: $ad\n")
            sonuc.append("Soyad: $soyad\n")
            sonuc.append("Yaş: $yas\n")
            sonuc.append("Şehir: $sehir\n")
            sonuc.append("--------------------\n")
        }

        cursor.close()

        return sonuc.toString()
    }

    fun kayitSil(id: String): Int {
        val idInt = id.toIntOrNull() ?: return 0

        return writableDatabase.delete(
            TABLE_NAME,
            "$COL_ID = ?",
            arrayOf(idInt.toString())
        )
    }

    fun kayitGuncelle(
        id: String,
        ad: String,
        soyad: String,
        yas: String,
        sehir: String
    ): Int {
        val idInt = id.toIntOrNull() ?: return 0

        if (ad.isBlank()) {
            return 0
        }

        val values = ContentValues()
        values.put(COL_AD, ad)
        values.put(COL_SOYAD, soyad)
        values.put(COL_YAS, yas.toIntOrNull() ?: 0)
        values.put(COL_SEHIR, sehir)

        return writableDatabase.update(
            TABLE_NAME,
            values,
            "$COL_ID = ?",
            arrayOf(idInt.toString())
        )
    }
}