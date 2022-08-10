package com.example.testchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.testchat.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SignInAct : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        checkAuthState()

        binding.buttonSignIn.setOnClickListener(){
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            if (email.isNotEmpty()&&password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        val intent = Intent (this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
                    }
                }
                } else {
                    Toast.makeText(this,"Error2",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btSignUp.setOnClickListener(){
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            if (email.isNotEmpty()&&password.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        sendEmailVerification(it.result?.user!!)  // ? и !! обязательны чтобы не ныла системы что значение user равно null
                        // ну обновляют данные типа
                        binding.btSignUp.visibility = View.GONE
                    } else {
                        Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this,"Error2",Toast.LENGTH_SHORT).show()
            }
        }





    }

    private fun checkAuthState() {
        if(firebaseAuth.currentUser != null)
        {
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        } else
        {
            binding.btSignUp.visibility = View.VISIBLE
        }
    }


    private fun sendEmailVerification (user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this,
                    "Успешно! Вам выслано письмо на электронную почту для подтверждения регистрации!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}