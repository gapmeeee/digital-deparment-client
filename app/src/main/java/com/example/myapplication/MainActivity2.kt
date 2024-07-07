//package com.example.myapplication
//
//import android.os.Bundle
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.navigation.findNavController
//import androidx.navigation.ui.setupWithNavController
//import com.google.android.material.bottomnavigation.BottomNavigationView
//
//class MainActivity2 : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main2)
//        val btnNavView = findViewById<BottomNavigationView>(R.id.bottomnavigationView)
//        val controller = findNavController(R.id.fragmentContainerView)
//        btnNavView.setupWithNavController(controller)
//
//    }
//}


package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        bottomNavigationView = findViewById(R.id.bottomnavigationView)

        if (savedInstanceState == null) {
            val initialFragment = CourseFragment.newInstance("param1", "param2")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, initialFragment)
                .addToBackStack(null)
                .commit()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.chatBotFragmentm -> {
                    replaceFragment(ChatBotFragment.newInstance("param1", "param2"))
                    true
                }
                R.id.profileFragment2m -> {
                    replaceFragment(ProfileFragment.newInstance())
                    true
                }
                R.id.courseFragment2m -> {
                    replaceFragment(CourseFragment.newInstance("param1", "param2"))
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null) // Добавляем в стек возврата для сохранения состояния
            .commit()
    }
}