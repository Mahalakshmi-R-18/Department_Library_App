package com.example.mca_lib_1

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class UpdateUser : AppCompatActivity() {
    private lateinit var userIdSpinner: Spinner
    private lateinit var nameEt: EditText
    private lateinit var phoneNumberEt: EditText
    private lateinit var updateButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        userIdSpinner = findViewById(R.id.userIdSpinner)
        nameEt = findViewById(R.id.nameEt)
        phoneNumberEt = findViewById(R.id.phoneNumberEt)
        updateButton = findViewById(R.id.updateButton)

        // Fetch user IDs from all three collections and populate the spinner
        fetchUserIdsFromAdmin()

        updateButton.setOnClickListener {
            updateUserDetails()
        }
    }

    private fun fetchUserIdsFromAdmin() {
        val userIdList = mutableListOf<String>()
        // Fetch user IDs from the admin collection
        db.collection("admin")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val userId = document.getString("userId")
                    if (userId != null) {
                        userIdList.add(userId)
                    }
                }
                // After fetching from admin collection, fetch from faculty collection
                fetchUserIdsFromFaculty(userIdList)
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Toast.makeText(this, "Failed to fetch user IDs from admin: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchUserIdsFromFaculty(userIdList: MutableList<String>) {
        // Fetch user IDs from the faculty collection
        db.collection("faculty")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val userId = document.getString("userId")
                    if (userId != null) {
                        userIdList.add(userId)
                    }
                }
                // After fetching from faculty collection, fetch from student collection
                fetchUserIdsFromStudent(userIdList)
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Toast.makeText(this, "Failed to fetch user IDs from faculty: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchUserIdsFromStudent(userIdList: MutableList<String>) {
        // Fetch user IDs from the student collection
        db.collection("student")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val userId = document.getString("userId")
                    if (userId != null) {
                        userIdList.add(userId)
                    }
                }
                // All user IDs fetched, populate the spinner
                populateSpinner(userIdList)
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Toast.makeText(this, "Failed to fetch user IDs from student: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun populateSpinner(userIdList: MutableList<String>) {
        // Populate the spinner with fetched user IDs
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userIdList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userIdSpinner.adapter = adapter
    }

    private fun updateUserDetails() {
        val userId = userIdSpinner.selectedItem.toString()
        val newName = nameEt.text.toString()
        val newPhoneNumber = phoneNumberEt.text.toString()

        // Determine the collection based on the[ selected user ID

        val collectionName = when {
            userId.startsWith("A", ignoreCase = true) -> "admin"
            userId.startsWith("C", ignoreCase = true) -> "faculty"
            userId.matches(Regex("\\d{2}MX\\d{3}.*")) -> "student"
            else -> null // Handle other cases if needed
        }

        // Log to check if the collectionName is not null
        Log.d("UpdateUser", "Collection Name: $collectionName")

        // If the collection name is not null, update user details in the corresponding collection
        collectionName?.let { collection ->
            db.collection(collection)
                .document(userId)
                .update(mapOf(
                    "name" to newName,
                    "phoneNumber" to newPhoneNumber
                ))
                .addOnSuccessListener {
                    Toast.makeText(this, "User details updated successfully", Toast.LENGTH_SHORT).show()
                    Log.d("UpdateUser", "User details updated successfully")
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to update user details: ${exception.message}", Toast.LENGTH_SHORT).show()
                    Log.d("UpdateUser", "Failed to update user details: ${exception.message}")
                }
        }
    }
}
