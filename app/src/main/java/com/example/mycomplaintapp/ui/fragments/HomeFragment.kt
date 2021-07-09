package com.example.mycomplaintapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycomplaintapp.R
import com.example.mycomplaintapp.databinding.FragmentHomeBinding
import com.example.mycomplaintapp.interfaces.DeptClickListener
import com.example.mycomplaintapp.models.DeptModel
import com.example.mycomplaintapp.ui.DeptAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class HomeFragment : Fragment(), DeptClickListener {

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
            getDeptsFromFirebase()
            setupRecyclerView()
        }
        return binding!!.root
    }

    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Departments")
        if (mUser != null) {
            userID = mUser!!.uid
        }
    }

    private fun setupRecyclerView() {
        val depts = ArrayList<DeptModel>()
        depts.add(DeptModel("Electricity", "This is electricity dept","this is image"))
        depts.add(DeptModel("Water", "This is water dept","this is image"))
        depts.add(DeptModel("Police", "This is police dept","this is image"))
        depts.add(DeptModel("Income tax", "This is income tax dept","this is image"))
        depts.add(DeptModel("Crime branch", "This is crime branch dept","this is image"))
        depts.add(DeptModel("Education", "This is education dept","this is image"))
        depts.add(DeptModel("Health", "This is health dept","this is image"))

        val adapter = DeptAdapter(depts,this)

        binding!!.deptRecyclerView.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.setHasFixedSize(true)
            it.adapter = adapter
        }

    }

    private fun getDeptsFromFirebase() {
        firebaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onDeptClick(deptModel: DeptModel) {
        val bundle = Bundle().apply {
            putSerializable("department", deptModel)
        }
        findNavController().navigate(R.id.navigate_to_register_complaint,bundle)
    }
}