package com.cwod.trasset.helper

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object SharedPref {

    lateinit var sharedPrefs: SharedPreferences

    fun instantiate(context: Context): SharedPref {
        sharedPrefs = context.getSharedPreferences("SharedPrefs", MODE_PRIVATE)
        return this
    }

    fun putString(key: String, value: String): SharedPref {
        val editor = sharedPrefs.edit()
        editor.putString(key, value)
        editor.apply()
        return this
    }

    fun getString(key: String): String? = sharedPrefs.getString(key,null)

    fun putLong(key: String, value: Long): SharedPref {
        val editor = sharedPrefs.edit()
        editor.putLong(key, value)
        editor.apply()
        return this
    }

    fun getLong(key:String) : Long? = sharedPrefs.getLong(key,0)

    fun clear() = sharedPrefs.edit().clear().apply();

}