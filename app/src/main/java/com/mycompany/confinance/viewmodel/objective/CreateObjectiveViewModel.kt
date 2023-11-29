package com.mycompany.confinance.viewmodel.objective

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mycompany.confinance.model.ObjectiveModel
import com.mycompany.confinance.model.ResponseModel
import com.mycompany.confinance.repository.ObjectiveRepository
import com.mycompany.confinance.request.ApiListener
import java.net.HttpURLConnection.HTTP_CREATED
import java.net.HttpURLConnection.HTTP_OK

class CreateObjectiveViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ObjectiveRepository(context = application)
    private var _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> = _isLoading

    fun createObjective(
        value: Double?,
        savedValue: Double?,
        description: String,
        photo: Int?,
        date: String
    ) {
        if (value == null || savedValue == null || description != "" || photo == null || date == "Data"){
            _isLoading.value = null
        }else{
            repository.createObjective(
                value = value!!,
                savedValue = savedValue!!,
                description = description,
                photo = photo!!,
                date = date,
                listener = object : ApiListener<ResponseModel> {
                    override fun onSuccess(result: ResponseModel) {
                        if (result.status == HTTP_CREATED) {
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

    fun updateObjective(updatedObjective: ObjectiveModel, objective: ObjectiveModel) {
        val updatedValue = updatedObjective.value.takeIf { it != objective.value }
        val updatedSavedValue = updatedObjective.savedValue.takeIf { it != objective.savedValue }
        val updatedDescription = updatedObjective.name.takeIf { it != objective.name }
        val updatedPhoto = updatedObjective.photo.takeIf { it != objective.photo }
        val updatedDate = updatedObjective.date.takeIf { it != objective.date }

        repository.updateObjective(
            id = objective.id!!,
            model = ObjectiveModel(
                value = updatedValue,
                savedValue = updatedSavedValue,
                name = updatedDescription,
                photo = updatedPhoto,
                date = updatedDate
            ),
            listener = object : ApiListener<ResponseModel> {
                override fun onSuccess(result: ResponseModel) {
                    if (result.status == HTTP_OK) {
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