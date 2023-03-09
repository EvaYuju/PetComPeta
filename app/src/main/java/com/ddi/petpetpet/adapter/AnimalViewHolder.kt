package com.ddi.petpetpet.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ddi.petpetpet.MainActivity
import com.ddi.petpetpet.VerActivity
import com.ddi.petpetpet.databinding.ItemAnimalBinding
import com.ddi.petpetpet.db.modelos.Animal

class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemAnimalBinding.bind(view)

    init {
        view.setOnClickListener {
            val context = binding.root.context
            val intent = Intent(context, VerActivity::class.java)

            intent.putExtra("usuario", binding.tvUsuario.text.toString())
            intent.putExtra("codigo", binding.tvCodigo.text.toString())
            context.startActivity(intent)
        }
    }
    fun render(animalModel: Animal) {
        binding.ivfoto.setImageResource(binding.root.context.resources.getIdentifier(animalModel.imagen,"drawable",binding.root.context.packageName))
        binding.tvNombre.text = animalModel.nombre
        binding.tvCodigo.text = animalModel.codigo
        binding.tvUsuario.text = animalModel.usuario
    }
}