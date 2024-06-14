package com.example.mojaram.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mojaram.R
import com.example.mojaram.data.reservListdata
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AdminMain : AppCompatActivity() {

    var firestore: FirebaseFirestore? = null
    private var uid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        //val recyclerView: RecyclerView = findViewById(R.id.reserv_recyclerview)
        //recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = RecyclerViewAdapter()
        //recyclerView.adapter = adapter

    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var reservList:ArrayList<reservListdata> = arrayListOf()

        init{
            firestore?.collection("reservation")?.orderBy("shopId", Query.Direction.DESCENDING)
                ?.addSnapshotListener {querySnapshot, firebaseFirestoreException ->
                    reservList.clear()
                    if(querySnapshot == null) return@addSnapshotListener

                    // 데이터 받아오기
                    for (snapshot in querySnapshot!!.documents){
                        var item = snapshot.toObject(reservListdata::class.java)
                        item?.let {
                            reservList.add(it)}
//                        reservListdata.add(item!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_reservlist_admin, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view:View):RecyclerView.ViewHolder(view){
            val reserv_date = view.findViewById<TextView>(R.id.reserv_date)
            val reserv_time = view.findViewById<TextView>(R.id.reserv_time)

            fun bind(reservData: reservListdata) {
                reserv_date.text = reservData.date
                reserv_time.text = reservData.reservationTimes
            }

        }

        override fun getItemCount(): Int {
            return reservList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as CustomViewHolder).bind(reservList[position])
//            var viewHolder = (holder as CustomHolder).itemView
//            viewHolder.reserv_date.text = reservList[position].date
//            viewHolder.reserv_time.text = reservList[position].reservationTimes
        }
    }
}