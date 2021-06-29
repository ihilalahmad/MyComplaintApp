package com.example.mycomplaintapp.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.mycomplaintapp.R
import com.example.mycomplaintapp.databinding.FragmentUserRegistrationBinding
import com.example.mycomplaintapp.models.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRegistrationFragment : Fragment() {

    private lateinit var binding: FragmentUserRegistrationBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserRegistrationBinding.inflate(inflater,container,false)

        binding.tvLoginHere.setOnClickListener {
            findNavController().navigate(R.id.navigate_register_to_login)
        }

        binding.btnRegister.setOnClickListener {
            registerUserWithFirebase()
        }

        return binding.root
    }

    private fun registerUserWithFirebase() {

        //getting data from edittexts
//        var userName = ""
//        var userEmail = ""
//        var userPassword = ""
//
//        binding.etRegName.addTextChangedListener {
//            userName = it.toString()
//        }
//
//        binding.etRegEmail.addTextChangedListener {
//            userEmail = it.toString()
//        }
//
//        binding.etRegPassword.addTextChangedListener {
//            userPassword = it.toString()
//        }

        //validation registration form
        if (validateForm()) {
            mAuth = FirebaseAuth.getInstance()
            binding.regProgressBar.visibility = View.VISIBLE
            mAuth.createUserWithEmailAndPassword(binding.etRegEmail.text.toString(),binding.etRegPassword.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userModel = UserModel(
                            binding.etRegName.text.toString(),
                            binding.etRegEmail.text.toString(),
                            binding.etRegPassword.text.toString())
                        saveUserInFirebaseDatabase(userModel)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "User Registration error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun saveUserInFirebaseDatabase(userModel: UserModel) {
        FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser.uid)
            .setValue(userModel)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "User saved successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Saving user error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateForm() : Boolean {

        //validation registration form
        if (binding.etRegName.text.toString().isEmpty()) {
            binding.etRegName.error = "User name required"
            binding.etRegName.requestFocus()
            return false
        }

        if (binding.etRegEmail.text.toString().isEmpty()) {
            binding.etRegEmail.error = "User name required"
            binding.etRegEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etRegEmail.text.toString()).matches()) {
            binding.etRegEmail.error = "Please enter a valid email"
            binding.etRegEmail.requestFocus()
            return false
        }

        if (binding.etRegPassword.text.toString().isEmpty()) {
            binding.etRegPassword.error = "User name required"
            binding.etRegPassword.requestFocus()
            return false
        }

        if (binding.etRegPassword.text.toString().length < 6) {
            binding.etRegPassword.error = "Min password length should be 6 characters!"
            binding.etRegPassword.requestFocus()
            return false
        }

        return true
    }
}