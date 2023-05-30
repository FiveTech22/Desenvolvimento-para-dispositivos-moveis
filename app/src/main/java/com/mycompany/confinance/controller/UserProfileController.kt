package com.mycompany.confinance.controller

import com.mycompany.confinance.model.user.GetUserModel
import com.mycompany.confinance.model.user.ResponseUserModel
import com.mycompany.confinance.repository.UserRepository
import com.mycompany.confinance.repository.listener.ApiListener
import com.mycompany.confinance.util.Session
import java.net.HttpURLConnection


class UserProfileController {
    private var userRepository = UserRepository()
    fun getUser(onSuccess: (name: String, email: String) -> Unit,onFailure: (message: String) -> Unit){
        userRepository.getUserById(id= Session.userId!!, object : ApiListener<GetUserModel>{
            override fun onSuccess(result: GetUserModel) {
                onSuccess.invoke(result.name, result.email)
            }

            override fun onFailure(message: String) {
                onFailure.invoke(message)
            }

        })
    }

    fun deleteUser(status: (message: String, status: Boolean) -> Unit) {
        userRepository.deleteUser(id = Session.userId!!, object : ApiListener<ResponseUserModel>{
            override fun onSuccess(result: ResponseUserModel) {
                if (result.status == HttpURLConnection.HTTP_OK){
                    status.invoke(result.message,true)
                }else{
                    status.invoke(result.message,false)
                }
            }
            override fun onFailure(message: String) {
                status.invoke(message,false)

            }

        })
    }
}