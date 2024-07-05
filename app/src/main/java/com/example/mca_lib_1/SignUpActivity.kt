package com.example.mca_lib_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var name: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPass: TextInputEditText
    private lateinit var userType: Spinner
    private lateinit var userId: TextInputEditText
    private lateinit var phoneNumber: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        // Initialize views
        name = findViewById(R.id.nameEt)
        email = findViewById(R.id.emailEt)
        password = findViewById(R.id.passET)
        confirmPass = findViewById(R.id.confirmPassEt)
        userType = findViewById(R.id.userTypeSpinner)
        userId = findViewById(R.id.facultyStudentIdEt)
        phoneNumber = findViewById(R.id.phoneNumberEt)

        // Set up click listener for the sign-up button
        val signUpButton: Button = findViewById(R.id.button)
        signUpButton.setOnClickListener {
            signUpUser()
        }

        // Set up click listener for the existing user button
        val existingButton: Button = findViewById(R.id.button3)
        existingButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser() {
        // Get the text from the TextInputEditText and Spinner
        val userName = name.text.toString()
        val userEmail = email.text.toString()
        val userPassword = password.text.toString()
        val userConfirmPassword = confirmPass.text.toString()
        val userUserType = userType.selectedItem.toString()
        val userId = userId.text.toString()
        val userPhoneNumber = phoneNumber.text.toString()

        //Phone number validations
        if (!isValidPhoneNumber(userPhoneNumber)) {
            Toast.makeText(this, "Invalid phone number. Please enter 10 digits.", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if passwords match
        if (userPassword != userConfirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new user account with Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up success
                    Toast.makeText(this, "User registration successful", Toast.LENGTH_SHORT).show()

                    // Store user data in Firestore
                    storeUserDataInFirestore(userName, userEmail, userPhoneNumber, userId, userUserType)

                    // Update user profile with additional information
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    currentUser?.let { user ->
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(userName)
                            .build()
                        user.updateProfile(profileUpdates)
                            .addOnCompleteListener { updateProfileTask ->
                                if (updateProfileTask.isSuccessful) {
                                    // Redirect based on username
                                    when {
                                        userName.startsWith("a", ignoreCase = true) -> {
                                            // Redirect to admin activity
                                            startActivity(Intent(this, AdminActivity::class.java))
                                        }
                                        userName.startsWith("c", ignoreCase = true) -> {
                                            // Redirect to faculty activity
                                            startActivity(Intent(this, FacultyActivity::class.java))
                                        }
                                        userName.matches(Regex("\\d{2}mx\\d{3}.*")) -> {
                                            // Redirect to student activity
                                            startActivity(Intent(this, StudentActivity::class.java))
                                        }
                                    }
                                    finish() // Finish current activity after redirection
                                } else {
                                    // Handle profile update failure
                                    Toast.makeText(this, "Failed to update user profile", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    // Sign up failed, display a message to the user.
                    Toast.makeText(this, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun storeUserDataInFirestore(
        userName: String,
        userEmail: String,
        userPhoneNumber: String,
        userId: String,
        userUserType: String
    ) {
        val db = FirebaseFirestore.getInstance()

        val user = hashMapOf(
            "name" to userName,
            "email" to userEmail,
            "phoneNumber" to userPhoneNumber,
            "userId" to userId,
            "userType" to userUserType
        )

        // Add user data to appropriate Firestore collection based on user type
        when (userUserType.toLowerCase()) {
            "admin" -> {
                db.collection("admin").document(userId)
                    .set(user)
                    .addOnSuccessListener {
                        Log.d(TAG, "Admin document added")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding admin document", e)
                    }
            }
            "faculty" -> {
                db.collection("faculty").document(userId)
                    .set(user)
                    .addOnSuccessListener {
                        Log.d(TAG, "Faculty document added")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding faculty document", e)
                    }
            }
            "student" -> {
                db.collection("student").document(userId)
                    .set(user)
                    .addOnSuccessListener {
                        Log.d(TAG, "Student document added")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding student document", e)
                    }
            }
            else -> {
                Log.e(TAG, "Unknown user type: $userUserType")
            }
        }
    }


    companion object {
        private const val TAG = "SignUpActivity"
    }
}
private fun isValidPhoneNumber(phoneNumber: String): Boolean {
    return phoneNumber.length == 10 && phoneNumber.all { it.isDigit() }
}