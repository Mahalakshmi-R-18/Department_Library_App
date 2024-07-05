package com.example.mca_lib_1

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class DeleteUser : AppCompatActivity() {
    private lateinit var userIdSpinner: Spinner
    private lateinit var deleteButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_user)

        userIdSpinner = findViewById(R.id.spinnerUserIdDelete)
        deleteButton = findViewById(R.id.buttonDeleteUser)

        // Fetch user IDs and populate the spinner
        fetchUserIds()

        deleteButton.setOnClickListener {
            deleteUser()
        }
    }

    private fun fetchUserIds() {
        val userIdList = mutableListOf<String>()

        // Fetch user IDs from all three collections
        fetchUserIdsFromCollection("admin", userIdList)
        fetchUserIdsFromCollection("faculty", userIdList)
        fetchUserIdsFromCollection("student", userIdList)
    }

    private fun fetchUserIdsFromCollection(collection: String, userIdList: MutableList<String>) {
        db.collection(collection)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val userId = document.getString("userId")
                    userId?.let {
                        userIdList.add(it)
                    }
                }
                // After fetching user IDs, populate the spinner
                populateSpinner(userIdList)
            }
            .addOnFailureListener { exception ->
                Log.e("DeleteUser", "Failed to fetch user IDs from $collection: ${exception.message}")
                Toast.makeText(this, "Failed to fetch user IDs from $collection: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun populateSpinner(userIdList: List<String>) {
        // Populate the spinner with fetched user IDs
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userIdList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userIdSpinner.adapter = adapter
    }

    private fun deleteUser() {
        val userId = userIdSpinner.selectedItem.toString()

        // Determine the collection based on the selected user ID
        val collectionName = when {
            userId.startsWith("A", ignoreCase = true) -> "admin"
            userId.startsWith("C", ignoreCase = true) -> "faculty"
            userId.matches(Regex("\\d{2}MX\\d{3}.*")) -> "student"
            else -> null
        }

        if (collectionName != null) {
            db.collection(collectionName)
                .document(userId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show()
                    // Optionally, you can refresh the spinner after deletion
                    fetchUserIds()
                }
                .addOnFailureListener { exception ->
                    Log.e("DeleteUser", "Failed to delete user: ${exception.message}")
                    Toast.makeText(this, "Failed to delete user: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.e("DeleteUser", "Collection name is null")
        }
    }
}
