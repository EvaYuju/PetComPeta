package com.ddi.petpetpet.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ddi.petpetpet.databinding.ItemAnimalBinding
import com.ddi.petpetpet.db.modelos.Animal

class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemAnimalBinding.bind(view)

    fun render(animalModel: Animal) {
        binding.ivfoto.setImageResource(binding.root.context.resources.getIdentifier(animalModel.imagen,"drawable",binding.root.context.packageName))
        binding.tvNombre.text = animalModel.nombre
        binding.tvCodigo.text = animalModel.codigo
    }
}