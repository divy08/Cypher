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

class FacultyRegisterActivity : AppCompatActivity() {


    private lateinit var spnBranch :Spinner
    private lateinit var edtName: EditText
    private lateinit var edtEmail :EditText
    private lateinit var edtPass :EditText
    private lateinit var btnReg :Button
    private lateinit var mAuth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_register)
        mAuth = FirebaseAuth.getInstance()

        spnBranch = findViewById(R.id.spinnerFRBranch)
        edtName = findViewById(R.id.editTextFRName)
        edtEmail = findViewById(R.id.editTextFREmail)
        edtPass = findViewById(R.id.editTextFRPass)
        btnReg = findViewById(R.id.buttonFRReg)

        btnReg.setOnClickListener {

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
                                Toast.makeText(this, "Verification Sent!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, FacultyLoginActivity::class.java)
                                intent.putExtra("name", edtName.text.toString())
                                intent.putExtra("branch", spnBranch.selectedItem.toString())
                                startActivity(intent)
                            }else{
                                Toast.makeText(this, "Network Error!", Toast.LENGTH_SHORT).show()
                            }
                        }
                }else{
                    Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show()
                }
            }
    }


}