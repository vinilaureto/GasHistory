package com.vinilaureto.gashistory.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vinilaureto.gashistory.databinding.LayoutRegisterBinding
import com.vinilaureto.gashistory.entities.Register
import java.text.DecimalFormat
import java.util.*

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
        var date = register?.date
        val cal = Calendar.getInstance()
        cal.time = date
        val priceFormat = DecimalFormat("#.###")
        with(registerLayoutHolder) {
            dateTv.text = "${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH)}/${cal.get(Calendar.YEAR)}"
            valueTv.text = "R$ ${priceFormat.format(register.price)}"
        }

        return registerLayoutView
    }

    private data class RegisterLayoutHolder(
        val dateTv: TextView,
        val valueTv: TextView,
    )
}