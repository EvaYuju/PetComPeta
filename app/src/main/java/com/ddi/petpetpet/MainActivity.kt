package com.ddi.petpetpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ddi.petpetpet.databinding.ActivityMainBinding
import com.ddi.petpetpet.db.DatabaseHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class MainActivity : AppCompatActivity() {
    companion object {
        private const val ADMIN_USERNAME = "admin@gmail.com"
        private const val ADMIN_PASSWORD = "admin1234"

    }
    lateinit var usuarioActual : String
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Vincula la vista principal a este código
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //mAuth = FirebaseAuth.getInstance()

        val editTextEmail = binding.usuario
        val editTextPassword = binding.contrasena
        val botonLogin = binding.loginButton
        val botonRegistro = binding.botonCambiaRegistro
        usuarioActual = binding.usuario.toString();
        botonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese su correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // El login fue exitoso, realiza la acción correspondiente
                            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                            if(email.equals("admin@gmail.com")) {
                                // Genera el contenedor de datos que llamaremos intent
                                val intent = Intent(this, RegistroAnimalesActivity::class.java)
                                // Introduce en el contenedor el dato que pasamos a la otra actividad
                                intent.putExtra("Usuario", email)
                                usuarioActual = email;
                                // Llama a la otra actividad
                                startActivity(intent)
                            } else {
                                // Genera el contenedor de datos que llamaremos intent
                                val intent = Intent(this, ReciclerViewActivity::class.java)
                                // Introduce en el contenedor el dato que pasamos a la otra actividad
                                intent.putExtra("Usuario", email)
                                // Llama a la otra actividad
                                usuarioActual = email;
                                startActivity(intent)
                            }

                        } else {
                            // El login falló, muestra un mensaje de error al usuario
                            Toast.makeText(this, "Error al iniciar sesión, por favor verifique su correo y contraseña", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        botonRegistro.setOnClickListener {
            val intent = Intent(this, MainActivityRegistro::class.java )

            // Llama a la otra actividad
            startActivity(intent)
        }



    }//onCreate
}//main