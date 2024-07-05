package com.example.mca_lib_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView

class ReturnBookFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_return_book, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.return_grid_recycler_view)
        val adapter = ImageAdapter()
        recyclerView.adapter = adapter

        return view
    }

    private class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image2, parent, false)
            return ImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val imageResource = imageResources[position]
            holder.imageView.setImageResource(imageResource)

            holder.actionButton.setOnClickListener {
                Toast.makeText(holder.itemView.context, "Book returned", Toast.LENGTH_SHORT).show()
            }
        }

        override fun getItemCount(): Int {
            return imageResources.size
        }

        inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.image_view2)
            val actionButton: Button = itemView.findViewById(R.id.return_button)
        }
    }
}
