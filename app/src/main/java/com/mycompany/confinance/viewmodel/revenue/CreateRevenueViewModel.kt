package com.mycompany.confinance.viewmodel.revenue

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mycompany.confinance.model.MovementModel
import com.mycompany.confinance.model.MovementUpdate
import com.mycompany.confinance.model.ResponseModel
import com.mycompany.confinance.repository.MovementRepository
import com.mycompany.confinance.request.ApiListener
import java.net.HttpURLConnection

class CreateRevenueViewModel(private val application: Application) : AndroidViewModel(application) {

    private val repository = MovementRepository(application)
    private var _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> = _isLoading

    fun createRevenue(
        value: Long?,
        description: String,
        data: String?,
        fixedIncome: Boolean?,
        repetitions: String?,
        photo: Int?
    ) {
        if (value == null || description == "" || data == "" || photo == null) {
            _isLoading.value = null
        } else {
            if (fixedIncome == false) {
                repository.createMovement(
                    context = application,
                    codeType = 1,
                    value = value,
                    description = description,
                    fixedIncome = null,
                    data = data!!,
                    repetitions = repetitions,
                    photo = photo!!,
                    listener = object : ApiListener<ResponseModel> {
                        override fun onSuccess(result: ResponseModel) {
                            if (result.status == HttpURLConnection.HTTP_CREATED) {
                                _isLoading.value = true
                            } else {
                                _isLoading.value = false
                            }
                        }

                        override fun onFailure(message: String?, code: Int) {
                            _isLoading.value = false
                        }

                    }
                )
            } else {
                repository.createMovement(
                    context = application,
                    codeType = 1,
                    value = value!!,
                    description = description,
                    data = data!!,
                    fixedIncome = fixedIncome,
                    repetitions = null,
                    photo = photo!!,
                    listener = object : ApiListener<ResponseModel> {
                        override fun onSuccess(result: ResponseModel) {
                            if (result.status == HttpURLConnection.HTTP_CREATED) {
                                _isLoading.value = true
                            } else {
                                _isLoading.value = false
                            }
                        }

                        override fun onFailure(message: String?, code: Int) {
                            _isLoading.value = false
                        }

                    }
                )
            }
        }
    }


    fun updateRevenue(updateRevenue: MovementUpdate, revenue: MovementModel) {

        if (updateRevenue.value == null || updateRevenue.description == "" || updateRevenue.date == "" || updateRevenue.photo == null) {
            _isLoading.value = null
        }else{
            val updatedValue = updateRevenue.value.takeIf { it != revenue.value }
            val updatedDescription = updateRevenue.description.takeIf { it != revenue.description }
            val updatedPhoto = updateRevenue.photo.takeIf { it != revenue.photo }
            val updatedDate = updateRevenue.date.takeIf { it != revenue.date }
            val fixed = updateRevenue.fixedIncome.takeIf { it != revenue.fixedIncome }
            val frequency = updateRevenue.recurrenceFrequency.takeIf { it != revenue.recurrenceFrequency }
            val intervals = updateRevenue.recurrenceIntervals.takeIf { it != revenue.recurrenceIntervals }

            repository.uptadeMovement(
                id = revenue.id!!,
                model = MovementUpdate(
                    description = updatedDescription,
                    photo = updatedPhoto,
                    value = updatedValue,
                    fixedIncome = fixed,
                    recurrenceFrequency = frequency,
                    recurrenceIntervals = intervals,
                    date = updatedDate
                ),
                listener = object : ApiListener<ResponseModel> {
                    override fun onSuccess(result: ResponseModel) {
                        if (result.status == HttpURLConnection.HTTP_OK) {
                            _isLoading.value = true
                        } else {
                            _isLoading.value = false
                        }
                    }

                    override fun onFailure(message: String?, code: Int) {
                        _isLoading.value = false

                    }

                }
            )
        }

    }

    }