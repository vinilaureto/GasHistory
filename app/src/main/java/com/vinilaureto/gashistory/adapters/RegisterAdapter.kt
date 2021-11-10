package com.vinilaureto.gashistory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vinilaureto.gashistory.databinding.LayoutRegisterBinding
import com.vinilaureto.gashistory.entities.Register

class RegisterAdapter (
    val localContext: Context,
    val layout: Int,
    val registersList: MutableList<Register>
): ArrayAdapter<Register>(localContext, layout, registersList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val registerLayoutView: View
        if (convertView != null) {
            registerLayoutView = convertView
        } else {
            val layoutRegisterBinding = LayoutRegisterBinding.inflate(localContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
            ) as LayoutInflater
            )
            with(layoutRegisterBinding) {
                root.tag = RegisterLayoutHolder(registerDateTv, registerValueTv)
                registerLayoutView = layoutRegisterBinding.root
            }
        }

        val register = registersList[position]

        val registerLayoutHolder = registerLayoutView.tag as RegisterLayoutHolder
        with(registerLayoutHolder) {
            dateTv.text = register.date.toString()
            valueTv.text = "R$ ${register.price.toString()}"
        }

        return registerLayoutView
    }

    private data class RegisterLayoutHolder(
        val dateTv: TextView,
        val valueTv: TextView,
    )
}