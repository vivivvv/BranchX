package com.app.mybase.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.app.mybase.R
import com.app.mybase.helper.SharedPreferenceHelper
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor()
    }

    open fun setStatusBarColor() {
        window.statusBarColor = getColor(R.color.white)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }

    fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}