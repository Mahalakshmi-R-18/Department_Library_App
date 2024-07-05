package com.example.mca_lib_1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class UserManagementFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_management, container, false)
        val addUsersImageView = view.findViewById<ImageView>(R.id.addUsersImageView)
        addUsersImageView.setOnClickListener {
            val intent = Intent(requireActivity(), SignUpActivity::class.java)
            startActivity(intent)
        }
        val updateUsersImageView = view.findViewById<ImageView>(R.id.updateUsersImageView)
        updateUsersImageView.setOnClickListener {
            val intent = Intent(requireActivity(), UpdateUser::class.java)
            startActivity(intent)
        }
        val deactivateUsersImageView = view.findViewById<ImageView>(R.id.deactivateUsersImageView)
        deactivateUsersImageView.setOnClickListener {
            val intent = Intent(requireActivity(), DeleteUser::class.java)
            startActivity(intent)
        }
        return view
    }
}
