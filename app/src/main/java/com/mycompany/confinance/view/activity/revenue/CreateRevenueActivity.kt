package com.mycompany.confinance.view.activity.revenue

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mycompany.confinance.R
import com.mycompany.confinance.databinding.*
import com.mycompany.confinance.model.MovementModel
import com.mycompany.confinance.model.MovementUpdate
import com.mycompany.confinance.util.DatePickerFragment
import com.mycompany.confinance.viewmodel.revenue.CreateRevenueViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class CreateRevenueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateRevenueBinding
    private lateinit var sheetBinding: CustomBottomSheetBinding
    private lateinit var sheet: CustomBottomSheetErroGenericBinding
    private val viewModel: CreateRevenueViewModel by viewModels()
    private var selectedCardView: Int? = null
    private var switchState = false
    private var dialogEditErro: AlertDialog? = null
    private var dialogEditDelete: AlertDialog? = null
    private var revenue: MovementModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateRevenueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editRevenue()
        handleDate()
        handleClick()
        handleRepetition()
        observe()
    }

    private fun observe() {
        viewModel.isLoading.observe(this) {
            when(it) {
                true -> {
                    startActivity(Intent(this, RevenueActivity::class.java))
                    finish()
                }

                false -> {
                    handleErro()
                }

                else -> {
                    handleSheet()
                }
            }
        }
    }


    private fun handleClick() {
        binding.arrowClose.setOnClickListener {
            if (revenue != null) {
                dialogEdit()
            } else {
                finish()
            }
        }

        handleCategory()

        binding.buttonCreate.setOnClickListener {
            save()
        }
    }

    private fun handleRepetition() {
        binding.switchRevenue.setOnCheckedChangeListener { _, isChecked ->
            switchState = isChecked
        }

        binding.textRepetition.setOnClickListener {
            if (!switchState) {
                openBottomSheet()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun openBottomSheet() {
        var cont: String? = null
        var period: String? = null
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        sheetBinding =
            CustomBottomSheetBinding.inflate(
                layoutInflater, null, false
            )
        dialog.setContentView(sheetBinding.root)

        if (cont != null && period != null) {
            sheetBinding.textCont.text = cont
            sheetBinding.textPeriodTotal.text = period
        }

        sheetBinding.arrowTop.setOnClickListener {
            val currentValue = sheetBinding.textCont.text.toString().toInt()
            val newValue = currentValue + 1
            sheetBinding.textCont.text = newValue.toString()
        }
        sheetBinding.arrowBottom.setOnClickListener {
            val currentValue = sheetBinding.textCont.text.toString().toInt()
            val newValue = currentValue - 1
            sheetBinding.textCont.text = newValue.toString()
        }

        sheetBinding.arrowBottomMenu.setOnClickListener {
            showPopupMenu(it)
        }

        sheetBinding.button.setOnClickListener {
            cont = sheetBinding.textCont.text.toString()
            period = sheetBinding.textPeriodTotal.text.toString()
            binding.textRepetition.text = "${cont}x $period"
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_popup, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.monthly -> {
                    sheetBinding.textPeriodTotal.text = applicationContext.getString(R.string.monthly)
                    return@setOnMenuItemClickListener true
                }

                R.id.daily -> {
                    sheetBinding.textPeriodTotal.text = applicationContext.getString(R.string.daily)
                    return@setOnMenuItemClickListener true
                }

                R.id.weekly -> {
                    sheetBinding.textPeriodTotal.text = applicationContext.getString(R.string.weekly)
                    return@setOnMenuItemClickListener true
                }

                R.id.annual -> {
                    sheetBinding.textPeriodTotal.text = applicationContext.getString(R.string.annual)
                    return@setOnMenuItemClickListener true
                }

                else -> return@setOnMenuItemClickListener false
            }
        }

        popup.show()
    }

    private fun handleDate() {
        binding.textData.setOnClickListener {
            val datePicker = DatePickerFragment { day, month, year -> onDateSelect(day, month, year) }
            datePicker.setStyle(R.style.DatePickRevenue)
            datePicker.show(supportFragmentManager, "datePicker")
        }
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun onDateSelect(day: Int, mounth: Int, year: Int) {
        val mounthNew = mounth + 1
        val formattedDay = if (day <= 9) "0$day" else "$day"
        val formattedMonth = if (mounthNew <= 9) "0$mounthNew" else "$mounthNew"
        val formattedDate = "$formattedDay/$formattedMonth/$year"
        binding.textData.text = formattedDate
    }


    private fun save() {
        val value = binding.editBalanceRevenue.text.toString().takeIf { it != "" }?.toDoubleOrNull()
        val description = binding.editTextDescription.text.toString()
        val date = binding.textData.text.toString()
        val fixed = binding.switchRevenue.isChecked
        val repetition = binding.textRepetition.text.toString()
        val photo = selectedCardView
        var recurrenceIntervals: Int? = null
        var recurrenceFrequency: String? = null

        if (revenue != null) {
            if (repetition != "Repetições") {
                val part = repetition.split("x ")
                recurrenceIntervals = part[0].toInt()
                recurrenceFrequency = part[1]
                when (recurrenceFrequency) {
                    "Semanal" -> {
                        recurrenceFrequency = "weekly"
                    }

                    "Diário" -> {
                        recurrenceFrequency = "daily"
                    }

                    "Mensal" -> {
                        recurrenceFrequency = "monthly"
                    }

                    "Anual" -> {
                        recurrenceFrequency = "annually"
                    }
                }
                viewModel.updateRevenue(
                    updateRevenue = MovementUpdate(
                        description = description,
                        value = value?.toLong(),
                        photo = selectedCardView,
                        date = date,
                        fixedIncome = fixed,
                        recurrenceIntervals = recurrenceIntervals,
                        recurrenceFrequency = recurrenceFrequency
                    ),
                    revenue= revenue!!
                )
            } else {
                viewModel.updateRevenue(
                    updateRevenue = MovementUpdate(
                        description = description,
                        value = value?.toLong(),
                        photo = selectedCardView,
                        date = date,
                        fixedIncome = fixed,
                        recurrenceIntervals = recurrenceIntervals,
                        recurrenceFrequency = recurrenceFrequency
                    ),
                    revenue= revenue!!
                )
            }

        } else {
            viewModel.createRevenue(
                value?.toLong(),
                description = description,
                data = date,
                fixedIncome = fixed,
                repetitions = repetition,
                photo = photo
            )
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun handleCategory() {
        val cardViews = listOf(
            binding.cardSalary,
            binding.cardInvesti,
            binding.cardService,
            binding.cardOutro
        )

        for (cardView in cardViews) {
            cardView.setOnClickListener {
                when (cardView.id) {
                    R.id.card_salary -> {
                        selectedCardView = 1
                        binding.imgSalary.setImageResource(R.drawable.salario_verde)
                        binding.imgInvesty.setImageResource(R.drawable.investimento)
                        binding.imgService.setImageResource(R.drawable.servi_os)
                        binding.imgOuther.setImageResource(R.drawable.outros_1)
                    }

                    R.id.card_investi -> {
                        selectedCardView = 2
                        binding.imgInvesty.setImageResource(R.drawable.investimento_verde)
                        binding.imgService.setImageResource(R.drawable.servi_os)
                        binding.imgOuther.setImageResource(R.drawable.outros_1)
                        binding.imgSalary.setImageResource(R.drawable.salario)
                    }

                    R.id.card_service -> {
                        selectedCardView = 3
                        binding.imgService.setImageResource(R.drawable.servi_os_verde)
                        binding.imgOuther.setImageResource(R.drawable.outros_1)
                        binding.imgSalary.setImageResource(R.drawable.salario)
                        binding.imgInvesty.setImageResource(R.drawable.investimento)
                    }

                    R.id.card_outro -> {
                        selectedCardView = 4
                        binding.imgOuther.setImageResource(R.drawable.outros_verde)
                        binding.imgService.setImageResource(R.drawable.servi_os)
                        binding.imgSalary.setImageResource(R.drawable.salario)
                        binding.imgInvesty.setImageResource(R.drawable.investimento)
                    }
                }

            }
        }

    }

    private fun handleErro() {
        if (dialogEditErro != null && dialogEditErro?.isShowing == true) {
            dialogEditErro?.dismiss()
        }

        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val dialogBinding =
            CustomDialogErrorBinding.inflate(LayoutInflater.from(this))

        dialogBinding.button.setOnClickListener {
            dialogEditErro?.dismiss()
        }

        dialogEditErro = build.setView(dialogBinding.root).create()
        dialogEditErro?.show()

    }

    private fun handleSheet() {
        sheet = CustomBottomSheetErroGenericBinding.inflate(layoutInflater, null, false)
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)

        sheet.imgAlert.setBackgroundResource(R.drawable.erro_green)

        sheet.button.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(sheet.root)
        dialog.show()
    }


    private fun dialogEdit() {
        if (dialogEditDelete != null && dialogEditDelete?.isShowing == true) {
            dialogEditDelete?.dismiss()
        }
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val dialogBinding =
            CustomDialogCancellEditRevenueBinding.inflate(LayoutInflater.from(this))
        dialogBinding.buttonYesExit.setOnClickListener {
            dialogEditDelete?.dismiss()
            startActivity(Intent(this, RevenueActivity::class.java))
            finish()
        }
        dialogBinding.buttonNo.setOnClickListener {
            dialogEditDelete?.dismiss()
        }
        dialogEditDelete = build.setView(dialogBinding.root).create()
        dialogEditDelete?.show()
    }

    private fun formatNumber(numero: Long?): String {
        val formato = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale("pt", "BR")))
        formato.isGroupingUsed = true
        formato.groupingSize = 3

        return formato.format(numero)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    private fun editRevenue() {
        revenue = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("revenue", MovementModel::class.java)
        } else {
            intent.getParcelableExtra("revenue")
        }

        if (revenue != null) {
            binding.editBalanceRevenue.setText(formatNumber(revenue!!.value))
            binding.editTextDescription.setText(revenue!!.description)
            binding.textData.text = revenue!!.date

            if (revenue!!.fixedIncome == true) {
                switchState = true
                binding.switchRevenue.isChecked = true
                binding.textRepetition.text = "Repetições"
            } else {
                val recurrenceFrequency = when (revenue!!.recurrenceFrequency) {
                    "weekly" -> {
                        "Semanal"
                    }

                    "daily" -> {
                        "Diário"
                    }

                    "monthly" -> {
                        "Mensal"
                    }

                    "annually" -> {
                        "Anual"
                    }

                    else -> {
                        null
                    }
                }

                if(recurrenceFrequency != null || revenue?.recurrenceIntervals != null){
                    binding.textRepetition.text = "${revenue?.recurrenceIntervals}x $recurrenceFrequency "
                }

            }
            when (revenue?.photo) {
                1 -> {
                    selectedCardView = 1
                    binding.imgSalary.setImageResource(R.drawable.salario_verde)
                }

                2 -> {
                    selectedCardView = 2
                    binding.imgInvesty.setImageResource(R.drawable.investimento_verde)
                }

                3 -> {
                    selectedCardView = 3
                    binding.imgService.setImageResource(R.drawable.servi_os_verde)
                }

                4 -> {
                    selectedCardView = 4
                    binding.imgOuther.setImageResource(R.drawable.outros_verde)

                }
            }
            binding.buttonCreate.text = "Salvar"
        }
    }
}

