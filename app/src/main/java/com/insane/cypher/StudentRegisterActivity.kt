package com.insane.cypher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class StudentRegisterActivity : AppCompatActivity() {

    private lateinit var spnYear : Spinner
    private lateinit var spnBranch : Spinner
    private lateinit var edtName: EditText
    private lateinit var edtEmail : EditText
    private lateinit var edtPass : EditText
    private lateinit var btn : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var db : FirebaseDatabase
    private lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_register)
        db = FirebaseDatabase.getInstance()
        ref = db.reference

        mAuth = FirebaseAuth.getInstance()

        spnYear = findViewById(R.id.spinnerSRYear)
        spnBranch = findViewById(R.id.spinnerSRBranch)
        edtName = findViewById(R.id.editTextSRName)
        edtEmail = findViewById(R.id.editTextSREmail)
        edtPass = findViewById(R.id.editTextSRPass)
        btn = findViewById(R.id.buttonSR)

        btn.setOnClickListener {

            register(edtEmail.text.toString().toLowerCase(Locale.ROOT).trim(), edtPass.text.toString().trim())
        }
    }

    private fun register(email: String, pass: String) {

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful){
                        mAuth.currentUser!!.sendEmailVerification()
                            .addOnCompleteListener { task2 ->
                                if (task2.isSuccessful){
                                    Toast.makeText(this, "Email Verification Send",Toast.LENGTH_SHORT).show()
                                    addToDB(edtEmail.text.trim().toString().toLowerCase(Locale.ROOT), edtPass.text.trim().toString())
                                }
                                else
                                {
                                    Toast.makeText(this,"Network Error",Toast.LENGTH_SHORT).show()
                                }
                            }
                    }else{
                        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun addToDB(email :String, pass :String){

        ref.child("users").child("student").child(split(email)).child("name").setValue(edtName.text.toString())
        ref.child("users").child("student").child(split(email)).child("email").setValue(email)
        ref.child("users").child("student").child(split(email)).child("pass").setValue(pass)
        ref.child("users").child("student").child(split(email)).child("year").setValue(spnYear.selectedItem.toString())
        ref.child("users").child("student").child(split(email)).child("branch").setValue(spnBranch.selectedItem.toString())
                .addOnCompleteListener { task2 ->
                    if (task2.isSuccessful){
                        startActivity(Intent(this, StudentLoginActivity::class.java))
                    }else{
                        Toast.makeText(this, "Database Error!", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun split(email: String): String{
        return email.split("@")[0]
    }
}