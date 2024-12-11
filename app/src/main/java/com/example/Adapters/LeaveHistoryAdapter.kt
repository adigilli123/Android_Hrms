package com.example.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Interface.Data2
import com.example.hrms_xyug.R
import java.text.SimpleDateFormat
import java.util.Locale

class LeaveHistoryAdapter (private val leaveHistoryList: List<Data2>) :
    RecyclerView.Adapter<LeaveHistoryAdapter.LeaveHistoryViewHolder>() {

    class LeaveHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leaveTypeTextView: TextView = view.findViewById(R.id.leaveTypeTextView)
        val fromDateTextView: TextView = view.findViewById(R.id.fromDateTextView)
        val toDateTextView: TextView = view.findViewById(R.id.toDateTextView)
        val leaveStatusTextView: TextView = view.findViewById(R.id.leaveStatusTextView)
        val leavecount: TextView = view.findViewById(R.id.leavecount_Tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leave_history, parent, false)
        return LeaveHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaveHistoryViewHolder, position: Int) {
        val leave = leaveHistoryList[position]
        holder.leaveTypeTextView.text = leave.leaveType ?: "N/A"
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        holder.fromDateTextView.text = leave.fromDate?.let { dateFormat.format(it) } ?: "N/A"
        holder.toDateTextView.text = leave.toDate?.let { dateFormat.format(it) } ?: "N/A"
        holder.leaveStatusTextView.text = leave.leaveStatus ?: "N/A"
        holder.leavecount.text = (leave.leavesCount ?: "N/A").toString()
    }

    override fun getItemCount(): Int = leaveHistoryList.size
}