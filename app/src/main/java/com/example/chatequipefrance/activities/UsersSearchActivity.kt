package com.example.chatequipefrance.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatequipefrance.R
import com.example.chatequipefrance.adapters.UsersRecyclerAdapter
import com.example.chatequipefrance.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class UsersSearchActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentUser: FirebaseUser? = null

    lateinit var rvUsers: RecyclerView
    lateinit var editSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_search)

        rvUsers = findViewById(R.id.rvUsers)
        editSearch = findViewById(R.id.editSearch)

        auth = Firebase.auth
        db = Firebase.firestore
        currentUser = auth.currentUser

        val usersRecyclerAdapter = UsersRecyclerAdapter()

        rvUsers.apply {
            layoutManager = LinearLayoutManager(this@UsersSearchActivity)
            adapter = usersRecyclerAdapter
        }

        val users = mutableListOf<User>()

        db.collection("users")
            .whereNotEqualTo("email", currentUser?.email)
            .get()
            .addOnSuccessListener {result ->

            for(document in result){
                val uuid = document.id
                val email = document.getString("email")
                val fullName = document.getString("fullname")
                users.add(User(uuid, email ?:"", fullName ?:"",null))
            }
            usersRecyclerAdapter.items = users
        }.addOnFailureListener { exception ->
            Log.e("UsersearchActivity", "error getting users", exception)
        }




        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                usersRecyclerAdapter.filter.filter(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
}