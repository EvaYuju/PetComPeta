package com.ddi.petpetpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ddi.petpetpet.databinding.ActivityMainBinding
import com.ddi.petpetpet.databinding.ActivityMainRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityRegistro : AppCompatActivity() {

    private lateinit var binding: ActivityMainRegistroBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        val editTextEmail = binding.usuario
        val editTextPassword = binding.contrasena
        val botonLogin = binding.loginButton

        botonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese su correo y contraseña", Toast.LENGTH_SHORT)
                    .show()
            } else {
                CoroutineScope(Dispatchers.IO).launch { registrouser(email, password) }
            }
        }

        binding.botonVuelveInicio.setOnClickListener{
            // Genera el contenedor de datos que llamaremos intent
            val intent = Intent(this, MainActivity::class.java)
            // Llama a la otra actividad
            startActivity(intent)
        }
    }


    private fun registrouser(email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // El login fue exitoso, realiza la acción correspondiente
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT)
                        .show()
                    // Genera el contenedor de datos que llamaremos intent
                    val intent = Intent(this, MainActivity::class.java)
                    // Llama a la otra actividad
                    startActivity(intent)

                } else {
                    // El login falló, muestra un mensaje de error al usuario
                    Toast.makeText(
                        this,
                        "Error al realizar el registro, por favor verifique el correo y contraseña",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }
}