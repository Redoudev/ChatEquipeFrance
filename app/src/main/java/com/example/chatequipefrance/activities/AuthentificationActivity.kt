package com.example.chatequipefrance.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.TextView
import com.example.chatequipefrance.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class AuthentificationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    private lateinit var layoutTextInputEmail: TextInputLayout
    private lateinit var layoutTextInputPassword: TextInputLayout
    private lateinit var btnConnect: MaterialButton
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentification)

        // Initialisation de Firebase Auth
        auth = Firebase.auth

        layoutTextInputEmail = findViewById(R.id.layoutTextInputEmail)
        layoutTextInputPassword = findViewById(R.id.layoutTextInputPassword)
        btnConnect = findViewById(R.id.btnConnect)
        tvRegister = findViewById(R.id.tvRegister)


    }

    override fun onStart() {
        super.onStart()

        btnConnect.setOnClickListener {

            layoutTextInputEmail.isErrorEnabled = false
            layoutTextInputPassword.isErrorEnabled = false

            val email = layoutTextInputEmail.editText?.text.toString()
            val password = layoutTextInputPassword.editText?.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    layoutTextInputEmail.error = "L'adresse e-mail est requise"
                    layoutTextInputEmail.isErrorEnabled = true
                }
                if (password.isEmpty()) {
                    layoutTextInputPassword.error = "Le mot de passe est requis"
                    // Activer l'erreur
                    layoutTextInputPassword.isErrorEnabled = true
                }
            } else {
                signIn(email, password)
            }
        }

        tvRegister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    fun signIn(email: String, password: String) {
        Log.d("singIn", "signIn user....")

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Intent(this, HomeActivity::class.java).also {
                    startActivity(it)
                }
                finish()

            } else {
                layoutTextInputPassword.error = "L'authentification a échoué !"
                layoutTextInputEmail.isErrorEnabled = true
                layoutTextInputPassword.isErrorEnabled = true
            }

        }
    }
}