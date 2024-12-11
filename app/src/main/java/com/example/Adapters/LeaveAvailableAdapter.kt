package com.example.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Interface.Datum
import com.example.Interface.Datum3
import com.example.hrms_xyug.R

class LeaveAvailableAdapter(private val leaveList: ArrayList<Datum3>) : RecyclerView.Adapter<LeaveAvailableAdapter.LeaveViewHolder>() {

    inner class LeaveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val leaveType: TextView = itemView.findViewById(R.id.leaveType)
        val pendingLeaves: TextView = itemView.findViewById(R.id.pendingLeaves)
        val casualleavetype: RelativeLayout = itemView.findViewById(R.id.casualleavetype)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leavedata_item, parent, false)
        return LeaveViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveViewHolder, position: Int) {
        val leave = leaveList[position]
        holder.leaveType.text = leave.leaveType
        holder.pendingLeaves.text = leave.pendingLeaves.toString()
        holder.casualleavetype.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return leaveList.size
    }

    fun updateData(newLeaveList: ArrayList<Datum3>) {
        leaveList.clear()
        leaveList.addAll(newLeaveList)
        notifyDataSetChanged()
    }
}
