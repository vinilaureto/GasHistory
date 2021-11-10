package com.vinilaureto.gashistory.entities

import java.util.*

interface RegisterDao {
    fun createRegister(register: Register): Long
    fun findRegister(date: Date): Register
    fun findAllRegister(): MutableList<Register>
    fun updateRegister(register: Register): Int
    fun deleteRegister(date: Date): Int
}