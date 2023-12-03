package com.mycompany.confinance.util

import android.content.Context


object SharedPreferencesUtil {
    fun saveUserId(context: Context, userId: Long) {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong(Constants.KEY.KEY_USER_ID, userId)
        editor.apply()
    }

    fun getUserId(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getLong(Constants.KEY.KEY_USER_ID, 0L)
    }

    fun saveState(isConnected: Boolean,context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("http_connected", isConnected)
        editor.apply()
    }

    fun saveImg(context: Context,img:Int){
        val sharedPreferences = context.getSharedPreferences("MyP", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("img",img)
        editor.apply()
    }

    fun getImg(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences("MyP", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("img", 1)
    }
}