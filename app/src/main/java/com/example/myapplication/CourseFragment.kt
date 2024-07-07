package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit.Api.AuthApi

import retrofit.moduls.Course
import retrofit.moduls.Lecture
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CourseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CourseFragment : Fragment() {

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_course, container, false)
        val linearLayoutTasks = view.findViewById<LinearLayout>(R.id.linearLayoutTasks)
        val textHead: TextView = view.findViewById(R.id.textviewHead)
        val nameCourse: TextView = view.findViewById(R.id.nameCourse)
//        val token = getAuthToken()
//        if (token != null) {
//            CoroutineScope(Dispatchers.IO).launch {
//                val course = authApi.getCourse(token)
//
//            }
//            requireActivity().runOnUiThread {
//                val intent = Intent(requireContext(), MainActivity2::class.java)
//                startActivity(intent)
//                requireActivity().finish()
//            }
//        }
        CoroutineScope(Dispatchers.IO).launch {
            val token = getAuthToken()
            if (token != null) {
                try {
                    val courseResponse = authApi.getCourse(token)
                    requireActivity().runOnUiThread {
                        nameCourse.setText(courseResponse.title)
                        addLecturesToLayout(courseResponse.lectures, linearLayoutTasks)
                    }
                } catch (e: Exception) {

                }
            } else {
                requireActivity().runOnUiThread {
                    val intent = Intent(requireContext(), RegActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
        return view
    }
    fun addLecturesToLayout(lectures: List<Lecture>, linearLayout: LinearLayout) {
        lectures.forEach { lecture ->
            val lectureView = layoutInflater.inflate(R.layout.item_lecture, linearLayout, false)
            val nameLecture = lectureView.findViewById<TextView>(R.id.nameLecture)
            val textTaskDescription1 = lectureView.findViewById<TextView>(R.id.textTaskDescription1)
            val date = lectureView.findViewById<TextView>(R.id.date)

            nameLecture.text = lecture.name
            textTaskDescription1.text = lecture.comment
            date.text = lecture.dateOfCreated.substring(0,10)

            linearLayout.addView(lectureView)
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CourseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = CourseFragment()
    }
    private fun getAuthToken(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }
}