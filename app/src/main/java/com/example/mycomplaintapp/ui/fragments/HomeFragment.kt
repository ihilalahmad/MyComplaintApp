package com.example.mycomplaintapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.mycomplaintapp.R
import com.example.mycomplaintapp.databinding.FragmentHomeBinding
import com.example.mycomplaintapp.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var mAuth: FirebaseAuth
    private var mUser: FirebaseUser? = null
    private lateinit var firebaseDatabase: DatabaseReference
    private lateinit var userID: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initFirebase()
        if (mAuth.currentUser == null) {
            findNavController().navigate(R.id.action_navigation_home_to_userLoginFragment)
        }else {
            getUserDataFromFirebase()
        }
        return binding!!.root
    }

    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users")
        if (mUser != null) {
            userID = mUser!!.uid
        }
    }

    private fun getUserDataFromFirebase() {
        firebaseDatabase.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userModel : UserModel? = snapshot.getValue(UserModel::class.java)

                if (userModel != null) {

                    val userName = userModel.userName
                    val userEmail = userModel.userEmail

                    binding!!.tvUserId.text = userID
                    binding!!.tvUserName.text = userName
                    binding!!.tvUserEmail.text = userEmail
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}