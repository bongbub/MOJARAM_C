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
        holder.reservationName.text = reserv.userName
        holder.reservationTimes.text = reserv.reservationTimes.joinToString(", ")
    }
//    override fun onBindViewHolder(holder: AdminAdapter.MyViewHolder, position: Int) {
//        val reserv : Reservation = reservationList[position]
//        holder.reservationDates.text = reserv.date
//        holder.reservationName.text = reserv.userId
//        holder.reservationTimes.text = reserv.reservationTimes.joinToString(",")
//    }

    override fun getItemCount(): Int {
        return reservationList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val reservationDates : TextView = itemView.findViewById(R.id.reservDate)
        val reservationTimes :TextView = itemView.findViewById(R.id.reservTime)
        val reservationName : TextView = itemView.findViewById(R.id.reservName)
    }
}




//class AdminAdapter : ListAdapter<Reservation, AdminAdapter.AdminViewHolder>(DiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
//        val binding = ItemRecyclerReservlistAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return AdminViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
//        val reservation = getItem(position)
//        holder.bind(reservation)
//    }
//
//    class AdminViewHolder(private val binding: ItemRecyclerReservlistAdminBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(reservation: Reservation) {
//            binding.reservDate.text = reservation.date
//            binding.reservTime.text = reservation.reservationTimes.joinToString(", ")
//            binding.reservName.text = reservation.userId
//        }
//    }
//
//    class DiffCallback : DiffUtil.ItemCallback<Reservation>() {
//        override fun areItemsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
//            return oldItem.date == newItem.date && oldItem.userId == newItem.userId
//        }
//
//        override fun areContentsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
//            return oldItem == newItem
//        }
//    }
//}
