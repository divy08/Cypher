package com.insane.cypher

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.insane.cypher.dataclass.TimeTableData
import java.lang.Exception

class TimetableActivity : AppCompatActivity() {

    private lateinit var lv :ListView
    private lateinit var timeTableArray :ArrayList<TimeTableData>
    private lateinit var st :FirebaseStorage
    private lateinit var stRef :StorageReference
    private lateinit var db :FirebaseDatabase
    private lateinit var ref :DatabaseReference
    private lateinit var mAuth :FirebaseAuth
    private lateinit var branch:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)
        mAuth = FirebaseAuth.getInstance()
        db= FirebaseDatabase.getInstance()
        ref = db.reference
        st = FirebaseStorage.getInstance()
        stRef = st.getReferenceFromUrl("gs://cypher-e0f15.appspot.com")

        timeTableArray = ArrayList()
        lv = findViewById(R.id.listViewTT)

        getTimeTable()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_time_table, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.itemMenuTT -> {
                startActivity(Intent(this, PopupTimetableActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class TimeTableAdapter(val ctx :Context,private val arr :ArrayList<TimeTableData>) :BaseAdapter(){

        override fun getCount(): Int {
            return arr.size
        }

        override fun getItem(position: Int): Any {
            return arr[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val timeTable = arr[position]
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.cardview_timetable, null)
            val txtBranch = view.findViewById<TextView>(R.id.textViewCVTTBranch)
            val txtYear = view.findViewById<TextView>(R.id.textViewCVTTYear)
            val txtDate = view.findViewById<TextView>(R.id.textViewCVTTDate)
            val img = view.findViewById<ImageView>(R.id.imageViewCVTT)
            txtBranch.text = timeTable.branch
            txtYear.text = timeTable.year
            txtDate.text = timeTable.date
            Glide.with(ctx).load(timeTable.imgPath).into(img)
            return view
        }

    }

    private fun getTimeTable(){

        ref.child("users").child("faculty").child(split(mAuth.currentUser!!.email!!)).child("branch")
            .addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    branch = snapshot.value.toString()
                    Log.i("branch", branch)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            })

        timeTableArray.clear()
        getFirstYearTT()
        getSecondYearTT()
        getThirdYearTT()
        getForthYearTT()
    }

    private fun getFirstYearTT(){

        try {
            ref.child("users").child("faculty").child(split(mAuth.currentUser!!.email!!)).child("timetable")
                    .child("first").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            try {
                                    val date = snapshot.child("date").value as String
                                    val path = snapshot.child("path").value as String

                                    timeTableArray.add(TimeTableData(branch, "1st", date, path))
                                    lv.adapter = TimeTableAdapter(applicationContext, timeTableArray)
                                    lv.deferNotifyDataSetChanged()

                            }catch (ex :Exception){}
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    })
        }catch (ex :Exception){}
    }

    private fun getSecondYearTT(){

        try {
            ref.child("users").child("faculty").child(split(mAuth.currentUser!!.email!!)).child("timetable")
                    .child("second").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            try {
                                val date = snapshot.child("date").value as String
                                val path = snapshot.child("path").value as String

                                timeTableArray.add(TimeTableData(branch, "2nd", date, path))
                                lv.adapter = TimeTableAdapter(applicationContext, timeTableArray)
                                lv.deferNotifyDataSetChanged()
                            }catch (ex :Exception){}
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    })
        }catch (ex :Exception){}
    }

    private fun getThirdYearTT(){

        try {
            ref.child("users").child("faculty").child(split(mAuth.currentUser!!.email!!)).child("timetable")
                    .child("third").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            try {
                                val date = snapshot.child("date").value as String
                                val path = snapshot.child("path").value as String

                                timeTableArray.add(TimeTableData(branch, "3rd", date, path))
                                lv.adapter = TimeTableAdapter(applicationContext, timeTableArray)
                                lv.deferNotifyDataSetChanged()
                            }catch (ex :Exception){}
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    })
        }catch (ex :Exception){}
    }

    private fun getForthYearTT(){

        try {
            ref.child("users").child("faculty").child(split(mAuth.currentUser!!.email!!)).child("timetable")
                    .child("forth").addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {

                            try {
                                val date = snapshot.child("date").value as String
                                val path = snapshot.child("path").value as String

                                timeTableArray.add(TimeTableData(branch, "4th", date, path))
                                lv.adapter = TimeTableAdapter(applicationContext, timeTableArray)
                                lv.deferNotifyDataSetChanged()
                            }catch (ex :Exception){}
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                        }
                    })
        }catch (ex :Exception){}
    }


    private fun split(email :String): String{
        return email.split("@")[0]
    }
}