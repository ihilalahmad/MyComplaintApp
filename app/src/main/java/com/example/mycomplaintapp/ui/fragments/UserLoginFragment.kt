package com.example.mycomplaintapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.example.mycomplaintapp.R
import com.example.mycomplaintapp.databinding.FragmentUserLoginBinding
import com.google.firebase.auth.FirebaseAuth


class UserLoginFragment : Fragment() {

    private lateinit var binding: FragmentUserLoginBinding
    private lateinit var mAuth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (mAuth.currentUser != null) {
            mAuth.signOut()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserLoginBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener {
            loginUserWithFirebase()
        }

        binding.tvRegisterHere.setOnClickListener {
            findNavController().navigate(R.id.navigate_login_to_register)
        }

        return binding.root
    }

    private fun loginUserWithFirebase() {
        binding.loginProgressBar.visibility = View.VISIBLE
        if (validateLoginForm()) {


            binding.btnLogin.visibility = View.GONE

            mAuth.signInWithEmailAndPassword(
                binding.etLoginEmail.text.toString(),
                binding.etLoginPassword.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.loginProgressBar.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE

                    findNavController().navigate(R.id.navigate_login_to_home)
                }
            }.addOnFailureListener {
                binding.loginProgressBar.visibility = View.GONE
                binding.btnLogin.visibility = View.VISIBLE
                Toast.makeText(context, "Login Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateLoginForm(): Boolean {
        if (binding.etLoginEmail.text.toString().isEmpty()) {
            binding.etLoginEmail.error = "Please enter email"
            binding.etLoginEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(binding.etLoginEmail.text.toString()).matches()) {
            binding.etLoginEmail.error = "Please enter a valid email"
            binding.etLoginEmail.requestFocus()
            return false
        }

        if (binding.etLoginPassword.text.toString().isEmpty()) {
            binding.etLoginPassword.error = "Please enter password"
            binding.etLoginPassword.requestFocus()
            return false
        }

        if (binding.etLoginPassword.text.toString().length < 6) {
            binding.etLoginPassword.error = "Min password length should be 6 characters!"
            binding.etLoginPassword.requestFocus()
            return false
        }

        return true
    }

}