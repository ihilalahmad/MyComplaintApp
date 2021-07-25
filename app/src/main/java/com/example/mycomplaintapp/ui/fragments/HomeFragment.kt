package com.example.mycomplaintapp.ui.fragments

import android.os.Bundle
import android.util.Log
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
import com.example.mycomplaintapp.models.UserModel
import com.example.mycomplaintapp.ui.adapters.DeptAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class HomeFragment : Fragment(), DeptClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var userID: String
    private lateinit var userName: String
    private val departmentsList = ArrayList<DeptModel>()
    private lateinit var adapter: DeptAdapter


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
            userID = mAuth.currentUser!!.uid
            setupRecyclerView()
            getDeptsFromFirebase()
            getUserDetailsFromFirebase()
        }
        return binding.root
    }

    private fun initFirebase() {
        mAuth = FirebaseAuth.getInstance()
    }

    private fun setupRecyclerView() {
        binding.deptRecyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.setHasFixedSize(true)
        }
    }

    private fun getDeptsFromFirebase() {

        binding.deptProgressbar.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().getReference("Departments")
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                departmentsList.clear()

                for (deptSnapShot in snapshot.children) {
                    val depts: DeptModel = deptSnapShot.getValue(DeptModel::class.java)!!
                    departmentsList.add(depts)
                }
                adapter = DeptAdapter(departmentsList, this@HomeFragment)
                binding.deptRecyclerView.adapter = adapter
                binding.deptProgressbar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                binding.deptProgressbar.visibility = View.GONE
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getUserDetailsFromFirebase() {

        FirebaseDatabase.getInstance().getReference("Users")
            .child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val user : UserModel = snapshot.getValue(UserModel::class.java)!!
                userName = user.userName
                Log.i("User", "User Details: ${user.userName} ")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDeptClick(deptModel: DeptModel) {

        val bundle = Bundle().apply {
            putSerializable("department", deptModel)
            putSerializable("userId", userID)
            putSerializable("userName", userName)
        }
        findNavController().navigate(R.id.navigate_to_register_complaint, bundle)
    }
}