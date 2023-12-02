package com.mycompany.confinance.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mycompany.confinance.model.MovementModel
import com.mycompany.confinance.model.ResponseModel
import com.mycompany.confinance.repository.MovementRepository
import com.mycompany.confinance.request.ApiListener
import java.net.HttpURLConnection

class MovementViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovementRepository(application)
    private var _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> = _isLoading
    private var _list = MutableLiveData<List<MovementModel>>()
    val list: LiveData<List<MovementModel>> = _list
    private var _isLoadingDelete = MutableLiveData<Boolean>()
    val isLoadingDelete: LiveData<Boolean> = _isLoadingDelete
    fun getRevenue(month: Int, year: Int) {
        repository.getRevenue(month = month, year = year, listener = object : ApiListener<List<MovementModel>> {
            override fun onSuccess(result: List<MovementModel>) {
                if (result.isNotEmpty()) {
                    _isLoading.value = false
                    _list.value = result
                }
            }

            override fun onFailure(message: String?, code: Int) {
                if (code == 404){
                    _isLoading.value = null
                }else{
                    _isLoading.value = true
                }
            }

        })
    }

    fun getExpense(month: Int, year: Int) {
        repository.getExpense(month = month, year = year, listener = object : ApiListener<List<MovementModel>> {
            override fun onSuccess(result: List<MovementModel>) {
                if (result.isNotEmpty()) {
                    _isLoading.value = false
                    _list.value = result
                }
            }

            override fun onFailure(message: String?, code: Int) {
                if (code == 404){
                    _isLoading.value = null
                }else{
                    _isLoading.value = true
                }
            }

        })
    }

    fun deleteMovement(id:Long) {
        repository.deleteMovement( idMovement = id, listener = object :ApiListener<ResponseModel>{
            override fun onSuccess(result: ResponseModel) {
                if (result.status == HttpURLConnection.HTTP_OK){
                    _isLoadingDelete.value = true
                }else{
                    _isLoadingDelete.value = false
                }
            }

            override fun onFailure(message: String?, code: Int) {
                _isLoadingDelete.value = false
            }

        })
    }



}
