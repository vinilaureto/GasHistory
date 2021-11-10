package com.vinilaureto.gashistory.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Register (
    var date: Date? = null,
    var price: Double? = null
) : Parcelable