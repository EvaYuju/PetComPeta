package com.ddi.petpetpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration

import androidx.recyclerview.widget.LinearLayoutManager
import com.ddi.petpetpet.adapter.AnimalAdapter
import com.ddi.petpetpet.databinding.ActivityReciclerViewBinding
import com.ddi.petpetpet.db.modelos.Animal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReciclerViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReciclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReciclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val usuario = intent.getStringExtra("Usuario")
        binding.btnVolver.isVisible = usuario.equals("admin@gmail.com")
        binding.usuarioLogeado2.text = "Usuario: $usuario"
        initRecyclerView()



        binding.btnVolver.setOnClickListener {
            // Genera el contenedor de datos que llamaremos intent
            val intent = Intent(this, RegistroAnimalesActivity::class.java )
            // Introduce en el contenedor el dato que pasamos a la otra actividad
            intent.putExtra("Usuario",usuario)
            // Llama a la otra actividad
            startActivity(intent)
        }
    }
    private fun initRecyclerView() {
        // Aquí devolvemos la lista de animales una vez que se hayan cargado todos los datos
        // Accedemos .su nombre real(id)
        // Se le indica el tipo espaciado a utilizar (lista de una columna))
        binding.recyclerAnimal.layoutManager = LinearLayoutManager(this)

        val db = FirebaseDatabase.getInstance().getReference("Animal")
        val listaAnimales = mutableListOf<Animal>()

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val animal = childSnapshot.getValue(Animal::class.java)
                    animal?.let {
                        listaAnimales.add(animal)
                    }
                }
                // Se le indica qué clase Adapter se encarga de proveer los datos al RecyclerView
                binding.recyclerAnimal.adapter = AnimalAdapter(listaAnimales, binding.usuarioLogeado2.text.replace(Regex("Usuario: "),""))

            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error aquí
            }
        })
    }
}