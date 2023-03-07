package com.ddi.petpetpet.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ddi.petpetpet.RegistroAnimalesActivity
import com.ddi.petpetpet.db.modelos.Animal

var BD="baseDatosPetpetpet";
class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, BD, factory, DATABASE_VERSION) {
    private lateinit var binding: RegistroAnimalesActivity

    companion object {
        private const val DATABASE_VERSION = 6
        // Tabla usuario
        private const val TABLE_NAME_USU = "usuarios"
        val COLUMN_USUARIO = "usuario"
        val COLUMN_CONTRASENA = "contrasena"
        // usuario preregistrado
        private const val ADMIN_USERNAME = "admin"
        private const val ADMIN_PASSWORD = "admin"
        // Tabla animal
        private const val TABLE_NAME_ANI = "animales"
        private const val COLUMN_CODIGO = "codigo"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_RAZA = "raza"
        private const val COLUMN_FECNAC = "fecnac"
        private const val COLUMN_SEXO = "sexo"
        private const val COLUMN_DNI = "dni"
        private const val COLUMN_IMG = "foto"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla de usuarios
        val createTableU = ("CREATE TABLE $TABLE_NAME_USU (" +
                "$COLUMN_USUARIO TEXT," +
                "$COLUMN_CONTRASENA TEXT)")
        // ?para los nulos // Ejecute sentencia
        db?.execSQL(createTableU)
        // Insertar usuario administrador
        val values = ContentValues().apply {
            put(COLUMN_USUARIO, ADMIN_USERNAME)
            put(COLUMN_CONTRASENA, ADMIN_PASSWORD)
        }
        db?.insert(TABLE_NAME_USU, null, values)
        // Crear tabla de animales
        val createTableA = ("CREATE TABLE "+ TABLE_NAME_ANI +"(" +
                COLUMN_CODIGO + " TEXT PRIMARY KEY," +
                COLUMN_NOMBRE + " TEXT," +
                COLUMN_RAZA + " TEXT," +
                COLUMN_SEXO + " TEXT," +
                COLUMN_FECNAC + " TEXT," +
                COLUMN_DNI + " TEXT," +
                COLUMN_IMG + " TEXT" + ")")
        db?.execSQL(createTableA)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_USU")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ANI")
        onCreate(db)
    }
    // Chequear el usuario
    fun checkUser(usuario: String, contrasena: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_USU WHERE $COLUMN_USUARIO = ? AND $COLUMN_CONTRASENA = ?"
        val cursor = db.rawQuery(query, arrayOf(usuario, contrasena))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    @SuppressLint("Range")
    fun getAnimal(codigo: String): Animal? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_ANI WHERE $COLUMN_CODIGO = ?"
        val cursor = db.rawQuery(query, arrayOf(codigo))

        var animal: Animal? = null

        if (cursor.moveToFirst()) {
            val codigo = cursor.getString(cursor.getColumnIndex(COLUMN_CODIGO))
            val nombre = cursor.getString(cursor.getColumnIndex(COLUMN_NOMBRE))
            val raza = cursor.getString(cursor.getColumnIndex(COLUMN_RAZA))
            val sexo = cursor.getString(cursor.getColumnIndex(COLUMN_SEXO))
            val fecnac = cursor.getString(cursor.getColumnIndex(COLUMN_FECNAC))
            val dniPropietario = cursor.getString(cursor.getColumnIndex(COLUMN_DNI))
            val imagen = cursor.getString(cursor.getColumnIndex(COLUMN_IMG))

            animal = Animal(codigo, nombre, raza, sexo, fecnac, dniPropietario, imagen)
        }

        cursor.close()
        db.close()

        return animal
    }


    fun insertUsuario(usuario: String, contrasena: String): String {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USUARIO, usuario)
        values.put(COLUMN_CONTRASENA, contrasena)

        val resultado = db.insert(TABLE_NAME_USU, null, values)

        if(resultado==-1.toLong()) {
            return "fallo al insertar";
        } else {
            return "Datos insertados (ok)"
        }
    }

    // Insertar un nuevo animal en la tabla "animales"
    fun insertAnimal( codigo: String, nombre: String, raza: String, sexo: String, fecnac: String, dniPropietario: String): String {
        val db = this.writableDatabase // prepara la base de datos editable
        val contenedordeValores = ContentValues()
        contenedordeValores.put(COLUMN_CODIGO, codigo)
        contenedordeValores.put(COLUMN_NOMBRE, nombre)
        contenedordeValores.put(COLUMN_RAZA, raza)
        contenedordeValores.put(COLUMN_SEXO, sexo)
        contenedordeValores.put(COLUMN_FECNAC, fecnac)
        contenedordeValores.put(COLUMN_DNI, dniPropietario)
        contenedordeValores.put(COLUMN_IMG, randomImage())
        val resultado = db.insert(TABLE_NAME_ANI, null, contenedordeValores)
        // Verificar que los campos estén llenos
        if (codigo.isEmpty() || nombre.isEmpty() || raza.isEmpty() || sexo.isEmpty() || fecnac.isEmpty() || dniPropietario.isEmpty()) {
            return "Debes rellenar todos los campos"
        }
        // Verificar si hubo algún error al insertar
        if (resultado == -1L) {
            return "Error al insertar el animal"
        } else {
            return "Animal insertado correctamente"
        }

        //db.close()
    }
    private fun randomImage(): String {
        val list = mutableListOf(
            "ayud",
            "betho",
            "botas",
            "felix",
            "garfield",
            "isidoro",
            "milu",
            "pepa",
            "pluto",
            "scooby",
            "snoopy"
        )
        return list.random()
    }

    // READ
// below method is to get
    // all data from our database
    fun getName(): Cursor? {
        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        return db.rawQuery("SELECT * FROM " + TABLE_NAME_ANI, null)
    }


    // UPDATE
    fun updateAnimal(codigo: String, nombre: String, raza: String, sexo: String, fecnac: String, dniPropietario: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NOMBRE, nombre)
        contentValues.put(COLUMN_RAZA, raza)
        contentValues.put(COLUMN_SEXO, sexo)
        contentValues.put(COLUMN_FECNAC, fecnac)
        contentValues.put(COLUMN_DNI, dniPropietario)
        val success = db.update(TABLE_NAME_ANI, contentValues, "$COLUMN_CODIGO=?", arrayOf(codigo)).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }
    // DELETE
    fun deleteAnimal(codigo: String): Boolean {
        val db = this.writableDatabase
        val whereClause = "$COLUMN_CODIGO=?"
        val whereArgs = arrayOf(codigo)
        val result = db.delete(TABLE_NAME_ANI, whereClause, whereArgs)
        return result != -1
    }






}
