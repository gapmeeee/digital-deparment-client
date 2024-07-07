package com.example.myapplication

import android.content.Intent
import android.os.Bundle
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

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        val email: TextView = findViewById(R.id.email)
        val FIO: TextView  = findViewById(R.id.FIO)
        val numberPhone: TextView = findViewById(R.id.numberPhone)
        val birthday: TextView = findViewById(R.id.birthday)
        val group: TextView = findViewById(R.id.group)
        val studentId: TextView = findViewById(R.id.studentId)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.102:8080").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        val authApi = retrofit.create(AuthApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val token = getAuthToken()
            if (token!=null){
                val user = authApi.authorization(token)
                runOnUiThread {
                    email.setText(user.email)
                    FIO.setText("${user.last_name} ${user.first_name} ${user.father_name}")
                    numberPhone.setText(user.phoneNumber)
                    group.setText(user.group)
                    studentId.setText(user.studentId)
                }
            }
            else{
                runOnUiThread {
                    val intent = Intent(this@ProfileActivity, RegActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
}