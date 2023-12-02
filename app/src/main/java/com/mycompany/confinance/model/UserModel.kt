package com.mycompany.confinance.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    var id: Long? = null,
    var name:String?,
    var email:String?,
    val password:String?,
    var photo: Int?
) : Parcelable
