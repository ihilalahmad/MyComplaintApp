package com.example.mycomplaintapp.models

import java.io.Serializable


/**
 * Created by Hilal Ahmad
 * on 7/25/21 11:49 AM
 **/

data class ComplaintsModel(
    val userId: String = "",
    val userName: String = "",
    val userContact: String = "",
    val deptName: String = "",
    val complaintSubject: String = "",
    val complaintDetails: String = ""
): Serializable