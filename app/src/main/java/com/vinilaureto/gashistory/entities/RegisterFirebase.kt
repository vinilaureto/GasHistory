package com.vinilaureto.gashistory.entities

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class RegisterFirebase: RegisterDao {
    companion object {
        private val DB_APP = "gas_history"
    }

    // Referência para o Realtime Datanase - vai criar caso não exista
    private val appRtDb = Firebase.database.getReference(DB_APP)

    // Datasource - Firebase vai atualizar essa lista
    private val registersList = mutableListOf<Register>()

    init {
        appRtDb.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newRegister: Register? = snapshot.value as? Register
                newRegister?.apply {
                    if (registersList.find { it.date.toString() == this.date.toString() } == null) {
                        registersList.add(this)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val editedRegister: Register? = snapshot.value as? Register
                editedRegister?.apply {
                    registersList[registersList.indexOfFirst { it.date.toString() == this.date.toString() }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removedRegister: Register? = snapshot.value as? Register
                removedRegister?.apply {
                    registersList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Não se aplica devido a estrutura da arvore atual
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica devido a estrutura da arvore atual
            }
        })
    }

    override fun createRegister(register: Register): Long {
        createOrUpdateRegister(register)
        return 0L
    }

    override fun findRegister(date: Date): Register {
        return registersList.firstOrNull { it.date.toString() == date.toString() } ?: Register()
    }

    override fun findAllRegister(): MutableList<Register> {
        return registersList
    }

    override fun updateRegister(register: Register): Int {
        createOrUpdateRegister(register)
        return 1
    }

    override fun deleteRegister(date: Date): Int {
        appRtDb.child(date.toString()).removeValue()
        return 1
    }

    private fun createOrUpdateRegister(register: Register) {
        appRtDb.child(register.date.toString()).setValue(register)
    }
}