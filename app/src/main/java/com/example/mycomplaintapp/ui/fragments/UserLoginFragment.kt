package com.example.mycomplaintapp.ui.fragments

import android.os.Bundle
import android.util.Log
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


class UserLoginFragment : Fragment() {

    private lateinit var binding: FragmentUserLoginBinding

    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
    }

    private lateinit var savedStateHandle: SavedStateHandle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle.set(LOGIN_SUCCESSFUL, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserLoginBinding.inflate(inflater,container,false)

        var name = ""

        binding.etLoginEmail.addTextChangedListener {
            name = it.toString()
        }

        binding.btnLogin.setOnClickListener {

            if (name == "hilal@gmail.com") {
                val toast = Toast.makeText(context, "Name: $name", Toast.LENGTH_LONG)
                toast.show()
                savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                findNavController().navigate(R.id.navigate_login_to_home)
            } else {
                val toast = Toast.makeText(context, "Not Hilal", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        binding.tvRegisterHere.setOnClickListener {
            findNavController().navigate(R.id.navigate_login_to_register)
        }

        return binding.root
    }

}