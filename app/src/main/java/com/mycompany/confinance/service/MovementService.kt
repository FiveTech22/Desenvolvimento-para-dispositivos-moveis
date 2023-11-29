package com.mycompany.confinance.service

import com.mycompany.confinance.model.MovementModel
import com.mycompany.confinance.model.MovementUpdate
import com.mycompany.confinance.model.ResponseModel
import com.mycompany.confinance.util.Constants
import retrofit2.Call
import retrofit2.http.*

interface MovementService {
    @POST(Constants.HTTP.URL.URL_CREATE_MOVEMENT)
    fun createMoviment(@Body movement: MovementModel): Call<ResponseModel>

    @GET(Constants.HTTP.URL.URL_RETURN_REVENUE_ID_USER_BY_PERIOD)
    fun getRevenue(
        @Path("id") id: Long,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Call<List<MovementModel>>

    @GET(Constants.HTTP.URL.URL_RETURN_EXPENSE_ID_USER_BY_PERIOD)
    fun getExpense(
        @Path("id") id: Long,
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Call<List<MovementModel>>

    @DELETE(Constants.HTTP.URL.URL_DELETE_MOVEMENT)
    fun deleteMovement(@Path("id") id: Long): Call<ResponseModel>

    @PATCH(Constants.HTTP.URL.URL_UPDATE_MOVEMENT)
    fun uptadeMovement(@Path("id") id: Long, @Body model: MovementUpdate): Call<ResponseModel>


}