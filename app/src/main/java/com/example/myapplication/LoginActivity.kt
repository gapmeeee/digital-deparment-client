package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit.Api.AuthApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val isAuthenticated = checkUserAuthentication()

        if (isAuthenticated) {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        setContentView(R.layout.activity_login2)

        val linearlayoutEmail : LinearLayout = findViewById(R.id.linearLayoutEmail)
        val linearlayoutPass : LinearLayout = findViewById(R.id.linearLayoutPass)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()


        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.106:8080").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val authApi = retrofit.create(AuthApi::class.java)

        val regbtn: ImageButton = findViewById(R.id.regbtn)
        val authbtn: ImageButton = findViewById(R.id.authbtn)
        val emailTV: EditText = findViewById(R.id.email)
        val passwordTV: EditText = findViewById(R.id.password)
        //val textView: TextView = findViewById(R.id.TVTest)

        emailTV.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                linearlayoutEmail.setBackgroundResource(R.drawable.shape_roundcube_bluestroke)
            } else
                linearlayoutEmail.setBackgroundResource(R.drawable.shape_roundcube)
        }

        passwordTV.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                linearlayoutPass.setBackgroundResource(R.drawable.shape_roundcube_bluestroke)
            } else
                linearlayoutPass.setBackgroundResource(R.drawable.shape_roundcube)
        }


        regbtn.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegActivity::class.java)
            startActivity(intent)
            finish()
        }

        authbtn.setOnClickListener {
            val t = emailTV.text.toString() + ":" + passwordTV.text.toString()
            val token = "Basic " + Base64.encodeToString(t.toByteArray(), Base64.NO_WRAP)
                CoroutineScope(Dispatchers.IO).launch {
                        saveAuthToken(token)
                        val user = authApi.authorization(token)
                        saveAuthToken(token)
                        runOnUiThread {
                            val intent = Intent(this@LoginActivity, MainActivity2::class.java)
                            startActivity(intent)
                            finish()
                        }

                }
        }
    }
    private fun saveAuthToken(token: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
    }

    private fun checkUserAuthentication(): Boolean {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        return token != null
    }
}