package com.mycompany.confinance.view.activity.objective

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.R.color
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mycompany.confinance.R
import com.mycompany.confinance.databinding.ActivityCreateObjectiveBinding
import com.mycompany.confinance.databinding.CustomBottomSheetErroGenericBinding
import com.mycompany.confinance.databinding.CustomDialogCancellEditObjectiveBinding
import com.mycompany.confinance.model.ObjectiveModel
import com.mycompany.confinance.util.DatePickerFragment
import com.mycompany.confinance.viewmodel.objective.CreateObjectiveViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class CreateObjectiveActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateObjectiveBinding
    private val viewModel: CreateObjectiveViewModel by viewModels()
    private var selectedCardView: Int? = null
    private var objective: ObjectiveModel? = null
    private var editDeleteEdit: AlertDialog? = null
    private lateinit var sheetBinding: CustomBottomSheetErroGenericBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateObjectiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleClick()
        handleCategory()
        editObjective()
        observe()
    }

    private fun observe() {
        viewModel.isLoading.observe(this) {
            when (it) {
                true -> {
                    startActivity(Intent(this, ObjectiveActivity::class.java))
                    finish()
                }
                false -> {
                    Toast.makeText(this, "erro", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    handleSheet()
                }
            }
        }
    }

    private fun handleSheet() {
        val dialog = BottomSheetDialog(this)
        sheetBinding = CustomBottomSheetErroGenericBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)

        sheetBinding.button.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun handleClick() {
        binding.arrowClose.setOnClickListener {
            if (objective != null) {
                handleDeleteEdit()
            } else {
                finish()
            }
        }
        binding.buttonCreate.setOnClickListener {
            save()
        }
        binding.textData.setOnClickListener {
            val datePicker = DatePickerFragment { day, month, year -> onDateSelect(day, month, year) }
            datePicker.setStyle(R.style.DatePickObjective)
            datePicker.show(supportFragmentManager, "datePicker")
        }
    }

    private fun handleDeleteEdit() {
        if (editDeleteEdit != null && editDeleteEdit?.isShowing == true) {
            editDeleteEdit?.dismiss()
        }

        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val dialogBinding =
            CustomDialogCancellEditObjectiveBinding.inflate(LayoutInflater.from(this))
        dialogBinding.buttonYesDelete.setOnClickListener {
            editDeleteEdit?.dismiss()
            startActivity(
                Intent(this, ObjectiveActivity::class.java)
            )
            finish()
        }
        dialogBinding.buttonCancell.setOnClickListener {
            editDeleteEdit?.dismiss()
        }
        editDeleteEdit = build.setView(dialogBinding.root).create()
        editDeleteEdit?.show()

    }

    private fun save() {
        val value = binding.editBalanceObjective.text.toString().takeIf { it != "" }?.toDoubleOrNull()
        val squared = binding.editSpared.text.toString().takeIf { it != "" }?.toDoubleOrNull()
        val descripton = binding.textNameObjective.text.toString()
        val date = binding.textData.text.toString()
        val photo = selectedCardView


        if (objective != null) {
            viewModel.updateObjective(
                updatedObjective = ObjectiveModel(
                    value = value,
                    savedValue = squared,
                    name = descripton,
                    date = date,
                    photo = photo
                ),
                objective = objective!!
            )
        } else {
            viewModel.createObjective(
                value = value,
                savedValue = squared,
                description = descripton,
                date = date,
                photo = photo
            )
        }

    }

    @SuppressLint("SetTextI18n")
    private fun onDateSelect(day: Int, mounth: Int, year: Int) {
        val mounthNew = mounth + 1
        val formattedDay = if (day <= 9) "0$day" else "$day"
        val formattedMonth = if (mounthNew <= 9) "0$mounthNew" else "$mounthNew"
        val formattedDate = "$formattedDay/$formattedMonth/$year"
        binding.textData.text = formattedDate
    }

    @SuppressLint("ResourceAsColor")
    private fun handleCategory() {
        val views = listOf(
            binding.cardHealth1,
            binding.cardCell,
            binding.cardCar,
            binding.cardJob,
            binding.cardGift,
            binding.cardHouse,
            binding.cardMarketplace,
            binding.cardOuther2
        )

        for (view in views) {
            view.setOnClickListener {
                when (view.id) {
                    R.id.card_health_1 -> {
                        selectedCardView = 1
                        binding.cardHealth1.setBackgroundResource(R.drawable.background_rounded_card_objective)
                        binding.cardOuther2.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCell.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCar.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardGift.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardJob.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardMarketplace.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHouse.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                    }

                    R.id.card_house -> {
                        selectedCardView = 2
                        binding.cardHouse.setBackgroundResource(R.drawable.background_rounded_card_objective)
                        binding.cardGift.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardJob.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCell.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHealth1.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardOuther2.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCar.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardMarketplace.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                    }

                    R.id.card_cell -> {
                        selectedCardView = 3
                        binding.cardCell.setBackgroundResource(R.drawable.background_rounded_card_objective)
                        binding.cardHealth1.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardOuther2.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCar.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardGift.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardJob.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardMarketplace.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHouse.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                    }

                    R.id.card_Marketplace -> {
                        selectedCardView = 4
                        binding.cardMarketplace.setBackgroundResource(R.drawable.background_rounded_card_objective)
                        binding.cardHouse.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardGift.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardJob.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCell.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHealth1.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardOuther2.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCar.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                    }

                    R.id.card_gift -> {
                        selectedCardView = 5
                        binding.cardGift.setBackgroundResource(R.drawable.background_rounded_card_objective)
                        binding.cardJob.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCell.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHealth1.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardOuther2.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCar.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardMarketplace.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHouse.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                    }

                    R.id.card_car -> {
                        selectedCardView = 6
                        binding.cardCar.setBackgroundResource(R.drawable.background_rounded_card_objective)
                        binding.cardCell.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHealth1.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardOuther2.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardGift.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardJob.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardMarketplace.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHouse.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                    }


                    R.id.card_job -> {
                        selectedCardView = 7
                        binding.cardJob.setBackgroundResource(R.drawable.background_rounded_card_objective)
                        binding.cardCell.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHealth1.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardOuther2.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCar.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardGift.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardMarketplace.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHouse.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                    }


                    R.id.card_outher_2 -> {
                        selectedCardView = 8
                        binding.cardOuther2.setBackgroundResource(R.drawable.background_rounded_card_objective)
                        binding.cardMarketplace.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHouse.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardGift.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardJob.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCell.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardHealth1.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                        binding.cardCar.setBackgroundResource(color.mtrl_btn_transparent_bg_color)
                    }
                }
            }
        }

    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    private fun editObjective() {
        objective = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("objective", ObjectiveModel::class.java)
        } else {
            intent.getParcelableExtra("objective")
        }

        if (objective != null) {
            binding.editBalanceObjective.setText("${formatNumber(objective?.value)}")
            binding.editSpared.setText("${formatNumber(objective?.savedValue)}")
            binding.textNameObjective.setText(objective?.name)
            binding.textData.text = objective?.date
            when (objective?.photo) {
                1 -> {
                    selectedCardView = 1
                    binding.cardHealth1.setBackgroundResource(R.drawable.background_rounded_card_objective)
                }

                2 -> {
                    selectedCardView = 2
                    binding.cardHouse.setBackgroundResource(R.drawable.background_rounded_card_objective)
                }

                3 -> {
                    selectedCardView = 3
                    binding.cardCell.setBackgroundResource(R.drawable.background_rounded_card_objective)
                }

                4 -> {
                    selectedCardView = 4
                    binding.cardMarketplace.setBackgroundResource(R.drawable.background_rounded_card_objective)
                }

                5 -> {
                    selectedCardView = 5
                    binding.cardGift.setBackgroundResource(R.drawable.background_rounded_card_objective)
                }

                6 -> {
                    selectedCardView = 6
                    binding.cardCar.setBackgroundResource(R.drawable.background_rounded_card_objective)
                }

                7 -> {
                    selectedCardView = 7
                    binding.cardJob.setBackgroundResource(R.drawable.background_rounded_card_objective)
                }

                8 -> {
                    selectedCardView = 8
                    binding.cardOuther2.setBackgroundResource(R.drawable.background_rounded_card_objective)
                }
            }
            binding.buttonCreate.text = "Salvar"
        }
    }

    private fun formatNumber(numero: Double?): String {
        val formato = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale("pt", "BR")))
        formato.isGroupingUsed = true
        formato.groupingSize = 3

        return formato.format(numero)
    }

}