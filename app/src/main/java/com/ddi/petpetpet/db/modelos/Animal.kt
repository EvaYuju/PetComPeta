package com.ddi.petpetpet.db.modelos


data class Animal(
        val codigo: String,
        val nombre: String,
        val raza: String,
        val fecnac: String,
        val sexo: String,
        val dni: String,
        val imagen: String,
        var usuario: String
    )
{
        constructor():this("","","","","","","","")
}


