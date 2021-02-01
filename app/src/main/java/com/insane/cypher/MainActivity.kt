package com.insane.cypher

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth :FirebaseAuth
    private lateinit var db :FirebaseDatabase
    private lateinit var ref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        ref = db.reference


        checkIfLoggedIn(this)
    }

    private fun checkIfLoggedIn(ctx :Context){
        if (mAuth.currentUser != null){

            ref.child("users").child("faculty").child(split(mAuth.currentUser!!.email!!))
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            if (snapshot.value != null)
                                startActivity(Intent(ctx, FacultyHomeActivity::class.java))
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(ctx, "Failed!", Toast.LENGTH_SHORT).show()
                        }

                    })

            ref.child("users").child("student").child(split(mAuth.currentUser!!.email!!))
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            Log.i("snapshot", snapshot.toString())
                            if (snapshot.value != null)
                                startActivity(Intent(ctx, StudentHomeActivity::class.java))
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(ctx, "Failed!", Toast.LENGTH_SHORT).show()
                        }

                    })
        }else{
            Handler().postDelayed({
                startActivity(Intent(this, HostActivity::class.java))//done
            }, 500)
        }
    }

    private fun split(email: String): String{
        return email.split("@")[0]
    }
}