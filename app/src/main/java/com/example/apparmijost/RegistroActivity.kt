package com.example.apparmijost

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.apparmijost.databinding.ActivityMainBinding
import com.example.apparmijost.databinding.ActivityRegistroBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding
    private lateinit var mensaje:String
    var id="nada"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inicializar()

        binding.btnChange.setOnClickListener {
            var origen = binding.edtSalida.text.toString()
            var destino = binding.edtDestino.text.toString()
            binding.edtSalida.setText(destino)
            binding.edtDestino.setText(origen)
        }

        binding.btnGuardar.setOnClickListener {
            val pasajero = binding.edtPasajero.text.toString()
            val salida = binding.edtSalida.text.toString()
            val destino = binding.edtDestino.text.toString()
            val dia = binding.edtDia.text.toString()
            val hora = binding.edtHora.text.toString()

            val pasaje = Pasaje(pasajero,salida,destino,dia,hora,id)
            if (id.equals("nada")){
                addPasaje(pasaje)
            }else{
                updatePasaje(pasaje)
            }
            val intent = Intent(baseContext,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDatePickerDialog(){
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datepicker")
    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        val realMonth = month+1
        binding.edtDia.setText("$day/$realMonth/$year")
    }

    fun inicializar(){
        binding.edtDia.setOnClickListener{
            showDatePickerDialog()
        }
        val bundle = intent.extras
        bundle?.let {
            val pasaje = bundle.getSerializable("key_pasaje") as Pasaje
            binding.btnGuardar.setText("Actualizar")
            id = pasaje.id
            binding.edtPasajero.setText(pasaje.pasajero)
            binding.edtSalida.setText(pasaje.salida)
            binding.edtDestino.setText(pasaje.destino)
            binding.edtDia.setText(pasaje.dia)
            binding.edtHora.setText(pasaje.hora)
        }?: run {
            binding.btnGuardar.setText("Agregar")
            binding.edtPasajero.setText("")
            binding.edtSalida.setText("")
            binding.edtDestino.setText("")
            binding.edtDia.setText("")
            binding.edtHora.setText("")
        }
        binding.edtPasajero.requestFocus()
    }

    fun addPasaje(pasaje: Pasaje){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://apparmijos-default-rtdb.firebaseio.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DaoPasaje::class.java)

        val retrofit = retrofitBuilder.saveNewPasaje(pasaje)
        retrofit.enqueue(
            object : Callback<Pasaje> {
                override fun onFailure(call: Call<Pasaje>, t: Throwable) {
                    mensaje = "Error al guardar"
                }
                override fun onResponse( call: Call<Pasaje>, response: Response<Pasaje>) {
                    mensaje = "Pasaje guardado con éxito"
                }
            }
        )
    }

    fun updatePasaje(pasaje: Pasaje){
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://apparmijos-default-rtdb.firebaseio.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DaoPasaje::class.java)

        val retrofit = retrofitBuilder.updatePasaje(pasaje.id,pasaje)
        retrofit.enqueue(
            object : Callback<Pasaje> {
                override fun onFailure(call: Call<Pasaje>, t: Throwable) {
                    mensaje = "Error al guardar"
                }
                override fun onResponse( call: Call<Pasaje>, response: Response<Pasaje>) {
                    mensaje = "Pasaje guardado con éxito"
                }
            }
        )
    }
}