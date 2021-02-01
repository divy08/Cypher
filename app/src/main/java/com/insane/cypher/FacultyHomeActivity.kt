package com.insane.cypher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FacultyHomeActivity : AppCompatActivity() {

    private lateinit var btnTimetable: Button
    private lateinit var btnSyllabus: Button
    private lateinit var btnNotes: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_home)

        btnTimetable = findViewById(R.id.buttonFHTimeTable)
        btnSyllabus = findViewById(R.id.buttonFHSyllabus)
        btnNotes = findViewById(R.id.buttonFHNotes)

        btnTimetable.setOnClickListener {
            startActivity(Intent(this, TimetableActivity::class.java))
        }
    }
}