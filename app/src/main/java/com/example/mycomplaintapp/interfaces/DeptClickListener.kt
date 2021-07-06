package com.example.mycomplaintapp.interfaces

import android.view.View
import com.example.mycomplaintapp.models.DeptModel

interface DeptClickListener {
    fun onDeptClick(view: View, deptModel: DeptModel)
}