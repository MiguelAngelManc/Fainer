package com.example.proyecto_final_gastos

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.proyecto_final_gastos.ui.login.Inicio_Sesion
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.fragment_detalle_mes.view.*
import java.io.BufferedReader
import java.io.FileOutputStream

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        auth = FirebaseAuth.getInstance()

        btnRegistro.setOnClickListener {
            var email: String = txtCorreo.text.toString()
            var password: String = txtPass.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Favor de llenar los campos", Toast.LENGTH_LONG).show()
            } else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener{ task ->
                    if(task.isSuccessful){
                        guardarInterno()
                        Toast.makeText(this, "Registrado", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, PrincipalActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, "Correo ya utilizado o contraseña muy debil", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        btnIniciarSesion.setOnClickListener {
            val intent = Intent(this, Inicio_Sesion::class.java)

            startActivity(intent)
        }
    }

    fun guardarInterno(){

        var cuerpo : String = txtNombre.text.toString()
        var titulo : String = auth.currentUser!!.email!!

            val fos : FileOutputStream = openFileOutput("$titulo.txt", Context.MODE_PRIVATE)
            fos.write(cuerpo.toByteArray())
            fos.close()
        }
    }



