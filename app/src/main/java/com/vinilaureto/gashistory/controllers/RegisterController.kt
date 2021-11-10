package com.vinilaureto.gashistory.controllers

import com.vinilaureto.gashistory.entities.Register
import com.vinilaureto.gashistory.entities.RegisterDao
import com.vinilaureto.gashistory.entities.RegisterFirebase
import java.util.*

class RegisterController {
    private val registerDatabase: RegisterDao = RegisterFirebase()

    fun newRegister(register: Register) = registerDatabase.createRegister(register)
    fun findOneRegister(date: Date) = registerDatabase.findRegister(date)
    fun findAllRegister() = registerDatabase.findAllRegister()
    fun updateRegister(register: Register) = registerDatabase.updateRegister(register)
    fun removeRegister(date: Date) = registerDatabase.deleteRegister(date)
}