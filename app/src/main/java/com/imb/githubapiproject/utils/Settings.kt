package com.imb.githubapiproject.utils

import android.content.Context
import android.content.SharedPreferences

object Settings {
    private val SHARED_PREF_NAME = "shared_preferences"
    private lateinit var mContext: Context
    private lateinit var sharedPreferences: SharedPreferences
    private val USER_IN = "user_in"
    private val USER_AVATAR = "user_avatar"
    private val USER_NAME = "user_name"

    fun init(mCtx: Context) {
        mContext = mCtx
        sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setUserIn(bool: Boolean) {
        sharedPreferences.edit().putBoolean(USER_IN, bool).apply()
    }

    fun userIn(): Boolean =
        sharedPreferences.getBoolean(USER_IN, false)

    fun setUserName(name: String) {
        sharedPreferences.edit().putString(USER_NAME, name).apply()
    }

    fun userName(): String? = sharedPreferences.getString(USER_NAME, "")

    fun setUserAvatarURL(url: String) {
        sharedPreferences.edit().putString(USER_AVATAR, url).apply()
    }

    fun userAvatarURL(): String? = sharedPreferences.getString(USER_AVATAR, "")

}