package com.insane.cypher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception
import java.util.*

class FacultyLoginActivity : AppCompatActivity() {

    private lateinit var btnFacultyReg :Button
    private lateinit var edtEmail : EditText
    private lateinit var edtPass : EditText
    private lateinit var btnLogin :Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var bundle :Bundle
    private lateinit var name :String
    private lateinit var branch :String
    private lateinit var db : FirebaseDatabase
    private lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_login)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        ref = db.reference

        edtEmail = findViewById(R.id.editTextFLEmail)
        edtPass = findViewById(R.id.editTextFLPass)
        btnLogin= findViewById(R.id.buttonFLLogin)
        btnFacultyReg = findViewById(R.id.buttonFLRegister)
        btnFacultyReg = findViewById(R.id.buttonFLRegister)

        try {
            bundle = intent.extras!!
            name = bundle.getString("name")!!
            branch = bundle.getString("branch")!!
        }catch (ex :Exception){
            ex.printStackTrace()
        }

        btnLogin.setOnClickListener {

            login(edtEmail.text.toString().toLowerCase(Locale.ROOT).trim(), edtPass.text.toString().trim())
        }

        btnFacultyReg.setOnClickListener {
            startActivity(Intent(this, FacultyRegisterActivity::class.java))
        }
    }

    private fun login(email: String, pass: String) {

        mAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->

                if (task.isSuccessful){
                    if (mAuth.currentUser!!.isEmailVerified){
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                        addToDB(email, pass)
                    }else{
                        Toast.makeText(this, "Please, verify your email!", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Login Failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addToDB(email :String, pass :String){

        if (ref.child("users").child("faculty").child(split(email)).key != split(email)){

            ref.child("users").child("faculty").child(split(email)).child("name").setValue(name)
            ref.child("users").child("faculty").child(split(email)).child("email").setValue(email)
            ref.child("users").child("faculty").child(split(email)).child("pass").setValue(pass)
            ref.child("users").child("faculty").child(split(email)).child("branch").setValue(branch)
                    .addOnCompleteListener { task2 ->
                        if (task2.isSuccessful){
                            Toast.makeText(this, "Verification Successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, FacultyHomeActivity::class.java))
                        }else{
                            Toast.makeText(this, "Database Error!", Toast.LENGTH_SHORT).show()
                        }
                    }
        }else{
            startActivity(Intent(this, FacultyHomeActivity::class.java))
        }

    }

    private fun split(email :String): String{
        return email.split("@")[0]
    }
}