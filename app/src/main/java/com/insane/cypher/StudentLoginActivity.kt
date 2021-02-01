package com.insane.cypher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import java.util.*

class StudentLoginActivity : AppCompatActivity() {

    private lateinit var btnStudentReg : Button
    private lateinit var edtEmail : EditText
    private lateinit var edtPass : EditText
    private lateinit var btnLogin : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var bundle :Bundle
    private lateinit var name :String
    private  lateinit var year : String
    private lateinit var branch :String
    private lateinit var db : FirebaseDatabase
    private lateinit var ref : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_login)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        ref = db.reference

        try {
            bundle = intent.extras!!
            name = bundle.getString("name")!!
            year = bundle.getString("year")!!
            branch = bundle.getString("branch")!!
        }catch ( ex: Exception){
            ex.printStackTrace()
        }

        edtEmail = findViewById(R.id.editTextSLEmail)
        edtPass = findViewById(R.id.editTextSLPass)
        btnLogin= findViewById(R.id.buttonSLLogin)
        btnStudentReg = findViewById(R.id.buttonSLRegister)

        btnLogin.setOnClickListener {

            login(edtEmail.text.toString().toLowerCase(Locale.ROOT).trim(), edtPass.text.toString().trim())
        }

        btnStudentReg = findViewById(R.id.buttonSLRegister)

        btnStudentReg.setOnClickListener {
            startActivity(Intent(this, StudentRegisterActivity::class.java))
        }
    }

    private fun login(email: String, pass: String) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful){
                        if (mAuth.currentUser!!.isEmailVerified){
                            startActivity(Intent(this, StudentHomeActivity::class.java))
                        }else{
                            Toast.makeText(this, "Please, Verify Your Email!", Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
    }

}