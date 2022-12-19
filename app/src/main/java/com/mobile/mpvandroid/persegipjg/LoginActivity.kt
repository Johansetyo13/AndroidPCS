package com.mobile.mpvandroid.persegipjg

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.mobile.mpvandroid.R
import com.mobile.mpvandroid.persegipjg.api.BaseRetrofit
import com.mobile.mpvandroid.persegipjg.response.login.LoginResponse
import com.mobile.mpvandroid.persegipjg.utils.SesssionManager
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    companion object{
        lateinit var sesssionManager: SesssionManager
        private lateinit var context: Context
    }

    private val api by lazy { BaseRetrofit().endpoint }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sesssionManager = SesssionManager(this)

        val loginStatus = sesssionManager.getBoolean("LOGIN_STATUS")
        if (loginStatus){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //deklar local layout
        val login = findViewById(R.id.btnLogin) as Button
        val email = findViewById(R.id.txtEmail) as TextInputEditText
        val password = findViewById(R.id.txtPassword) as TextInputEditText

        login.setOnClickListener{
            api.login(email.text.toString(),password.text.toString()).enqueue(object  : retrofit2.Callback<LoginResponse>
            {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    Log.e("Login", response.toString())
                    val correct = response.body()!!.success

                    if (correct) {
                        val token = response.body()!!.data.token
                        sesssionManager.saveString("TOKEN", "Bearer"+token)
                        sesssionManager.saveBoolean("LOGIN_STATUS", true)
                        sesssionManager.saveString("ADMIN_ID",response.body()!!.data.admin.id.toString())

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(applicationContext, "User dan password salah", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("LoginError", t.toString())
                }

            })
        }

    }
}