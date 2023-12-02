package com.mycompany.confinance.repository

import android.content.Context
import com.google.gson.Gson
import com.mycompany.confinance.R
import com.mycompany.confinance.model.*
import com.mycompany.confinance.request.ApiListener
import com.mycompany.confinance.request.Retrofit
import com.mycompany.confinance.service.UserService
import com.mycompany.confinance.util.Constants
import com.mycompany.confinance.util.SharedPreferencesUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection.*

class UserRepository(private val context: Context) {

    private val remote = Retrofit.getService(UserService::class.java)

    fun login(email: String, password: String, listener: ApiListener<ResponseModel>) {
        val call = remote.login(LoginModel(email, password))

        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.code() == HTTP_OK) {
                    response.body()?.let {
                        listener.onSuccess(it)
                        SharedPreferencesUtil.saveState(true, context)
                        SharedPreferencesUtil.saveUserId(context = context, userId = it.userId!!)
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                    listener.onFailure(context.getString(R.string.error_failure_login), error.status)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                if (t is IOException) {
                    listener.onFailure(context.getString(R.string.error_no_connection), 500)
                } else {
                    listener.onFailure(context.getString(R.string.error_generic), 500)
                }
            }

        })

    }


    fun createAccount(
        name: String, email: String, password: String, listener: ApiListener<ResponseModel>
    ) {
        val call = remote.createAccount(UserModel(null, name, email, password,1))

        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.code() == HTTP_CREATED) {
                    response.body()?.let {
                        listener.onSuccess(it)
                        SharedPreferencesUtil.saveUserId(context = context, userId = it.userId!!)
                        SharedPreferencesUtil.saveState(true, context)
                    }
                } else if (response.code() == HTTP_FORBIDDEN) {
                    listener.onFailure(context.getString(R.string.email_already_linked), response.code())
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                if (t is IOException) {
                    listener.onFailure(context.getString(R.string.error_no_connection), 500)
                } else {
                    listener.onFailure(context.getString(R.string.error_generic), 500)
                }
            }

        })
    }

    fun forgotPassword(email: String, listener: ApiListener<ResponseModel>) {
        val call = remote.emailSending(email = email)
        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.code() == HTTP_OK) {
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                    listener.onFailure(context.getString(R.string.email_Exist_Please_Check), code = error.status)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                if (t is IOException) {
                    listener.onFailure(context.getString(R.string.error_no_connection), 500)
                } else {
                    listener.onFailure(context.getString(R.string.error_generic), 500)
                }
            }

        })
    }

    fun verificationCode(
        email: String,
        codeOne: String,
        codeTwo: String,
        codeTree: String,
        codeFour: String,
        listener: ApiListener<ResponseModel>
    ) {
        val code = codeOne.plus(codeTwo).plus(codeTree).plus(codeFour)
        val call = remote.verificationCode(ReviewCoding(email = email, code = code))

        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.code() == HTTP_OK) {
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                    listener.onFailure(context.getString(R.string.wrong_verification_code), code = error.status)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                if (t is IOException) {
                    listener.onFailure(context.getString(R.string.error_no_connection), 500)
                } else {
                    listener.onFailure(context.getString(R.string.error_generic), 500)
                }
            }

        })
    }

    fun resetPassword(email: String, password: String, listener: ApiListener<ResponseModel>) {
        val call = remote.resetPassword(ResetPasswordModel(email = email, newPassword = password))

        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.code() == HTTP_OK) {
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                    listener.onFailure(message = error.message, code = error.status)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                if (t is IOException) {
                    listener.onFailure(context.getString(R.string.error_no_connection), 500)
                } else {
                    listener.onFailure(context.getString(R.string.error_generic), 500)
                }
            }

        })
    }

    fun queryMonthAndYear(month: Int, year: Int, listener: ApiListener<QueryResponse>) {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val id = sharedPreferences.getLong(Constants.KEY.KEY_USER_ID, -1)

        val call = remote.queryMonthAndYear(userId = id, month = month, year = year)
        call.enqueue(object : Callback<QueryResponse> {
            override fun onResponse(call: Call<QueryResponse>, response: Response<QueryResponse>) {
                if (response.code() == HTTP_OK) {
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                    listener.onFailure(message = error.message, code = error.status)
                }
            }

            override fun onFailure(call: Call<QueryResponse>, t: Throwable) {
                if (t is IOException) {
                    listener.onFailure(context.getString(R.string.error_no_connection), 500)
                } else {
                    listener.onFailure(context.getString(R.string.error_generic), 500)
                }
            }

        })
    }

    fun deleteUser(listener: ApiListener<ResponseModel>) {
        val call = remote.deleteUser(SharedPreferencesUtil.getUserId(context))
        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.code() == HTTP_OK) {
                    response.body()?.let {
                        listener.onSuccess(it)
                        SharedPreferencesUtil.saveState(false, context)
                        SharedPreferencesUtil.saveUserId(context, 0)
                    }
                } else {
                    val error = Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                    listener.onFailure(message = error.message, code = error.status)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                if (t is IOException) {
                    listener.onFailure(context.getString(R.string.error_no_connection), 500)
                } else {
                    listener.onFailure(context.getString(R.string.error_generic), 500)
                }
            }

        })

    }

    fun getUser(listener: ApiListener<UserModel>) {
        val call = remote.getUser(SharedPreferencesUtil.getUserId(context))
        call.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.code() == HTTP_OK) {
                    response.body()?.let {
                        SharedPreferencesUtil.saveImg(context = context, it.photo!!)
                        listener.onSuccess(it)
                    }
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                if (t is IOException) {
                    listener.onFailure(context.getString(R.string.error_no_connection), 500)
                } else {
                    listener.onFailure(context.getString(R.string.error_generic), 500)
                }
            }

        })
    }

    fun updateForNameOrEmailOrPhoto(name: String?, email: String?, photo:Int?, listener: ApiListener<ResponseModel>) {
            val call = remote.uptadeUser(
                id = SharedPreferencesUtil.getUserId(context),
                user = UserModel(null, name, email, null, photo)
            )

            call.enqueue(object : Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    if (response.code() == HTTP_OK) {
                        response.body()?.let {
                            listener.onSuccess(it)
                        }
                    }else{
                        val error = Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                        listener.onFailure(message = error.message, code = error.status)
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    if (t is IOException) {
                        listener.onFailure(context.getString(R.string.error_no_connection), 500)
                    } else {
                        listener.onFailure(context.getString(R.string.error_generic), 500)
                    }
                }

            })
    }

    fun upgradePassword(password: String, newPassword: String, listener: ApiListener<ResponseModel>) {
        val call = remote.uptadePassword(
            id = SharedPreferencesUtil.getUserId(context = context),
            PasswordResetRequest(currentPassword = password, newPassword = newPassword)
        )

        call.enqueue(object :Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.code() == HTTP_OK){
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                }else{
                    val error = Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                    listener.onFailure(message = error.message, code = error.status)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                if (t is IOException) {
                    listener.onFailure(context.getString(R.string.error_no_connection), 500)
                } else {
                    listener.onFailure(context.getString(R.string.error_generic), 500)
                }
            }

        })
    }

}