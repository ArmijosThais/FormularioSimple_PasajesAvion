package com.example.apparmijost

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apparmijost.databinding.ActivityMainBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    private val database = Firebase.database
    private lateinit var messagesListener: ValueEventListener
    private val listPasajes:MutableList<Pasaje> = ArrayList()
    val myRef = database.getReference("Pasajes")
    private val adapter: PasajeAdapter by lazy {
        PasajeAdapter()
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewPasajes: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        database = Firebase.database.reference

        recyclerViewPasajes = findViewById(R.id.rvPasajes)
        recyclerViewPasajes.layoutManager = LinearLayoutManager(this)
        recyclerViewPasajes.setHasFixedSize(true)

        binding.btnRegistrar.setOnClickListener{ v ->
            val intent = Intent(this, RegistroActivity::class.java)
            v.context.startActivity(intent)
        }
        listPasajes.clear()
        setupRecyclerView(binding.rvPasajes)



    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        messagesListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listPasajes.clear()
                dataSnapshot.children.forEach { child ->
                    val pasaje: Pasaje? =
                        child.key?.let {
                            Pasaje(
                                child.child("pasajero").getValue().toString(),
                                child.child("salida").getValue().toString(),
                                child.child("destino").getValue().toString(),
                                child.child("dia").getValue().toString(),
                                child.child("hora").getValue().toString(),
                                it
                            )
                        }
                    pasaje?.let { listPasajes.add(it) }
                }
                adapter.updateListPasajes(listPasajes)
                recyclerView.adapter = adapter

                adapter.setOnClickListenerPasajeDelete = {
                    deletePasaje(it)
                }
                adapter.setOnClickListenerPasajeEdit = {
                    val bundle = Bundle().apply {
                        putSerializable("key_pasaje",it)
                    }
                    val intent = Intent(baseContext,RegistroActivity::class.java).apply {
                        putExtras(bundle)
                    }
                    startActivity(intent)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        myRef.addValueEventListener(messagesListener)
    }

    fun deletePasaje(pasaje: Pasaje){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://apparmijos-default-rtdb.firebaseio.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DaoPasaje::class.java)

        val dialog = AlertDialog.Builder(this)
            .setTitle("El registro se eliminará permanentemente")
            .setMessage("¿Desea continuar?")
            .setNegativeButton("Cancelar"){ view, _ ->
                Toast.makeText(this, "Acción cancelada", Toast.LENGTH_SHORT).show()
                view.dismiss()
            }
            .setPositiveButton("Continuar", DialogInterface.OnClickListener{
                    dialog, which ->
                val retrofit = retrofitBuilder.deletePasaje(pasaje.id)
                retrofit.enqueue( object :Callback<Pasaje>{
                    override fun onResponse(call: Call<Pasaje>, response: Response<Pasaje>) {}
                    override fun onFailure(call: Call<Pasaje>, t: Throwable) {}
                })
            })
            .setCancelable(false)
            .create()
        dialog.show()
    }

}



