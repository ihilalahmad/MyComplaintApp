package com.example.mycomplaintapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mycomplaintapp.R
import com.example.mycomplaintapp.databinding.DeptListItemBinding
import com.example.mycomplaintapp.interfaces.DeptClickListener
import com.example.mycomplaintapp.models.DeptModel

class DeptAdapter(
        private val deptList: List<DeptModel>,
        private val deptClickListener: DeptClickListener
) : RecyclerView.Adapter<DeptAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val binding = DeptListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvDeptName.text = deptList[position].deptName
            tvDeptDesc.text = deptList[position].deptDesc

            tvRegisterComplaint.setOnClickListener {
                deptClickListener.onDeptClick(deptList[position])
            }
        }


    }

    override fun getItemCount() = deptList.size


    inner class ViewHolder(val binding: DeptListItemBinding) : RecyclerView.ViewHolder(binding.root)
}