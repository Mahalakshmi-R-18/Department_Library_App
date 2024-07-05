package com.example.mca_lib_1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class UpdateBook : AppCompatActivity() {
    private lateinit var bookIdSpinner: Spinner
    private lateinit var titleEt: EditText
    private lateinit var authorEt: EditText
    private lateinit var qtyEt: EditText
    private lateinit var updateButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_book)

        // Initialize views
        bookIdSpinner = findViewById(R.id.bookIdSpinner)
        titleEt = findViewById(R.id.titleEt)
        authorEt = findViewById(R.id.authorEt)
        qtyEt = findViewById(R.id.qtyEt)
        updateButton = findViewById(R.id.updateButton)

        // Fetch book IDs and populate the spinner
        fetchBookIds()

        updateButton.setOnClickListener {
            updateBookDetails()
        }
    }

    private fun fetchBookIds() {
        val bookIdList = mutableListOf<String>()

        // Fetch book IDs from the books collection
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

    private fun updateBookDetails() {
        val bookId = bookIdSpinner.selectedItem.toString()
        val newTitle = titleEt.text.toString()
        val newAuthor = authorEt.text.toString()
        val newQty = qtyEt.text.toString()

        // Update book details in the books collection
        db.collection("books")
            .document(bookId)
            .update(
                mapOf(
                    "bookTitle" to newTitle,
                    "author" to newAuthor,
                    "bookQty" to newQty
                )
            )
            .addOnSuccessListener {
                Toast.makeText(this, "Book details updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to update book details: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
