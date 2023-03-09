package com.ddi.petpetpet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ddi.petpetpet.R
import com.ddi.petpetpet.db.modelos.Animal

class AnimalAdapter(private val animalList: List<Animal>, val usuario : String) : RecyclerView.Adapter<AnimalViewHolder>  (){
    // Encargado de generar ViewHolders a medida que vayan haciendo falta
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AnimalViewHolder(layoutInflater.inflate(R.layout.item_animal, parent, false))
    }
    // Al desplazar los datos arriba/abajo, dime qué dato va a ocupar el ViewHolder que ya
    // no se ve en la pantalla (para reutilizarlo) y en qué posición del RecyclerView se muestra
    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val item = animalList[position]
        item.usuario = usuario

        holder.render(item)
    }
    // Sabe el número máximo de datos que han de presentarse e informa a los ViewHolders de ello
    override fun getItemCount(): Int = animalList.size

}
