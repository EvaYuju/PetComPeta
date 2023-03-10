package com.ddi.petpetpet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ddi.petpetpet.databinding.ActivityRegistroAnimalBinding
import com.ddi.petpetpet.db.DatabaseHelper
import com.ddi.petpetpet.db.modelos.Animal
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*

class RegistroAnimalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroAnimalBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var db : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Vincula a la vista secundaria este código
        binding = ActivityRegistroAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Crear la base de datos
        dbHelper = DatabaseHelper(this, null)
        db = FirebaseDatabase.getInstance().getReference("Animal")


        // Recupera del Intent el nombre de usuario que inició esta actividad
        val usuario = intent.getStringExtra("Usuario")
        // Actualiza el TextView con el nombre de usuario
        binding.usuarioLogeado.text = "Usuario: $usuario"

        // Asignar escuchadores de clic a los botones
        // Botón insertar
        binding.btnAlta.setOnClickListener {
            // Código para guardar el registro
            val codigo = binding.ptCodigo.text.toString()
            val nombre = binding.ptNombre.text.toString()
            val raza = binding.ptRaza.text.toString()
            val sexo = binding.ptSexo.text.toString()
            val fecnac = binding.ptFecNac.text.toString()
            val dni = binding.ptDNI.text.toString()
            val imagen = randomImage()

                // Verificar si todos los campos están llenos
            if (codigo.isNotEmpty() && nombre.isNotEmpty() && raza.isNotEmpty() && sexo.isNotEmpty() && fecnac.isNotEmpty() && dni.isNotEmpty()) {

                val ani = Animal(codigo,nombre,raza,fecnac,sexo,dni,imagen,usuario.toString())

                db.child(codigo).setValue(ani).addOnSuccessListener {
                    limpiar()
                    Snackbar.make(binding.root, "Datos insertados", Snackbar.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Snackbar.make(
                        binding.root,
                        "La conexión ha fallado",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                limpiar()
                }
            else {
                // notificación
                Snackbar.make(
                    binding.root,
                    "Todos los campos son obligatorios",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }


        // Botón actualizar
        binding.btnModif.setOnClickListener {
            // Código para modificar el registro
            // Obtenemos los nuevos valores de los campos
            val codigo = binding.ptCodigo.text.toString()
            val nombre = binding.ptNombre.text.toString()
            val raza = binding.ptRaza.text.toString()
            val sexo = binding.ptSexo.text.toString()
            val fecnac = binding.ptFecNac.text.toString()
            val dni = binding.ptDNI.text.toString()
            var imagen = "";

            if(codigo == null || codigo == ""){
                Snackbar.make(binding.root, "No se encontró el registro", Snackbar.LENGTH_SHORT).show()
            } else if (codigo.isEmpty() || nombre.isEmpty() || raza.isEmpty()|| sexo.isEmpty() || fecnac.isEmpty() || dni.isEmpty()) {
                Snackbar.make(binding.root, "Debe rellenar todos los campos", Snackbar.LENGTH_SHORT).show()

            }else {
                db.child(codigo).addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val animal = snapshot.getValue(Animal::class.java)
                            if (animal != null) {
                                imagen = animal.imagen
                            }
                        } else {
                            // Si no se encuentra un animal con el código introducido, mostrar un mensaje de error.
                            Snackbar.make(binding.root, "No se encontraron resultados", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                     override fun onCancelled(error: DatabaseError) {
                    }

                })

                val ani = Animal(codigo,nombre,raza,fecnac,sexo,dni,imagen,usuario.toString())

                db.child(codigo).setValue(ani).addOnSuccessListener {
                    Snackbar.make(binding.root, "Registro modificado", Snackbar.LENGTH_SHORT).show()
                }.addOnFailureListener() {
                    Snackbar.make(binding.root, "No se encontró el registro", Snackbar.LENGTH_SHORT)
                        .show()
                }
                limpiar()
            }


            limpiar()
        }


        // Botón borrar
        binding.btnBorrar.setOnClickListener {
            val codigo = binding.ptCodigo.text.toString().trim()
            if(codigo == null || codigo == ""){
                Snackbar.make(binding.root, "No se encontró el registro", Snackbar.LENGTH_SHORT).show()
            } else {

                db.child(codigo).removeValue().addOnSuccessListener {
                    Snackbar.make(binding.root, "Registro borrado", Snackbar.LENGTH_SHORT).show()
                }.addOnFailureListener() {
                    Snackbar.make(binding.root, "No se encontró el registro", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
            limpiar()
        }

        // Botón leer
        // Botón consulta
        binding.btnConsul.setOnClickListener {
            // Código para consultar el registro
            val codigo = binding.ptCodigo.text.toString().trim()

            db.child(codigo).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val animal = snapshot.getValue(Animal::class.java)
                        val nombre = animal?.nombre
                        val raza = animal?.raza
                        val fecNac = animal?.fecnac
                        val sexo = animal?.sexo
                        val dni = animal?.dni

                        binding.ptDNI.setText(dni)
                        binding.ptNombre.setText(nombre)
                        binding.ptRaza.setText(raza)
                        binding.ptFecNac.setText(fecNac)
                        binding.ptSexo.setText(sexo)

                        Snackbar.make(binding.root, "Datos cargados", Snackbar.LENGTH_SHORT).show()
                    } else {
                        // Si no se encuentra un animal con el código introducido, mostrar un mensaje de error.
                        Snackbar.make(binding.root, "No se encontraron resultados", Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }

        // Botón consulta todos
        binding.btnConsulTodo.setOnClickListener {
            val intent = Intent(this, ReciclerViewActivity::class.java)
            // Introduce en el contenedor el dato que pasamos a la otra actividad
            intent.putExtra("Usuario", usuario)
            // Llama a la otra actividad
            startActivity(intent)

        }
    }
    // método general para limpiar los campos
    private fun limpiar() {
        binding.ptCodigo.setText("")
        binding.ptNombre.setText("")
        binding.ptRaza.setText("")
        binding.ptSexo.setText("")
        binding.ptFecNac.setText("")
        binding.ptDNI.setText("")
    }
    // Método con lista de url de imágenes de forma random
    private fun randomImage(): String {
        val list = mutableListOf(
            "https://s1.eestatic.com/2020/08/26/curiosidades/mascotas/mascotas-perros-gatos_515959375_158488465_1706x960.jpg",
            "https://t2.ea.ltmcdn.com/es/posts/2/7/6/_24672_1_600.jpg",
            "https://i.pinimg.com/originals/ac/70/fb/ac70fbacaf1bbaa60f57654868995679.jpg",
            "https://supercurioso.com/wp-content/uploads/2014/07/el-perro-mas-feo-del-mundo-696x385.jpg",
            "https://res.cloudinary.com/postedin/image/upload/d_mascotas:no-image.jpg,w_340,c_thumb,f_auto,q_80/mascotas/e392d5a.jpg",
            "https://i.blogs.es/a4d31e/sims-4-bug/840_560.jpeg",
            "https://i.ebayimg.com/images/g/A0MAAMXQTghRLmEp/s-l500.jpg",
            "https://elcomercio.pe/resizer/0pMUlR_T98x1Vx8HdhcRFqr95TE=/1200x800/smart/filters:format(jpeg):quality(75)/arc-anglerfish-arc2-prod-elcomercio.s3.amazonaws.com/public/ETBB5OFWBZCWTHTXCLMPNCMI5Q.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTw1ayZVQR1eAlj8AczE75wVsMGD9wVX_hdjQ&usqp=CAU",
            "https://images7.memedroid.com/images/UPLOADED803/5e7fa33f785ee.jpeg",
            "https://images.ecestaticos.com/J5pFdl2mm0ef9Y19ON0gtdw8E-c=/0x91:747x651/1200x899/filters:fill(white):format(jpg)/f.elconfidencial.com%2Foriginal%2F003%2F218%2F4ff%2F0032184ffd5d997c8994e4823ea4b21b.jpg",
            "https://i.pinimg.com/564x/73/ed/3e/73ed3e93762cb6e17874dbfcb9971598.jpg",
            "https://i.pinimg.com/originals/dd/d2/bb/ddd2bb3f0fb29f6daf9bc391ed564f17.jpg",
            "https://www.pintzap.com/storage/img/memegenerator/templates/perro-depresivo.webp",
            "https://static.wikia.nocookie.net/memes-pedia/images/3/3d/Nelson_the_Bull_Terrier.jpg/revision/latest?cb=20221019210527&path-prefix=es"
        )
        return list.random()
    }
}