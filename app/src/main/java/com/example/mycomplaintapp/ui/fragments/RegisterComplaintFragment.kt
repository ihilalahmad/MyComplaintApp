package com.example.mycomplaintapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.mycomplaintapp.R
import com.example.mycomplaintapp.databinding.FragmentRegisterComplaintBinding

class RegisterComplaintFragment : Fragment() {

    private lateinit var binding: FragmentRegisterComplaintBinding
    private val mArgs: RegisterComplaintFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterComplaintBinding.inflate(
                inflater,
                container,
                false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dept = mArgs.department

        binding.tvDeptName.text = dept.deptName
    }

}