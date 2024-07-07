//package com.example.myapplication
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.google.gson.GsonBuilder
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit.Api.AuthApi
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//class ProfileFragment : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_profile, container, false)
//
//        val email: TextView = view.findViewById(R.id.email)
//        val FIO: TextView = view.findViewById(R.id.FIO)
//        val numberPhone: TextView = view.findViewById(R.id.numberPhone)
//        val birthday: TextView = view.findViewById(R.id.birthday)
//        val group: TextView = view.findViewById(R.id.group)
//        val studentId: TextView = view.findViewById(R.id.studentId)
//        val logout: Button = view.findViewById(R.id.logout)
//
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
//            .build()
//        val gson = GsonBuilder()
//            .setLenient()
//            .create()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.0.102:8080").client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create(gson)).build()
//        val authApi = retrofit.create(AuthApi::class.java)
//
//        logout.setOnClickListener(){
//            deleteAuthToken()
//            val intent = Intent(requireContext(), LoginActivity::class.java)
//            startActivity(intent)
//            requireActivity().finish()
//        }
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val token = getAuthToken()
//            if (token!=null){
//                val user = authApi.authorization(token)
//                requireActivity().runOnUiThread {
//                    email.setText(user.email)
//                    FIO.setText("${user.last_name} ${user.first_name} ${user.father_name}")
//                    numberPhone.setText(user.phoneNumber)
//                    group.setText(user.group)
//                    studentId.setText(user.studentId)
//                }
//            }
//            else{
//                requireActivity().runOnUiThread {
//                    val intent = Intent(requireContext(), RegActivity::class.java)
//                    startActivity(intent)
//                    requireActivity().finish()
//                }
//            }
//        }
//        return view
//
//
//    }
//    private fun getAuthToken(): String? {
//        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
//        return sharedPreferences.getString("auth_token", null)
//    }
//    private fun deleteAuthToken() {
//        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.remove("auth_token")
//        editor.apply()
//    }
//    companion object {
//        @JvmStatic
//        fun newInstance() = ProfileFragment()
//    }
//}

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.RegActivity
import com.google.gson.GsonBuilder
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit.Api.AuthApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {

    private lateinit var email: TextView
    private lateinit var FIO: TextView
    private lateinit var numberPhone: TextView
    private lateinit var group: TextView
    private lateinit var studentId: TextView
    private lateinit var birthday: TextView
    private lateinit var course: TextView
    private lateinit var logout: Button
    private lateinit var btnChooseImage: Button
    private lateinit var imgAvatar: ImageView
    private lateinit var authApi: AuthApi

    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        email = view.findViewById(R.id.email)
        FIO = view.findViewById(R.id.FIO)
        numberPhone = view.findViewById(R.id.numberPhone)
        group = view.findViewById(R.id.group)
        studentId = view.findViewById(R.id.studentId)
        birthday = view.findViewById(R.id.birthday)
        logout = view.findViewById(R.id.logout)
        btnChooseImage = view.findViewById(R.id.btnChooseImage)
        imgAvatar = view.findViewById(R.id.imageView2)
        course = view.findViewById(R.id.course)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.106:8080")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        authApi = retrofit.create(AuthApi::class.java)

        logout.setOnClickListener {
            deleteAuthToken()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        btnChooseImage.setOnClickListener {
            openFileChooser()
        }
        CoroutineScope(Dispatchers.IO).launch {
            val token = getAuthToken()
            if (token != null) {
                val user = authApi.authorization(token)
                requireActivity().runOnUiThread {
                    email.text = user.email
                    FIO.text = "${user.last_name} ${user.first_name} ${user.father_name}"
                    numberPhone.text = user.phoneNumber
                    group.text = user.group
                    studentId.text = user.studentId
                    birthday.text = user.birthday
                    course.text = user.course?.title ?: ""
                }

            } else {
                requireActivity().runOnUiThread {
                    val intent = Intent(requireContext(), RegActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = getAuthToken()

                if (token != null) {
                    val response = authApi.getAvatar(token).bytes()
                    if (response != null) {
                        requireActivity().runOnUiThread {
                            Log.d("ProfileFragment", "Image bytes length: ${response?.size}")
                            val bitmap =
                                BitmapFactory.decodeByteArray(response, 0, response.size)
                            imgAvatar.setImageBitmap(bitmap)
                        }
                    } else {
                        // Обработка случая, когда данные изображения пусты
                        Log.e("ProfileFragment", "Image bytes are null")
                    }

                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return view
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            imgAvatar.setImageURI(selectedImageUri) // Display selected image in ImageView
            uploadImage()
        }
    }

    private fun uploadImage() {
        selectedImageUri?.let { uri ->
            val file = File(getRealPathFromURI(uri))

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val compressedFile = Compressor.compress(requireContext(), file){
                        resolution(500, 500)
                        quality(80)
                        size(597_152) // 2 MB
                    }
                    val requestFile = compressedFile.asRequestBody("image/*".toMediaTypeOrNull())

                    val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
                    val token = getAuthToken()
                    if (token != null) {
                        val response = authApi.uploadAvatar(token, body)
                        if (response.isSuccessful) {
                            requireActivity().runOnUiThread {
                                Log.d("Upload", "Image uploaded successfully")
                                // Handle successful upload UI updates if needed
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                Log.e(
                                    "Upload",
                                    "Failed to upload image: ${response.errorBody()?.string()}"
                                )
                                // Handle failed upload UI updates if needed
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getAuthToken(): String? {
        val sharedPreferences =
            requireActivity().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

    private fun deleteAuthToken() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("auth_token")
        editor.apply()
    }

    private fun getRealPathFromURI(uri: Uri): String? {
        var realPath: String? = null
        try {
            requireActivity().contentResolver.openInputStream(uri)?.use { inputStream ->
                val file = File(requireContext().cacheDir, "temp_image")
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                realPath = file.absolutePath
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return realPath
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1

        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

}