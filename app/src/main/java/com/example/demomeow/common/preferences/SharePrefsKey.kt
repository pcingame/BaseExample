package com.example.demomeow.common.preferences

import android.content.Context
import com.example.demomeow.di.App
import com.google.gson.Gson

object SharePrefsKey {

    private const val PREF_NAME = "application_db"

    private val sharedPrefs by lazy {
        App.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private val gson = Gson()

    /**
     * Remove object
     * @param key is the key for saving
     */
    fun remove(key: String) {
        sharedPrefs.edit().remove(key).apply()
    }
}
