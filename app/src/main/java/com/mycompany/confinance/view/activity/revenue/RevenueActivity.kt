package com.mycompany.confinance.view.activity.revenue

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mycompany.confinance.R
import com.mycompany.confinance.databinding.ActivityRevenueBinding
import com.mycompany.confinance.databinding.CustomDialogDeleteRevenueBinding
import com.mycompany.confinance.databinding.CustomDialogEditRevenueBinding
import com.mycompany.confinance.model.MovementModel
import com.mycompany.confinance.util.OnClickMovementListener
import com.mycompany.confinance.view.activity.MainActivity
import com.mycompany.confinance.view.activity.expense.CreateExpenseActivity
import com.mycompany.confinance.view.adapter.MovementAdapter
import com.mycompany.confinance.viewmodel.MovementViewModel
import java.util.*
import kotlin.collections.ArrayList

class RevenueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRevenueBinding
    private val viewModel: MovementViewModel by viewModels()
    private var listRevenue: ArrayList<MovementModel> = arrayListOf()
    private var id: Long? = null
    private val adapter = MovementAdapter()
    private val calendar = Calendar.getInstance()
    private var dialogEdit: AlertDialog? = null
    private var dialogDelete: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRevenueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateMonthYearText()
        checkMonthAndYear()
        observe()
        handleClick()

    }

    private fun handleMovement() {
        val listener = object : OnClickMovementListener {
            override fun onClick(id: Long) {
                dialogEdit(id)
            }

            override fun delete(id: Long) {
                this@RevenueActivity.id = id
                dialogDelete(this@RevenueActivity.id!!)
            }
        }
        adapter.setListener(listener)
    }

    private fun dialogDelete(id: Long) {
        if (dialogDelete != null && dialogDelete?.isShowing == true) {
            dialogDelete?.dismiss()
        }

        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val dialogBinding =
            CustomDialogDeleteRevenueBinding.inflate(LayoutInflater.from(this))

        dialogBinding.buttonYesExit.setOnClickListener {
            dialogDelete?.dismiss()
            viewModel.deleteMovement(id)
        }
        dialogBinding.buttonCancell.setOnClickListener {
            dialogDelete?.dismiss()
        }

        dialogDelete = build.setView(dialogBinding.root).create()
        dialogDelete?.show()

    }

    private fun dialogEdit(id: Long) {
        if (dialogEdit != null && dialogEdit?.isShowing == true) {
            dialogEdit?.dismiss()
        }

        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val dialogBinding =
            CustomDialogEditRevenueBinding.inflate(LayoutInflater.from(this))
        dialogBinding.buttonYes.setOnClickListener {
            dialogEdit?.dismiss()

            val revenueModel = listRevenue.find { it.id == id }

            if (revenueModel != null) {
                val intent = Intent(this, CreateRevenueActivity::class.java)
                intent.putExtra("revenue", revenueModel)
                startActivity(intent)
                finish()
            }

        }
        dialogBinding.buttonCancell.setOnClickListener {
            dialogEdit?.dismiss()
        }
        dialogEdit = build.setView(dialogBinding.root).create()
        dialogEdit?.show()
    }

    private fun observe() {
        viewModel.isLoading.observe(this) { loading ->
            if (loading == true) {
                recycler()
                adapter.startShimmerAnimation()
            } else if (loading == false) {
                recycler()
                adapter.stopShimmerAnimation()
                viewModel.list.observe(this) { list ->
                    listRevenue = list as ArrayList
                 val list =   listRevenue.sortedByDescending { it.fixedIncome == true }
                    adapter.setList(list)
                }
            }else{
                listRevenue.clear()
                binding.recycler.visibility= View.GONE
                binding.imageCreateRevenue.visibility = View.VISIBLE
                binding.textCreateRevenues.visibility = View.VISIBLE
                binding.textGuia.visibility = View.VISIBLE
            }
        }

        viewModel.isLoadingDelete.observe(this) {
            if (it) {
                updateMovement(id = this.id!!)
            } else {
                Toast.makeText(this, "erro ai paizão", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleClick() {
        binding.arrowBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.buttonCreateRevenue.setOnClickListener {
            startActivity(Intent(this, CreateRevenueActivity::class.java))
        }
    }

    private fun recycler() {
        binding.textGuia.visibility = View.GONE
        binding.textCreateRevenues.visibility = View.GONE
        binding.imageCreateRevenue.visibility = View.GONE
        binding.recycler.visibility = View.VISIBLE
        binding.recycler.layoutManager = LinearLayoutManager(applicationContext)
        binding.recycler.adapter = adapter
        handleMovement()
    }

    private fun updateMovement(id: Long) {
        var position = 0
        var movement: MovementModel? = null
        for (i in 0 until listRevenue.size) {
            if (listRevenue[i].id == id) {
                position = i
                movement = listRevenue[i]
            }
        }
        listRevenue.remove(movement)
        adapter.notifyItemRemoved(position)
    }


    private fun checkMonthAndYear() {
        binding.arrowBack1.setOnClickListener {
            if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                calendar.add(Calendar.YEAR, -1)
                calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            } else {
                calendar.add(Calendar.MONTH, -1)
            }
            updateMonthYearText()
        }

        binding.arrowNext.setOnClickListener {
            if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
                calendar.add(Calendar.YEAR, 1)
                calendar.set(Calendar.MONTH, Calendar.JANUARY)
            } else {
                calendar.add(Calendar.MONTH, 1)
            }
            updateMonthYearText()
        }
    }


    private fun updateMonthYearText() {
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)

        val displayedYear = calendar.get(Calendar.YEAR)
        val displayedMonth = calendar.get(Calendar.MONTH)

        val monthArray = resources.getStringArray(R.array.months)
        val monthAbbreviationsArray = resources.getStringArray(R.array.month_abbreviations)

        val formattedDate = if (displayedYear != currentYear) {
            "${monthAbbreviationsArray[displayedMonth]}/${displayedYear % 100}"
        } else {
            monthArray[displayedMonth]
        }


        binding.textMonth.text = formattedDate
        handleQuery()
    }

    private fun handleQuery() {
        val displayedDate = binding.textMonth.text.toString()
        if (displayedDate.isNotEmpty()) {
            val monthAbbreviationsArray = resources.getStringArray(R.array.month_abbreviations)
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val currentMonthAbbreviation = monthAbbreviationsArray[Calendar.getInstance().get(Calendar.MONTH)]
            val monthIndex = monthAbbreviationsArray.indexOfFirst {
                it.equals(displayedDate.substring(0, 3), ignoreCase = true)
            }

            if (monthIndex >= 0) {
                val yearString = if (displayedDate.length == 7) {
                    val selectedYear = displayedDate.substring(4)
                    val formattedYear = if (selectedYear.length == 2) {
                        "20$selectedYear"
                    } else {
                        selectedYear
                    }
                    formattedYear
                } else {
                    if (displayedDate.substring(0, 3).equals(currentMonthAbbreviation, ignoreCase = true)) {
                        currentYear.toString()
                    } else {
                        displayedDate.substring(4)
                    }
                }

                val year = try {
                    if (yearString.length == 2) {
                        val yw = "20${yearString}"
                        yw.toInt()

                    } else {
                        yearString.toInt()
                    }
                } catch (e: NumberFormatException) {
                    currentYear
                }

                val month = monthIndex + 1
                viewModel.getRevenue(month = month, year = year)
            }
        }
    }


}