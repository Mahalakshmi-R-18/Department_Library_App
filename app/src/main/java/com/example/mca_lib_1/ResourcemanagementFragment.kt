package com.example.mca_lib_1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class ResourcemanagementFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resourcemanagement, container, false)
        val addUsersImageView = view.findViewById<ImageView>(R.id.addBookImageView)
        addUsersImageView.setOnClickListener {
            val intent = Intent(requireActivity(), AddBook::class.java)
            startActivity(intent)
        }
        val updateBookImageView = view.findViewById<ImageView>(R.id.updateBookImageView)
        updateBookImageView.setOnClickListener {
            val intent = Intent(requireActivity(), UpdateBook::class.java)
            startActivity(intent)
        }
        val removeBookImageView = view.findViewById<ImageView>(R.id.removeBookImageView)
        removeBookImageView.setOnClickListener {
            val intent = Intent(requireActivity(), DeleteBook::class.java)
            startActivity(intent)
        }
        val fineImageView = view.findViewById<ImageView>(R.id.fineImageView)
        fineImageView.setOnClickListener {
            val intent = Intent(requireActivity(), BookFine::class.java)
            startActivity(intent)
        }
        return view
    }
}
