package com.example.mycomplaintapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycomplaintapp.databinding.ItemComplaintsBinding
import com.example.mycomplaintapp.interfaces.ComplaintClickListener
import com.example.mycomplaintapp.models.ComplaintsModel


/**
 * Created by Hilal Ahmad
 * on 7/25/21 11:52 AM
 **/

class ComplaintsAdapter(
    private val complaintsList: List<ComplaintsModel>,
    private val complaintClickListener: ComplaintClickListener
): RecyclerView.Adapter<ComplaintsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemComplaintsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {
            tvDeptName.text = "DEPARTMENT | ${complaintsList[position].deptName}"
            tvUserName.text = complaintsList[position].userName
            tvContactNumber.text = complaintsList[position].userContact
            tvComplaintTitle.text = complaintsList[position].complaintSubject
            tvComplaintDetails.text = complaintsList[position].complaintDetails

            tvOpenComplaint.setOnClickListener {
                complaintClickListener.onComplaintClick(complaintsList[position])
            }
        }
    }

    override fun getItemCount() = complaintsList.size


   inner class ViewHolder(val binding: ItemComplaintsBinding): RecyclerView.ViewHolder(binding.root)


}