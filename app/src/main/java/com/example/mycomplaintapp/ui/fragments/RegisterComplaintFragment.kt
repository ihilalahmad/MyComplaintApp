package com.example.mycomplaintapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.mycomplaintapp.R
import com.example.mycomplaintapp.databinding.FragmentRegisterComplaintBinding
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register_complaint.*

class RegisterComplaintFragment : Fragment() {

    private lateinit var binding: FragmentRegisterComplaintBinding
    private val mArgs: RegisterComplaintFragmentArgs by navArgs()
    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var deptName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentRegisterComplaintBinding.inflate(inflater, container, false)

        initialInit()

        binding.btnSubmitComplaint.setOnClickListener {
            submitComplaint()
        }

        return binding.root
    }

    private fun initialInit() {

        deptName = mArgs.department.deptName
        userId = mArgs.userId
        userName = mArgs.userName
        Log.i("User", "User userId: $userId userName $userName ")

        binding.tvDeptName.text = "You are making complaint to ''${deptName}'' department"
        binding.etUserName.setText(userName)
    }

    private fun submitComplaint() {
        if (isFormValidated()) {

            binding.regComplaintProgressbar.visibility = View.VISIBLE
            binding.btnSubmitComplaint.visibility = View.GONE

            val hashMap: HashMap<String, String> = HashMap()

            val userName = binding.etUserName.text.toString()
            val userContact = binding.etUserContact.text.toString()
            val complaintSubject = binding.etComplaintSubject.text.toString()
            val complaintDetails = binding.etComplaintDetails.text.toString()

            hashMap["deptName"] = deptName
            hashMap["complaintDetails"] = complaintDetails
            hashMap["complaintSubject"] = complaintSubject
            hashMap["userContact"] = userContact
            hashMap["userId"] = userId
            hashMap["userName"] = userName

            FirebaseDatabase.getInstance().getReference("Complaints")
                .push()
                .setValue(hashMap)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Complaint submitted successfully", Toast.LENGTH_SHORT).show()
                        binding.regComplaintProgressbar.visibility = View.GONE
                        binding.btnSubmitComplaint.visibility = View.VISIBLE
                        binding.etUserContact.setText("")
                        binding.etComplaintSubject.setText("")
                        binding.etComplaintDetails.setText("")
                    }
                }.addOnFailureListener {
                    Toast.makeText(context, "Complaint submission error: ${it.message}", Toast.LENGTH_SHORT).show()
                    binding.regComplaintProgressbar.visibility = View.GONE
                    binding.btnSubmitComplaint.visibility = View.VISIBLE
                }
        }
    }

    private fun isFormValidated(): Boolean {

        if (binding.etUserName.text.toString().isEmpty()) {
            binding.etUserName.error = "Please enter name"
            binding.etUserName.requestFocus()
            return false
        }

        if (binding.etUserContact.text.toString().isEmpty()) {
            binding.etUserContact.error = "Please enter contact number"
            binding.etUserContact.requestFocus()
            return false
        }

        if (binding.etComplaintSubject.text.toString().isEmpty()) {
            binding.etComplaintSubject.error = "Please enter complaint subject"
            binding.etComplaintSubject.requestFocus()
            return false
        }

        if (binding.etComplaintDetails.text.toString().isEmpty()) {
            binding.etComplaintDetails.error = "Please enter complaint details"
            binding.etComplaintDetails.requestFocus()
            return false
        }

        return true
    }
}