package com.example.apparmijost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apparmijost.databinding.ItemPasajeBinding

class PasajeAdapter(var pasajes:List<Pasaje> = emptyList()): RecyclerView.Adapter<PasajeAdapter.PasajeAdapterViewHolder>() {
    lateinit var  setOnClickListenerPasajeEdit:(Pasaje) -> Unit
    lateinit var  setOnClickListenerPasajeDelete:(Pasaje) -> Unit

    inner class PasajeAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val pasajero:TextView = itemView.findViewById(R.id.txtPasajeroItem)
        val salida:TextView = itemView.findViewById(R.id.txtSalidaItem)
        val destino:TextView = itemView.findViewById(R.id.txtDestinoItem)
        val dia:TextView = itemView.findViewById(R.id.txtDiaItem)
        val hora:TextView = itemView.findViewById(R.id.txtHoraItem)
        val buttonEditar:ImageButton = itemView.findViewById(R.id.btnEditarItem)
        val buttonEliminar:ImageButton = itemView.findViewById(R.id.btnEliminarItem)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasajeAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pasaje, parent, false)
        return PasajeAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PasajeAdapterViewHolder, position: Int) {
        val currentitem = pasajes[position]

        holder.pasajero.text = currentitem.pasajero
        holder.salida.text = currentitem.salida
        holder.destino.text = currentitem.destino
        holder.dia.text = currentitem.dia
        holder.hora.text = currentitem.hora

        holder.buttonEditar.setOnClickListener {
            setOnClickListenerPasajeEdit(currentitem)
        }

        holder.buttonEliminar.setOnClickListener {
            setOnClickListenerPasajeDelete(currentitem)
        }

    }

    override fun getItemCount(): Int {
        return pasajes.size
    }

    fun updateListPasajes(pasajes:List<Pasaje>){
        this.pasajes = pasajes
        notifyDataSetChanged()
    }
}