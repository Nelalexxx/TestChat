package com.example.testchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.example.testchat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        var email = auth.currentUser?.email.toString()
        displayEmailLoggedUser()




        // Write a message to the database
        val database = Firebase.database

        val myRef = database.getReference("message")
        onChangeListener(myRef, email)

        binding.sendMessage.setOnClickListener{
            myRef.setValue(binding.editText.text.toString())



        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.sign_out){
            auth.signOut()
            val intent = Intent (this, SignInAct::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }



    private fun onChangeListener(dref: DatabaseReference, email: String) {
        dref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.chatLayout.append("\n")
                binding.chatLayout.append(email)
                binding.chatLayout.append("   ")
                binding.chatLayout.append(snapshot.value.toString())
                binding.chatLayout.append("\n")


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun displayEmailLoggedUser() {
        val actionBar = supportActionBar
        actionBar?.title = auth.currentUser?.email.toString().removeSuffix("@gmail.com")
    }
}