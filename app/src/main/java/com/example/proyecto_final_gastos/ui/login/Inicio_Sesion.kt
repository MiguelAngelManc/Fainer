package com.example.proyecto_final_gastos.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.proyecto_final_gastos.PrincipalActivity

import com.example.proyecto_final_gastos.R
import com.example.proyecto_final_gastos.RegistroActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_inicio__sesion.*
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.activity_registro.btnRegistro

class Inicio_Sesion : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_inicio__sesion)

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            var email: String = txtCorreoLogin.text.toString()
            var password: String = txtPassLogin.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Favor de llenar los campos", Toast.LENGTH_LONG).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Bienvenido", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, PrincipalActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Usuario o contraseña incorrecta",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }
        }

        txtRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)

            startActivity(intent)
        }

    }
}