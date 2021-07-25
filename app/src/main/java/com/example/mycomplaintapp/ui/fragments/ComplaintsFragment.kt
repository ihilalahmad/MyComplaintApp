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
import com.example.mycomplaintapp.databinding.FragmentDashboardBinding
import com.example.mycomplaintapp.interfaces.ComplaintClickListener
import com.example.mycomplaintapp.models.ComplaintsModel
import com.example.mycomplaintapp.ui.adapters.ComplaintsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ComplaintsFragment : Fragment(), ComplaintClickListener {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var mAuth: FirebaseAuth
    private val complaintsList = ArrayList<ComplaintsModel>()
    private lateinit var adapter: ComplaintsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()
        setupRecyclerView()
        getComplaintsFromFirebase()

        return binding.root
    }

    private fun setupRecyclerView() {

        binding.complaintsRecyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.setHasFixedSize(true)
        }
    }

    private fun getComplaintsFromFirebase() {

        binding.complaintProgressbar.visibility = View.VISIBLE

        FirebaseDatabase.getInstance()
            .getReference("Complaints")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    complaintsList.clear()

                    for (complaintsSnapShot in snapshot.children) {

                        val complaints = complaintsSnapShot.getValue(ComplaintsModel::class.java)

                        val userId = mAuth.currentUser!!.uid

                        if (complaints!!.userId == userId) {
                            complaintsList.add(complaints)
                        }
                    }
                    adapter = ComplaintsAdapter(complaintsList, this@ComplaintsFragment)
                    binding.complaintsRecyclerView.adapter = adapter
                    binding.complaintProgressbar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.complaintProgressbar.visibility = View.GONE
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onComplaintClick(complaintsModel: ComplaintsModel) {
        val bundle = Bundle().apply {
            putSerializable("complaint", complaintsModel)
        }
        findNavController().navigate(R.id.navigate_complaints_to_chat, bundle)
    }
}