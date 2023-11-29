package com.mycompany.confinance.repository

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.mycompany.confinance.R
import com.mycompany.confinance.model.MovementModel
import com.mycompany.confinance.model.MovementUpdate
import com.mycompany.confinance.model.ResponseModel
import com.mycompany.confinance.model.User
import com.mycompany.confinance.request.ApiListener
import com.mycompany.confinance.request.Retrofit
import com.mycompany.confinance.service.MovementService
import com.mycompany.confinance.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

class MovementRepository(private val context: Context) {

    private val remote = Retrofit.getService(MovementService::class.java)

    private fun getUserIdFromSharedPreferences(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getLong(Constants.KEY.KEY_USER_ID, -1)
    }


    @SuppressLint("SuspiciousIndentation")
    fun createMovement(
        codeType: Int,
        value: Long,
        description: String,
        data: String,
        fixedIncome: Boolean?,
        repetitions: String?,
        photo: Int,
        listener: ApiListener<ResponseModel>,
        context: Context
    ) {
        var recurrenceIntervals:Int? = null
        var recurrenceFrequency: String? = null

        if (repetitions != "Repetições"){
            val part = repetitions?.split("x ")
             recurrenceIntervals = part?.get(0)?.toInt()
             recurrenceFrequency = part?.get(1)
            when (recurrenceFrequency) {
                "Semanal" -> {
                    recurrenceFrequency = "weekly"
                }

                "Diário" -> {
                    recurrenceFrequency = "daily"
                }

                "Mensal" -> {
                    recurrenceFrequency = "monthly"
                }

                "Anual" -> {
                    recurrenceFrequency = "annually"
                }

                else -> {
                    recurrenceFrequency = null
                }
            }
        }

        val userId = getUserIdFromSharedPreferences(context = context)

        var call: Call<ResponseModel>?


        if (codeType == 1) {
            call = remote.createMoviment(
                MovementModel(
                    id = null,
                    type_movement = "receita",
                    value = value,
                    description = description,
                    date = data,
                    photo = photo,
                    fixedIncome = fixedIncome,
                    recurrenceFrequency = recurrenceFrequency,
                    recurrenceIntervals = recurrenceIntervals,
                    user = User(userId)
                ))

                        call.enqueue (object : Callback<ResponseModel> {
                    override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                        if (response.code() == HttpURLConnection.HTTP_CREATED) {
                            response.body()?.let {
                                listener.onSuccess(it)
                            }
                        } else {
                            val error =
                                Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                            listener.onFailure(error.message, code = error.status)
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
        } else {
            call = remote.createMoviment(
                MovementModel(
                    id = null,
                    type_movement = "despesa",
                    value = value,
                    description = description,
                    date = data,
                    photo = photo,
                    fixedIncome = fixedIncome,
                    recurrenceFrequency = recurrenceFrequency,
                    recurrenceIntervals = recurrenceIntervals,
                    user = User(id = userId)
                ))

                        call.enqueue(object : Callback<ResponseModel> {
                    override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                        if (response.code() == HttpURLConnection.HTTP_CREATED) {
                            response.body()?.let {
                                listener.onSuccess(it)
                            }
                        } else {
                            val error =
                                Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                            listener.onFailure(error.message, code = error.status)
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

    fun getRevenue(month:Int, year:Int, listener: ApiListener<List<MovementModel>>) {
        val userId = getUserIdFromSharedPreferences(context = context)
        val call = remote.getRevenue(id = userId, month = month , year= year)

        call.enqueue(object : Callback<List<MovementModel>> {
            override fun onResponse(call: Call<List<MovementModel>>, response: Response<List<MovementModel>>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                    listener.onFailure(message = error.message, code = error.status)
                }

            }

            override fun onFailure(call: Call<List<MovementModel>>, t: Throwable) {
                if (t is IOException) {
                    listener.onFailure(context.getString(R.string.error_no_connection), 500)
                } else {
                    listener.onFailure(context.getString(R.string.error_generic), 500)
                }
            }

        })
    }

    fun getExpense(month:Int, year:Int, listener: ApiListener<List<MovementModel>>) {
        val userId = getUserIdFromSharedPreferences(context = context)
        val call = remote.getExpense(id = userId, month = month , year= year)

        call.enqueue(object : Callback<List<MovementModel>> {
            override fun onResponse(call: Call<List<MovementModel>>, response: Response<List<MovementModel>>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                    listener.onFailure(message = error.message, code = error.status)
                }

            }

            override fun onFailure(call: Call<List<MovementModel>>, t: Throwable) {
                if (t is IOException) {
                    listener.onFailure(context.getString(R.string.error_no_connection), 500)
                } else {
                    listener.onFailure(context.getString(R.string.error_generic), 500)
                }
            }

        })
    }

    fun deleteMovement(idMovement: Long, listener: ApiListener<ResponseModel>) {
        val call = remote.deleteMovement(id = idMovement)

        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                    listener.onFailure(error.message, code = error.status)
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

    fun uptadeMovement(id: Long,model: MovementUpdate ,listener: ApiListener<ResponseModel>) {

        val call = remote.uptadeMovement(id= id, model =  model)

        call.enqueue(object :Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if (response.code() == HttpURLConnection.HTTP_OK){
                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                }else{
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseModel::class.java)
                    listener.onFailure(error.message, code = error.status)
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