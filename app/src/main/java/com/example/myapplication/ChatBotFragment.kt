
package com.example.myapplication

//import QuestionsAdapter
import ThemesAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit.Api.AuthApi
import retrofit.moduls.FAQ
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class ChatBotFragment : Fragment() {

    private lateinit var linearLayoutAnswer: LinearLayout
    private lateinit var linearLayoutQuestion: LinearLayout
    private lateinit var linearLayoutMessages: LinearLayout

    private lateinit var recyclerViewThemes: RecyclerView
    private lateinit var recyclerViewQuestions: RecyclerView
    private lateinit var faqs: List<FAQ>
    private lateinit var scrollViewMessages: ScrollView
    private lateinit var chatFragment : ChatBotFragment
    private var messageList: MutableList<Pair<String, String>> = mutableListOf()

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
    val authApi = retrofit.create(AuthApi::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_chat_bot, container, false)
          linearLayoutMessages = view.findViewById(R.id.linearLayoutMessages)

        scrollViewMessages = view.findViewById(R.id.scrollViewMessages)
        if (!::faqs.isInitialized) {
            loadFAQs()
        }

        return view
    }


    private fun loadFAQs() {
        CoroutineScope(Dispatchers.IO).launch {
            val token = getAuthToken()
            if (token != null) {
                try {
                    faqs = authApi.getFAQs(token)
                    requireActivity().runOnUiThread {
                        populateThemes(faqs)
                    }
                } catch (e: Exception) {
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

    private fun populateThemes(themes: List<FAQ>) {
        val flexboxLayoutThemes = view?.findViewById<FlexboxLayout>(R.id.linearLayoutThemes) ?: return
        flexboxLayoutThemes.removeAllViews() // Убедитесь, что предыдущие элементы удалены
        themes.forEach { theme ->
            val themeView = LayoutInflater.from(requireContext()).inflate(R.layout.theme_item, flexboxLayoutThemes, false)
            val themeText = themeView.findViewById<TextView>(R.id.TextEditTheme)
            themeText.text = theme.themes
            if(theme.faqQuestions!=null){
                themeText.setOnClickListener {
                    showQuestions(theme)
                }
                flexboxLayoutThemes.addView(themeView)
            }

        }
    }



    private fun showQuestions(questions: FAQ) {
        val flexboxLayoutThemes = view?.findViewById<FlexboxLayout>(R.id.linearLayoutThemes) ?: return
        flexboxLayoutThemes.removeAllViews()
        questions.faqQuestions.forEach { question ->
            val themeView = LayoutInflater.from(requireContext()).inflate(R.layout.questions_item, flexboxLayoutThemes, false)
            val questionText = themeView.findViewById<TextView>(R.id.TextQuestion)
            questionText.text = question.questions
            questionText.setOnClickListener {
                sendMessages(question.questions, question.answers)
            }
            flexboxLayoutThemes.addView(themeView)
        }
    }

    private fun sendMessages(question : String, answer: String) {
        val questionView = layoutInflater.inflate(R.layout.question_item, linearLayoutMessages, false)
        val questionText = questionView.findViewById<TextView>(R.id.question)
        questionText.text = question
        linearLayoutMessages.addView(questionView)


        val answerView = layoutInflater.inflate(R.layout.answer_item, linearLayoutMessages, false)
        val answerText = answerView.findViewById<TextView>(R.id.answer)
        answerText.text = answer
        linearLayoutMessages.addView(answerView)
        scrollViewMessages.post {
            scrollViewMessages.fullScroll(View.FOCUS_DOWN)
        }
        populateThemes(faqs)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ChatBotFragment()
    }

}