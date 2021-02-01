package com.insane.cypher

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class StudentTimetableActivity : AppCompatActivity() {

    private lateinit var img : ImageView
    private lateinit var facultyEmail :EditText
    private lateinit var btnAdd :Button
    private lateinit var st : FirebaseStorage
    private lateinit var stRef : StorageReference
    private lateinit var db :FirebaseDatabase
    private lateinit var ref :DatabaseReference
    private lateinit var sp :SharedPreferences
    private  lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_timetable)
        db = FirebaseDatabase.getInstance()
        ref = db.reference
        st = FirebaseStorage.getInstance()
        stRef = st.getReferenceFromUrl("gs://cypher-e0f15.appspot.com/timetable")
        sp = getSharedPreferences("faculty", MODE_PRIVATE)
        mAuth= FirebaseAuth.getInstance()

        img = findViewById(R.id.imageViewST)
        facultyEmail = findViewById(R.id.editTextSTEmail)
        btnAdd = findViewById(R.id.buttonSTAdd)

        btnAdd.setOnClickListener {

            if (facultyEmail.text.toString().trim().isEmpty())
                Toast.makeText(this,"Please Enter Email!",Toast.LENGTH_SHORT).show()
            else{
                sp.edit().putString("email", facultyEmail.text.toString().trim().toLowerCase(Locale.ROOT)).apply()
                getFromDatabase(this)
            }
        }

        getFromDatabase(this)

    }

    private fun getFromDatabase(ctx :Context){

        val email = sp.getString("email", "null")
        var year = ""

        ref.child("users").child("student").child(split(mAuth.currentUser!!.email!!)).child("year")
                .addListenerForSingleValueEvent(object  :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        when(snapshot.value){
                            "1st" -> year = "first"
                            "2ud" -> year = "second"
                            "3rd" -> year = "third"
                            "4th" -> year = "forth"
                        }
                        Handler().postDelayed({
                            if (email != "null"){
                                ref.child("users").child("faculty").child(split(email!!)).child("timetable")
                                        .child(year).child("path").addListenerForSingleValueEvent(object :ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                loadImage(snapshot.value.toString())
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                Toast.makeText(ctx,"Network Error!",Toast.LENGTH_SHORT).show()
                                            }


                                        })
                            }
                        }, 100)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(ctx,"Network Error!",Toast.LENGTH_SHORT).show()
                    }

                })


    }

    private fun loadImage(path :String){

        Glide.with(this).load(path).into(img)
    }

    private fun split(email: String): String{
        return email.split("@")[0]
    }
}