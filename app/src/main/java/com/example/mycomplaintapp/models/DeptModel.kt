package com.example.mycomplaintapp.models

import java.io.Serializable

data class DeptModel(
    val deptName: String,
    val deptDesc: String,
    val deptImage: String
) : Serializable