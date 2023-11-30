package com.mycompany.confinance.view.viewHolder

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mycompany.confinance.R
import com.mycompany.confinance.databinding.RowMovementBinding
import com.mycompany.confinance.model.MovementModel
import com.mycompany.confinance.util.OnClickMovementListener
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class MovementViewHolder(
    private val binding: RowMovementBinding, private val listener: OnClickMovementListener
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("ResourceAsColor", "SetTextI18n")
    fun bind(movementModel: MovementModel) {
        if (movementModel.type_movement == "receita") {
            when (movementModel.photo) {
                1 -> {
                    binding.imageIcon.setImageResource(R.drawable.salario_verde)
                }

                2 -> {
                    binding.imageIcon.setImageResource(R.drawable.investimento_verde)
                }

                3 -> {
                    binding.imageIcon.setImageResource(R.drawable.servi_os_verde)
                }

                4 -> {
                    binding.imageIcon.setImageResource(R.drawable.outros_verde)
                }
            }
            binding.textValueMovement.setTextColor(ContextCompat.getColor(binding.root.context, R.color.green_4))
        }
        else {
            when (movementModel.photo) {
                1 -> {
                    binding.imageIcon.setImageResource(R.drawable.alimen_vermelho)
                }

                2 -> {
                    binding.imageIcon.setImageResource(R.drawable.edu_vermelho)
                }

                3 -> {
                    binding.imageIcon.setImageResource(R.drawable.sude_vermelho)
                }

                4 -> {
                    binding.imageIcon.setImageResource(R.drawable.outro_vermelho)
                }
                5 ->{
                    binding.imageIcon.setImageResource(R.drawable.casa_vermelha)

                }
            }
            binding.textValueMovement.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
        }

        if (movementModel.fixedIncome == true){
            binding.imageFixedIncome.visibility = View.VISIBLE
        }

        if (movementModel.recurrenceFrequency != null && movementModel.recurrenceIntervals != null){
            binding.imageFrequency.visibility = View.VISIBLE
            binding.textFrequency.visibility = View.VISIBLE


            var recurrenceFrequency =   when (movementModel.recurrenceFrequency) {
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
            binding.imageFrequency.visibility = View.VISIBLE
            binding.textFrequency.text = "${movementModel.recurrenceIntervals}x $recurrenceFrequency "
        }

        binding.textTypeMovement.text = movementModel.description
        binding.textDate.text = movementModel.date
        binding.textValueMovement.text = formatNumber(movementModel.value)
        binding.imgEdit.visibility = View.VISIBLE
        binding.trash.visibility = View.VISIBLE
        binding.shimmerLayout.hideShimmer()
        binding.shimmerLayout.stopShimmer()

        binding.trash.setOnClickListener {
            listener.delete(movementModel.id!!)
        }
        binding.imgEdit.setOnClickListener {
            listener.onClick(movementModel.id!!)
        }

    }

    fun bindShimmer() {
        binding.imageIcon.setImageDrawable(null)
        binding.textValueMovement.text = ""
    }

    private fun formatNumber(numero: Long): String {
        val formato = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale("pt", "BR")))
        formato.isGroupingUsed = true
        formato.groupingSize = 3

        return "R$ " + formato.format(numero)
    }
}