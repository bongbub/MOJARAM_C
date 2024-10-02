package com.example.mojaram.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.example.mojaram.R
import com.example.mojaram.databinding.ItemRecyclerReservlistAdminBinding
import com.example.mojaram.model.Reservation
import com.google.firebase.firestore.FirebaseFirestore

class AdminAdapter(private val reservationList : ArrayList<Reservation>) : RecyclerView.Adapter<AdminAdapter.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_reservlist_admin, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val reserv = reservationList[position]
        holder.reservationDates.text = reserv.date
        holder.reservationName.text = reserv.userId
        holder.reservationTimes.text = reserv.reservationTimes.joinToString(", ")
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val reservationDates : TextView = itemView.findViewById(R.id.reservDate)
        val reservationTimes :TextView = itemView.findViewById(R.id.reservTime)
        val reservationName : TextView = itemView.findViewById(R.id.reservName)
    }
}

