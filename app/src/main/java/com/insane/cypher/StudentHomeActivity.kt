package com.insane.cypher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StudentHomeActivity : AppCompatActivity() {


    private lateinit var btnTimetable: Button
    private lateinit var btnSyllabus: Button
    private lateinit var btnNotes: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)

        btnTimetable = findViewById(R.id.buttonSHTimeTable)
        btnSyllabus = findViewById(R.id.buttonSHSyllabus)
        btnNotes = findViewById(R.id.buttonSHNotes)

        btnTimetable.setOnClickListener {
            startActivity(Intent(this, StudentTimetableActivity::class.java))
        }
    }
}
