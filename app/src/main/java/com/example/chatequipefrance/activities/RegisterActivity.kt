package com.example.chatequipefrance.activities


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.core.content.ContextCompat
import com.example.chatequipefrance.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    lateinit var layoutTextInputName: TextInputLayout
    lateinit var layoutTextInputEmail: TextInputLayout
    lateinit var layoutTextInputPassword: TextInputLayout
    lateinit var layoutTextInputConfirmPassword: TextInputLayout
    lateinit var btnRegister: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialisation de  Firebase Auth
        auth = Firebase.auth

        layoutTextInputName = findViewById(R.id.layoutTextInputName)
        layoutTextInputEmail = findViewById(R.id.layoutTextInputEmail)
        layoutTextInputPassword = findViewById(R.id.layoutTextInputPassword)
        layoutTextInputConfirmPassword = findViewById(R.id.layoutTextInputConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {

            // Initialiser les erreurs
            initErrors()

            val name = layoutTextInputName.editText?.text.toString()
            val email = layoutTextInputEmail.editText?.text.toString()
            val password = layoutTextInputPassword.editText?.text.toString()
            val confirmPassword = layoutTextInputConfirmPassword.editText?.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                if (name.isEmpty()) {
                    layoutTextInputName.error = "Le nom complet est requis"
                    layoutTextInputName.isErrorEnabled = true
                }
                if (email.isEmpty()) {
                    layoutTextInputEmail.error = "L'adresse e-mail est requise"
                    // activer l'erreur
                    layoutTextInputEmail.isErrorEnabled = true
                }
                if (password.isEmpty()) {
                    layoutTextInputPassword.error = "Le mot de passe est requis"
                    layoutTextInputPassword.isErrorEnabled = true
                }
                if (confirmPassword.isEmpty()) {
                    layoutTextInputConfirmPassword.error = "La confirmation du mot de passe est requise"
                    layoutTextInputConfirmPassword.isErrorEnabled = true
                }
            } else {
                if (password != confirmPassword) {
                    layoutTextInputConfirmPassword.error = "Les mots de passe ne correspondent pas"
                    layoutTextInputConfirmPassword.isErrorEnabled = true
                } else {
                    // creation d'un utilisateur dans le module authentification de firebase
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Create a new user with a first and last name
                                val user = hashMapOf(
                                    "fullname" to name,
                                    "email" to email
                                )
                                val currrentUser = auth.currentUser

                                // creation de l'utilisateur dans le module Firestore
                                val db = Firebase.firestore
                                // Add a new document with a generated ID
                                db.collection("users").document(currrentUser!!.uid).set(user).addOnSuccessListener {
                                        Intent(this, HomeActivity::class.java).also {
                                            startActivity(it)
                                        }

                                    }.addOnFailureListener {
                                        layoutTextInputConfirmPassword.error = "Une erreur s'est produite, veuillez réessayer plus tard"
                                        layoutTextInputConfirmPassword.isErrorEnabled = true
                                    }
                            } else {
                                layoutTextInputConfirmPassword.error = "Une erreur s'est produite, veuillez réessayer plus tard"
                                layoutTextInputConfirmPassword.isErrorEnabled = true
                            }
                        }
                }
            }

        }

    }

    private fun initErrors() {
        layoutTextInputName.isErrorEnabled = false
        layoutTextInputEmail.isErrorEnabled = false
        layoutTextInputPassword.isErrorEnabled = false
        layoutTextInputConfirmPassword.isErrorEnabled = false
    }
}