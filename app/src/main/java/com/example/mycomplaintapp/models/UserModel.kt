package com.example.mycomplaintapp.models

import java.io.Serializable

data class UserModel(
     val userName: String = "",
     val userEmail: String = "",
     val userPassword: String = ""
) : Serializable