package com.example.mycomplaintapp.interfaces

import com.example.mycomplaintapp.models.ComplaintsModel

interface ComplaintClickListener {
    fun onComplaintClick(complaintsModel: ComplaintsModel)
}