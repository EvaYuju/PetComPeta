package com.ddi.petpetpet

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddi.petpetpet.adapter.AnimalAdapter
import com.ddi.petpetpet.databinding.ActivityReciclerViewBinding
import com.ddi.petpetpet.db.DatabaseHelper
import com.ddi.petpetpet.modelos.AnimalList

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
        // Accedemos .su nombre real(id)
        // Se le indica el tipo espaciado a utilizar (lista de una columna))
        binding.recyclerAnimal.layoutManager = LinearLayoutManager(this)
        // Se le indica qué clase Adapter se encarga de proveer los datos al RecyclerView
        val db = DatabaseHelper(this, null)
        binding.recyclerAnimal.adapter = AnimalAdapter(AnimalList.getLista(db))
    }



}