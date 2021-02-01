package com.insane.cypher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HostActivity : AppCompatActivity() {

    private lateinit var btnFacultyLogin :Button
    private lateinit var btnStudentLogin :Button
    private lateinit var firebaseAnalytics :FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        btnFacultyLogin = findViewById(R.id.buttonHostFacultyLogin)
        btnStudentLogin = findViewById(R.id.buttonHostStudentLogin)

        btnFacultyLogin.setOnClickListener {
            val intent = Intent(this, FacultyLoginActivity::class.java)
            startActivity(intent)
        }

        btnStudentLogin.setOnClickListener {
            val intent = Intent(this, StudentLoginActivity::class.java)
            startActivity(intent)
        }
    }

}