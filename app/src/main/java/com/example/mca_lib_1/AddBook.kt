package com.example.mca_lib_1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddBook : AppCompatActivity() {
    private lateinit var accessionNumberEt: TextInputEditText
    private lateinit var bookIdEt: TextInputEditText
    lateinit var bookTitleEt: TextInputEditText
    lateinit var authorEt: TextInputEditText
    private lateinit var bookQtyEt: TextInputEditText
    private lateinit var isbnEt: TextInputEditText
    private lateinit var subjectEt: TextInputEditText
    private lateinit var bookImageView: ImageView

    private var selectedImageUri: Uri? = null
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        // Initialize views
        accessionNumberEt = findViewById(R.id.accessionNumberEt)
        bookIdEt = findViewById(R.id.bookIdEt)
        bookTitleEt = findViewById(R.id.bookTitleEt)
        authorEt = findViewById(R.id.authorEt)
        bookQtyEt = findViewById(R.id.bookQtyEt)
        isbnEt = findViewById(R.id.isbnEt)
        subjectEt = findViewById(R.id.subjectEt)
        bookImageView = findViewById(R.id.bookImageView)

        storageReference = FirebaseStorage.getInstance().reference

        // Set up click listener for the add book button
        val addBookButton: Button = findViewById(R.id.addBookButton)
        addBookButton.setOnClickListener {
            addBookToDatabase()
        }

        // Set up click listener for selecting an image
        val selectImageButton: Button = findViewById(R.id.selectImageButton)
        selectImageButton.setOnClickListener {
            selectImage()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                selectedImageUri = it // Capture the URI of the selected image
                bookImageView.setImageURI(selectedImageUri) // Set the selected image to the ImageView
            }
        }
    }

    private fun addBookToDatabase() {
        // Get other details of the book
        val accessionNumber = accessionNumberEt.text.toString()
        val bookId = bookIdEt.text.toString()
        val bookTitle = bookTitleEt.text.toString()
        val author = authorEt.text.toString()
        val bookQty = bookQtyEt.text.toString()
        val isbn = isbnEt.text.toString()
        val subject = subjectEt.text.toString()

        // Check if an image is selected
        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        // Upload image to Firebase Storage
        val imageRef = storageReference.child("images/${UUID.randomUUID()}")
        val uploadTask = imageRef.putFile(selectedImageUri!!)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully, get the download URL
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Create a new book object
                val book = hashMapOf(
                    "bookId" to bookId,
                    "accessionNumber" to accessionNumber,
                    "bookTitle" to bookTitle,
                    "author" to author,
                    "bookQty" to bookQty,
                    "isbn" to isbn,
                    "subject" to subject,
                    "imageUrl" to uri.toString()
                )

                // Add the book to Firestore with the document ID as the bookId
                val db = FirebaseFirestore.getInstance()
                db.collection("books")
                    .document(bookId)
                    .set(book)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot added with ID: $bookId")
                        // Handle success, if needed
                        Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                        // Handle failure, if needed
                        Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error uploading image", e)
            Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        private const val TAG = "AddBook"
        private const val IMAGE_PICK_REQUEST = 1
    }
}
