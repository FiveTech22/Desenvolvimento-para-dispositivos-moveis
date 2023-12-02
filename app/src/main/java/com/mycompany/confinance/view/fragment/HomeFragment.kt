package com.mycompany.confinance.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mycompany.confinance.R
import com.mycompany.confinance.databinding.FragmentHomeBinding
import com.mycompany.confinance.view.activity.revenue.CreateRevenueActivity
import com.mycompany.confinance.view.activity.GraphicActivity
import com.mycompany.confinance.view.activity.expense.CreateExpenseActivity
import com.mycompany.confinance.viewmodel.HomeViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        handleClick()
    }

    private fun handleClick() {
        binding.textCreateRevenue.setOnClickListener {
            startActivity(Intent(activity, CreateRevenueActivity::class.java))
        }
        binding.textCreateExpense.setOnClickListener {
            startActivity(Intent(activity, CreateExpenseActivity::class.java))
        }
        binding.textViewGraphic.setOnClickListener {
            startActivity(Intent(activity, GraphicActivity::class.java))
        }
    }

    private fun formatarNumero(numero: Double): String {
        val formato = DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale("pt", "BR")))
        return "R$ " + formato.format(numero)
    }

    private fun observe() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.shimmerLayoutTextTotals.shimmerColor = 0
            } else {
                binding.textTotalRevenue.text = context?.getString(R.string.total_default)
                binding.shimmerLayoutTextTotals.startLayoutAnimation()
            }

            viewModel.totalMovement.observe(viewLifecycleOwner) {
                binding.textTotalExpense.text = formatarNumero(it.totalExpenses)
                binding.textTotalRevenue.text = formatarNumero(it.totalRevenues)
            }
        }

    }
}