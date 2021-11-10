package com.vinilaureto.gashistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.vinilaureto.gashistory.adapters.RegisterAdapter
import com.vinilaureto.gashistory.controllers.RegisterController
import com.vinilaureto.gashistory.databinding.ActivityMainBinding
import com.vinilaureto.gashistory.entities.Register

class MainActivity : AppCompatActivity() {
    companion object Extras {
        const val EXTRA_REGISTER = "EXTRA_REGISTER"
        const val EXTRA_REGISTER_POSITION = "EXTRA_REGISTER_POSITION"
    }

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var addRegisterEditorActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var editRegisterEditorActivityLauncher: ActivityResultLauncher<Intent>

    private val registerController: RegisterController by lazy {
        RegisterController()
    }

    private var registersList: MutableList<Register> = mutableListOf()
    private fun prepareRegistersList() {
        registersList = registerController.findAllRegister()
    }

    private val registerAdapter: RegisterAdapter by lazy {
        RegisterAdapter(this, R.layout.layout_register, registersList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        supportActionBar?.subtitle = "Histórico de preços"

        prepareRegistersList()
        activityMainBinding.registersLv.adapter = registerAdapter

        registerForContextMenu(activityMainBinding.registersLv)

        activityMainBinding.newRegisterBt.setOnClickListener {
            addRegisterEditorActivityLauncher.launch(Intent(this, RegisterActivity::class.java))
        }

        activityMainBinding.registersLv.setOnItemClickListener{_, _, position, _ ->
            val register = registersList[position]
            val editRegisterIntent = Intent(this, RegisterActivity::class.java)
            editRegisterIntent.putExtra(EXTRA_REGISTER, register)
            editRegisterIntent.putExtra(EXTRA_REGISTER_POSITION, position)
            editRegisterEditorActivityLauncher.launch(editRegisterIntent)
        }

        addRegisterEditorActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val register = result.data?.getParcelableExtra<Register>(EXTRA_REGISTER)
                if (register != null) {
                    registersList.add(register)
                    registerController.newRegister(register)
                    registerAdapter.notifyDataSetChanged()
                }
            }
        }

        editRegisterEditorActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val position = result.data?.getIntExtra(EXTRA_REGISTER_POSITION, -1)
                result.data?.getParcelableExtra<Register>(EXTRA_REGISTER)?.apply {
                    if (position != null && position != -1) {
                        registerController.updateRegister(this)
                        registersList[position] = this
                        registerAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu_item, menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.refreshMi -> {
            registerAdapter.notifyDataSetChanged()
            true
        }
        else -> { false }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val registerPosition = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        val register = registersList[registerPosition]

        return when (item.itemId) {
            R.id.editItemMi -> {
                val editRegisterIntent = Intent(this, RegisterActivity::class.java)
                editRegisterIntent.putExtra(EXTRA_REGISTER, register)
                editRegisterIntent.putExtra(EXTRA_REGISTER_POSITION, registerPosition)
                editRegisterEditorActivityLauncher.launch(editRegisterIntent)
                true
            }
            R.id.removeItemMi -> {
                with(AlertDialog.Builder(this)) {
                    setMessage("Deseja apagar o registro?")
                    setPositiveButton("Sim") {_, _ ->
                        registersList.removeAt(registerPosition)
                        registerAdapter.notifyDataSetChanged()
                        register.date?.let { registerController.removeRegister(it) }
                        Snackbar.make(activityMainBinding.root, "Registro removido", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Cancelar") {_,_ ->
                        Snackbar.make(activityMainBinding.root, "Operação cancelada", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()
                true
            }
            else -> {
                false
            }
        }
    }
}