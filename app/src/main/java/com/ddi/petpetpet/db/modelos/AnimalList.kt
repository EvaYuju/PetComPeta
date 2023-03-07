package com.ddi.petpetpet.db.modelos

import android.database.Cursor
import com.ddi.petpetpet.db.DatabaseHelper

class AnimalList {
    // Para poder acceder a lo que está dentro de estra clase
    companion object {
        fun getLista(BD: DatabaseHelper): ArrayList<Animal> {
            val lista: ArrayList<Animal> = ArrayList()
            val db = BD.writableDatabase
            val cursor = db.rawQuery("SELECT * FROM ANIMALES", null)
            while (cursor.moveToNext()) {
                lista.add(setAnimal(cursor))
            }
            cursor.close()
            db.close()
            return lista
        }

        private fun setAnimal(cursor: Cursor): Animal {
            return Animal(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6)

            )
        }
    }
}