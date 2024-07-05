package com.example.mca_lib_1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class DeleteBook : AppCompatActivity() {
    private lateinit var bookIdSpinner: Spinner
    private lateinit var deleteButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_book)

        bookIdSpinner = findViewById(R.id.bookIdSpinner)
        deleteButton = findViewById(R.id.deleteButton)

        // Fetch book IDs and populate the spinner
        fetchBookIds()

        deleteButton.setOnClickListener {
            deleteBook()
        }
    }

    private fun fetchBookIds() {
        val bookIdList = mutableListOf<String>()

        // Fetch book IDs from the Firestore collection
        db.collection("books")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val bookId = document.getString("bookId")
                    bookId?.let {
                        bookIdList.add(it)
                    }
                }
                // Populate the spinner with fetched book IDs
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bookIdList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                bookIdSpinner.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to fetch book IDs: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteBook() {
        // Check if any item is selected in the spinner
        if (bookIdSpinner.selectedItem != null) {
            val bookId = bookIdSpinner.selectedItem.toString()

            // Delete the book from the Firestore collection
            db.collection("books")
                .document(bookId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Book deleted successfully", Toast.LENGTH_SHORT).show()
                    // Optionally, you can refresh the spinner after deletion
                    fetchBookIds()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to delete book: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please select a book to delete", Toast.LENGTH_SHORT).show()
        }
    }

}
