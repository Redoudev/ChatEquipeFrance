package com.example.chatequipefrance.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatequipefrance.R
import com.example.chatequipefrance.adapters.FriendsRecyclerAdapter
import com.example.chatequipefrance.models.Friend
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.util.Objects

class HomeActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser

    lateinit var rvFriends: RecyclerView
    lateinit var fabChat: FloatingActionButton

    lateinit var friendsRecyclerAdapter: FriendsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rvFriends = findViewById(R.id.rvFriends)
        fabChat = findViewById(R.id.fabChat)

        auth = Firebase.auth
        db = Firebase.firestore
        currentUser = auth.currentUser!!

        fabChat.setOnClickListener {
            Intent(this, UsersSearchActivity::class.java).also {
                startActivity(it)
            }

        }

    }

    override fun onResume() {
        super.onResume()

        val friends = mutableListOf<Friend>()

        friendsRecyclerAdapter = FriendsRecyclerAdapter()

        rvFriends.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = friendsRecyclerAdapter
        }

        // Récupérer tous les derniers messages de friends
        db.collection("users")
            .document(currentUser.uid)
            .collection("friends").get()
            .addOnSuccessListener { result ->
                for(document in result){
                    val friend = document.toObject(Friend::class.java)
                    friend.uuid = document.id
                    friends.add(friend)
                }
                friendsRecyclerAdapter.items = friends
            }.addOnFailureListener {
                Log.e("HomeActivity", "error reading list friends", it)
            }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.itemSetting) {
            Intent(this, SettingsActivity::class.java).also {
                startActivity(it)
            }
        }
        if (item.itemId == R.id.itemLogout) {
            val auth = Firebase.auth
            auth.signOut()
            Intent(this, AuthentificationActivity::class.java).also {
                startActivity(it)
            }
            finish()
        }


        return super.onOptionsItemSelected(item)
    }
}