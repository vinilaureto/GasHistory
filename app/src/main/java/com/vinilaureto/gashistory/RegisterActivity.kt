package com.vinilaureto.gashistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.vinilaureto.gashistory.databinding.ActivityRegisterBinding
import com.vinilaureto.gashistory.entities.Register
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(activityRegisterBinding.root)

        val register = intent.getParcelableExtra<Register>(MainActivity.EXTRA_REGISTER)
        position = intent.getIntExtra(MainActivity.EXTRA_REGISTER_POSITION, -1)


        if (register != null) {
            activityRegisterBinding.registerDateDp.updateDate(2021,11, 10)
            activityRegisterBinding.registerPriceEt.setText(register.price.toString())
        }

        supportActionBar?.subtitle = "Detalhes do registro"
    }

    fun saveAction(view: View) {
        val register = Register(
            getDateFromDatepicker(),
            activityRegisterBinding.registerPriceTv.text.toString().toDouble()
        )

        val intentResult = Intent()
        intentResult.putExtra(MainActivity.EXTRA_REGISTER, register)
        if (position != -1) {
            intentResult.putExtra(MainActivity.EXTRA_REGISTER_POSITION, position)
        }

        setResult(RESULT_OK, intentResult)
        finish()

    }
    fun cancelAction(view: View) {}

    private fun getDateFromDatepicker(): Date {
        var day = activityRegisterBinding.registerDateDp.dayOfMonth
        var month = activityRegisterBinding.registerDateDp.month
        var year = activityRegisterBinding.registerDateDp.year
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val formatedDate = sdf.format(calendar.time)
        val date = sdf.parse(formatedDate)
        return date
    }
}

