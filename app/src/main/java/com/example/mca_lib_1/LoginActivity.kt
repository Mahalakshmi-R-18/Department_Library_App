package com.example.mca_lib_1

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialize views
        emailEditText = findViewById(R.id.emailEt)
        passwordEditText = findViewById(R.id.passET)

        val loginButton: Button = findViewById(R.id.loginbutton)
        loginButton.setOnClickListener {
            loginUser()
        }

        val newUserButton: Button = findViewById(R.id.btn) // Updated button ID
        newUserButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        // Perform input validation
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        // Sign in with email and password using Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "Login successful", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser


                    val intent = when {
                        user?.email?.matches(Regex(".*admin.*")) == true -> {
                            Intent(this, AdminActivity::class.java)
                        }

                        user?.email?.matches(Regex("[a-zA-Z]{3}\\.mca.*")) == true -> {
                            Intent(this, FacultyActivity::class.java)
                        }

                        user?.email?.matches(Regex("\\d{2}mx\\d{3}@.*")) == true -> {
                            Intent(this, StudentActivity::class.java)
                        }

                        else -> {
                            Log.e(TAG, "Invalid User")
                            return@addOnCompleteListener
                        }
                    }
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Login failed. Please check your credentials.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}

