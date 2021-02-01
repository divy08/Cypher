package com.insane.cypher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*


class PopupTimetableActivity : AppCompatActivity() {


    private lateinit var txtBranch : TextView
    private lateinit var spnYear : Spinner
    private lateinit var imgPreview : ImageView
    private lateinit var btnSelectFile : Button
    private lateinit var btnUpload : Button
    private lateinit var db :FirebaseDatabase
    private lateinit var ref :DatabaseReference
    private lateinit var mAuth :FirebaseAuth
    private lateinit var storage :FirebaseStorage
    private lateinit var stRef :StorageReference
    private lateinit var email :String
    private lateinit var selectedImage :Uri
    private var isImageSelected = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_timetable)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        ref = db.reference
        storage = FirebaseStorage.getInstance()
        stRef = storage.getReferenceFromUrl("gs://cypher-e0f15.appspot.com/timetable")

        txtBranch = findViewById(R.id.textViewPuTT)
        spnYear = findViewById(R.id.spinnerPuTTYear)
        btnUpload = findViewById(R.id.buttonPuTTUpload)
        btnSelectFile = findViewById(R.id.buttonPuTTSelectFile)
        imgPreview = findViewById(R.id.imageViewPuTT)

        getBranch()

        btnSelectFile.setOnClickListener {
            checkPermission()
        }

        btnUpload.setOnClickListener {
            if (isImageSelected)
                sendData()
            else
                Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onStart() {
        super.onStart()
        getBranch()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 78){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage()
            }else{
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 69 && data != null && resultCode == Activity.RESULT_OK){

            selectedImage = data.data!!
            val imagePath = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, imagePath, null, null, null)
            cursor!!.moveToFirst()
            val colIndex = cursor.getColumnIndex(imagePath[0])
            val imageCol = cursor.getString(colIndex)
            cursor.close()
            imgPreview.setImageBitmap(BitmapFactory.decodeFile(imageCol))
            isImageSelected = true
        }
    }

    private fun selectImage(){

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 69)
    }

    private fun checkPermission(){

        if (Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 78)
                return
            }
        }
        //Toast.makeText(this, "99", Toast.LENGTH_SHORT).show()
        selectImage()
    }

    private fun getBranch(){

        email = mAuth.currentUser!!.email.toString()
        ref.child("users").child("faculty").child(split(email)).child("branch")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        txtBranch.text = snapshot.value.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
    }

    @SuppressLint("SimpleDateFormat")
    private fun sendData(){

        email = mAuth.currentUser!!.email.toString()
        val year = when(spnYear.selectedItem.toString()){

            "1st" -> "first"
            "2nd" -> "second"
            "3rd" -> "third"
            else -> "forth"
        }

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        ref.child("users").child("faculty").child(split(email)).child("timetable")
                .child(year).child("date").setValue(currentDate).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        saveImageToFirebase(year)
                    }else{
                        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun saveImageToFirebase(year: String){

        val fileName = txtBranch.text.toString() + year + ".jpg"
        val imageRef = stRef.child("timetable/$fileName")

        imageRef.putFile(selectedImage)
                .addOnSuccessListener { taskSnapshot ->
                    val url = taskSnapshot.storage.downloadUrl

                    url.addOnSuccessListener { uri ->
                        ref.child("users").child("faculty").child(split(email)).child("timetable")
                            .child(year).child("path").setValue(uri.toString())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful){
                                    Toast.makeText(this, "Data Added!", Toast.LENGTH_SHORT).show()
                                    finish()
                                }else{
                                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
        }.addOnFailureListener {
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun split(email: String): String{
        return email.split("@")[0]
    }

}