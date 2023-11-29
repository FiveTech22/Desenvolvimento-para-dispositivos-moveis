package com.mycompany.confinance.model

data class MovementUpdate(
    val description: String?,
    val photo: Int? = 0,
    val value: Long? = null,
    val date: String?,
    val fixedIncome: Boolean?,
    val recurrenceFrequency: String?,
    val recurrenceIntervals: Int?,
)