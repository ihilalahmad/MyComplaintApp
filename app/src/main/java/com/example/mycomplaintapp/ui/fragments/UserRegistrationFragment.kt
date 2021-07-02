package com.example.mycomplaintapp.ui.fragments

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mycomplaintapp.R
import com.example.mycomplaintapp.databinding.FragmentUserRegistrationBinding
import com.example.mycomplaintapp.models.UserModel
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

        binding.regProgressBar.visibility = View.VISIBLE

        //validation registration form
        if (validateRegistrationForm()) {

            binding.btnRegister.visibility = View.GONE

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
                    binding.regProgressBar.visibility = View.GONE
                    binding.btnRegister.visibility = View.VISIBLE
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
                    binding.regProgressBar.visibility = View.GONE
                    binding.btnRegister.visibility = View.VISIBLE
                    findNavController().navigate(R.id.navigate_register_to_login)
                    Toast.makeText(context, "User saved successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                binding.regProgressBar.visibility = View.GONE
                binding.btnRegister.visibility = View.VISIBLE
                Toast.makeText(context, "Saving user error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateRegistrationForm() : Boolean {

        //validation registration form
        if (binding.etRegName.text.toString().isEmpty()) {
            binding.etRegName.error = "Please enter name"
            binding.etRegName.requestFocus()
            return false
        }

        if (binding.etRegEmail.text.toString().isEmpty()) {
            binding.etRegEmail.error = "Please enter email"
            binding.etRegEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etRegEmail.text.toString()).matches()) {
            binding.etRegEmail.error = "Please enter a valid email"
            binding.etRegEmail.requestFocus()
            return false
        }

        if (binding.etRegPassword.text.toString().isEmpty()) {
            binding.etRegPassword.error = "Please enter password"
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