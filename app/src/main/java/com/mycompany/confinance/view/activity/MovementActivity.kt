package com.mycompany.confinance.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mycompany.confinance.databinding.ActivityMovementBinding
import com.mycompany.confinance.model.MovementModel
import com.mycompany.confinance.util.OnClickMovementListener
import com.mycompany.confinance.view.adapter.MovementAdapter
import com.mycompany.confinance.viewmodel.MovementViewModel

class MovementActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovementBinding
    private val viewModel: MovementViewModel by viewModels()
    private val adapter = MovementAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getMovement("receita")
        observe()
        handleClick()

    }

   private fun handleMovement() {
        val listener = object :OnClickMovementListener{
            override fun onClick(id: Long) {

            }

            override fun delete(id: Long) {
            }
        }
       adapter.setListener(listener)
    }
    private fun observe() {
        viewModel.isLoading.observe(this) {
            if (it == true) {
                recycler()
                adapter.startShimmerAnimation()
            }else if (it == false){
                recycler()
                adapter.stopShimmerAnimation()
                viewModel.list.observe(this){ list->
                    adapter.setList(list)
                }
            }
        }

    }

    private fun handleClick() {
        binding.arrowBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
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

}