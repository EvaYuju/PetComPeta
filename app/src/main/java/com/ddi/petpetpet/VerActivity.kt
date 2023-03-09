package com.ddi.petpetpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ddi.petpetpet.databinding.ActivityRegistroAnimalBinding
import com.ddi.petpetpet.databinding.ActivityVerBinding
import com.ddi.petpetpet.db.DatabaseHelper
import com.ddi.petpetpet.db.modelos.Animal
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*


class VerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerBinding
    private lateinit var db : DatabaseReference
    private lateinit var usuario : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Vincula a la vista secundaria este código
        binding = ActivityVerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseDatabase.getInstance().getReference("Animal")

        usuario = intent.getStringExtra("usuario").toString()
        val codigo = intent.getStringExtra("codigo")


        if (codigo != null) {
            db.child(codigo).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val animal = snapshot.getValue(Animal::class.java)
                        val nombre = animal?.nombre
                        val raza = animal?.raza
                        val fecNac = animal?.fecnac
                        val sexo = animal?.sexo
                        val dni = animal?.dni
                        binding.codigo.setText(codigo)
                        binding.ptDNI.setText(dni)
                        binding.ptNombre.setText(nombre)
                        binding.ptRaza.setText(raza)
                        binding.ptFecNac.setText(fecNac)
                        binding.ptSexo.setText(sexo)

                        Snackbar.make(binding.root, "Datos cargados", Snackbar.LENGTH_SHORT).show()
                    } else {
                        // Si no se encuentra un animal con el código introducido, mostrar un mensaje de error.
                        Snackbar.make(binding.root, "No se encontraron resultados", Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
        // Actualiza el TextView con el nombre de usuario



        binding.botonVolverRecycler.setOnClickListener {
            // Genera el contenedor de datos que llamaremos intent
            val intent = Intent(this, ReciclerViewActivity::class.java )
            println(usuario)
            intent.putExtra("Usuario", usuario)
            // Llama a la otra actividad
            startActivity(intent)
        }
    }
}