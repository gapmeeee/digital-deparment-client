package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import org.json.JSONObject
import retrofit.Api.AuthApi
import retrofit.moduls.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

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

        val linearlayoutEmail : LinearLayout = findViewById(R.id.linearLayoutEmail)
        val linearlayoutPass : LinearLayout = findViewById(R.id.linearLayoutPass)
        val linearlayoutName : LinearLayout = findViewById(R.id.linearLayoutName)
        val linearlayoutSurname : LinearLayout = findViewById(R.id.linearLayoutSurname)
        val linearlayoutFatherName : LinearLayout = findViewById(R.id.linearLayoutFatherName)
        val linearlayoutGroup : LinearLayout = findViewById(R.id.linearLayoutGroup)
        val linearlayoutStudentId : LinearLayout = findViewById(R.id.linearLayoutStudentId)
        val linearlayoutBirthday: LinearLayout = findViewById(R.id.linearLayoutBirthday)

        val email: EditText = findViewById(R.id.emailReg)
        val password: EditText = findViewById(R.id.passwordReg)
        val firstName: EditText = findViewById(R.id.firstName)
        val secondName: EditText = findViewById(R.id.secondName)
        val fatherName: EditText = findViewById(R.id.fatherName)
        val numberPhone: EditText = findViewById(R.id.numberPhone)
        val birthday: EditText = findViewById(R.id.birthday)
        val group: EditText = findViewById(R.id.group)
        val regButton: ImageButton = findViewById(R.id.buttonReg)
        val testTV: TextView = findViewById(R.id.testTV1)
        val studentId: EditText = findViewById(R.id.studentId)

        email.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                linearlayoutEmail.setBackgroundResource(R.drawable.shape_roundcube_bluestroke)
            } else
                linearlayoutEmail.setBackgroundResource(R.drawable.shape_roundcube)
        }

        password.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                linearlayoutPass.setBackgroundResource(R.drawable.shape_roundcube_bluestroke)
            } else
                linearlayoutPass.setBackgroundResource(R.drawable.shape_roundcube)

        }

        firstName.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                linearlayoutName.setBackgroundResource(R.drawable.shape_roundcube_bluestroke)
            } else
                linearlayoutName.setBackgroundResource(R.drawable.shape_roundcube)

        }

        secondName.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                linearlayoutSurname.setBackgroundResource(R.drawable.shape_roundcube_bluestroke)
            } else
                linearlayoutSurname.setBackgroundResource(R.drawable.shape_roundcube)

        }

        fatherName.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                linearlayoutFatherName.setBackgroundResource(R.drawable.shape_roundcube_bluestroke)
            } else
                linearlayoutFatherName.setBackgroundResource(R.drawable.shape_roundcube)

        }
        birthday.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                linearlayoutBirthday.setBackgroundResource(R.drawable.shape_roundcube_bluestroke)
            } else
                linearlayoutBirthday.setBackgroundResource(R.drawable.shape_roundcube)

        }
        group.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                linearlayoutGroup.setBackgroundResource(R.drawable.shape_roundcube_bluestroke)
            } else
                linearlayoutGroup.setBackgroundResource(R.drawable.shape_roundcube)
        }
        studentId.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                linearlayoutStudentId.setBackgroundResource(R.drawable.shape_roundcube_bluestroke)
            } else
                linearlayoutStudentId.setBackgroundResource(R.drawable.shape_roundcube)
        }


        regButton.setOnClickListener() {
            when {
                //password.length() < 8 -> testTV.setText("неправильный пароль")
                //numberPhone.text.toString().length != 11 -> testTV.setText("неправильный телефон")
                !CheckValidEmail(email.text.toString()) -> testTV.setText("неправильное мыло")
                else -> {
                    val user = User(
                        email = email.text.toString(),
                        phoneNumber = numberPhone.text.toString(),
                        first_name = firstName.text.toString(),
                        last_name = secondName.text.toString(),
                        father_name = fatherName.text.toString(),
                        password = password.text.toString(),
                        group = group.text.toString(),
                        studentId = studentId.text.toString(),
                        birthday = birthday.text.toString()
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                            val response = authApi.registration(user)
//                            val message = response.errorBody()?.string()1
//                                ?.let { it1 -> JSONObject(it1).getString("message") }
//                            testTV.setText(message)
                            runOnUiThread {
                                val intent = Intent(this@RegActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }

                    }
                }
            }
        }
    }

}

    private fun CheckValidEmail(email: String):Boolean{
//        val charsToCheck = listOf('.', '@')
//        val charCount = mutableMapOf<Char, Int>()
//        for(char in email) {
//            if (char in charsToCheck){
//                charCount[char] = charCount.getOrDefault(char, 0) + 1
//
//            }
//        }
//        for (char in charsToCheck) {
//            if (charCount[char] != 1) {
//                return false
//            }
//        }
        return true
    }
