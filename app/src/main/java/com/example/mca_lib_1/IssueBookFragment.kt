package com.example.mca_lib_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.Toast
import android.widget.TextView

class IssueBookFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_issue_book, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.image_grid_recycler_view)
        val adapter = ImageAdapter()
        recyclerView.adapter = adapter

        return view
    }

    private class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
        // Assuming you have an array or list of image resources
        private val imageResources = intArrayOf(
            R.drawable.book1,
            R.drawable.book3,
            R.drawable.book11,
            R.drawable.book4,
            R.drawable.book7,
            R.drawable.book8,
            R.drawable.book6,
            R.drawable.book5,
            R.drawable.book10,
            R.drawable.book12,
            R.drawable.book2,
            R.drawable.book9,
        )

        private val imageNames = arrayOf(
            "Book 1",
            "Book 3",
            "Book 11",
            "Book 4",
            "Book 7",
            "Book 8",
            "Book 6",
            "Book 5",
            "Book 10",
            "Book 12",
            "Book 2",
            "Book 9",
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val imageResource = imageResources[position]
            holder.imageView.setImageResource(imageResource)
            holder.imageName.text = imageNames[position]

            // Handle click events on the "Issue Book" button
            holder.actionButton.setOnClickListener {
                Toast.makeText(holder.itemView.context, "Issue book clicked for ${imageNames[position]}", Toast.LENGTH_SHORT).show()
                // Handle the issue book action here
            }
        }

        override fun getItemCount(): Int {
            return imageResources.size
        }

        inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.image_view)
            val actionButton: Button = itemView.findViewById(R.id.issue_button)
            val imageName: TextView = itemView.findViewById(R.id.image_name)
        }
    }
}
