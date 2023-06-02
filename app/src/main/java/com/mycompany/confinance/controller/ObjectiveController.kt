package com.mycompany.confinance.controller

import com.mycompany.confinance.model.objective.ObjectiveModel
import com.mycompany.confinance.model.objective.ObjectiveResponse
import com.mycompany.confinance.repository.ObjectiveRepository
import com.mycompany.confinance.repository.listener.ApiListener
import com.mycompany.confinance.util.Session

class ObjectiveController {

    private val repository = ObjectiveRepository()


    fun getObjective(onSuccess:(list: List<ObjectiveModel>) -> Unit, onFailure:(message:String) -> Unit) {
        repository.getObjectiveById(Session.userId!!, object : ApiListener<List<ObjectiveModel>> {
            override fun onSuccess(result: List<ObjectiveModel>) {
                if (result.isNotEmpty()) {
                    onSuccess.invoke(result)
                }
            }

            override fun onFailure(message: String) {
                onFailure(message)
            }
        })
    }

    fun deleteObjectiveById(id: Long, result: (message: String, status: Boolean) -> Unit){
        repository.deleteObjectiveById(id, object : ApiListener<ObjectiveResponse> {
            override fun onSuccess(result: ObjectiveResponse) {
                result(result.message,true)
            }

            override fun onFailure(message: String) {
                result(message, false)
            }
        })
    }
}