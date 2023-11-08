package com.example.chatequipefrance.activities

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.chatequipefrance.R
import com.example.chatequipefrance.models.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream
import java.util.UUID


class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null


    lateinit var ivUser: ShapeableImageView
    private lateinit var layoutTextInputName: TextInputLayout
    private lateinit var layoutTextInputEmail: TextInputLayout
    lateinit var btnSave: MaterialButton
    var isImageChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        ivUser = findViewById(R.id.ivUser)
        layoutTextInputName = findViewById(R.id.layoutTextInputName)
        layoutTextInputEmail = findViewById(R.id.layoutTextInputEmail)
        btnSave = findViewById(R.id.btnSave)

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                Glide.with(this).load(it).placeholder(R.drawable.ic_avatar).into(ivUser)
                isImageChanged = true
            }
        }

        ivUser.setOnClickListener {
            pickImage.launch("image/*")
        }

        auth = Firebase.auth
        db = Firebase.firestore
        currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users").document(currentUser!!.uid).get()
                .addOnSuccessListener { result ->
                    if (result != null) {
                        var user = result.toObject(User::class.java)
                        user?.let {
                            user.uuid = currentUser!!.uid
                            setUserData(user)

                        }
                    }
                }
        } else {
            Log.d("SettingsActivity", "No user found")
        }
    }

    private fun setUserData(user: User) {
        layoutTextInputName.editText?.setText(user.fullname)
        layoutTextInputEmail.editText?.setText(user.email)

        user.image?.let{
            Glide.with(this).load(it).placeholder(R.drawable.ic_avatar).into(ivUser)
        }

        btnSave.setOnClickListener {
            layoutTextInputName.isErrorEnabled = false

            if (isImageChanged) {
                uploadImageToFirebaseStorage(user)
            } else if(layoutTextInputName.editText?.text.toString() != user.fullname) {
                updateUserInfo(user)
            } else{
                Toast.makeText(this, "Vos informations sont mises à jour", Toast.LENGTH_LONG).show()
                layoutTextInputName.clearFocus()
            }
        }
    }

    private fun uploadImageToFirebaseStorage(user: User) {

        // Create a storage reference from our app
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/${user.uuid}")

        val bitmap = (ivUser.drawable as BitmapDrawable).bitmap

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        // upload the byte to firebase storage
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                user.image = uri.toString()
                updateUserInfo(user)
            }

        }
    }

    private fun updateUserInfo(user: User) {
        var updatedUser = hashMapOf<String, Any>(
            "fullname" to layoutTextInputName.editText?.text.toString(),
            "image" to (user.image ?: "")
        )

        db.collection("users").document(user.uuid).update(updatedUser)
            .addOnSuccessListener {
                Toast.makeText(this, "Vos informations sont mises à jour", Toast.LENGTH_LONG).show()


            }.addOnFailureListener {
                layoutTextInputName.error = "Une erreur s'est produite, veuillez réessayer plus tard"
                layoutTextInputName.isErrorEnabled = true
            }
    }
}
