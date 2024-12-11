package com.example.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.Interface.Attendance
import com.example.hrms_xyug.R
import java.text.SimpleDateFormat
import java.util.Locale

class AttendanceHistoryAdapter(private val attendanceList: List<Attendance>) : RecyclerView.Adapter<AttendanceHistoryAdapter.AttendanceViewHolder>() {

    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clockInTimeTextView: TextView = itemView.findViewById(R.id.clockInTimeTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val clockOutTimeTextView: TextView = itemView.findViewById(R.id.clockOutTimeTextView)
        val totalHoursTextView: TextView = itemView.findViewById(R.id.totalHoursTextView)
        val lateLoginTextView: TextView = itemView.findViewById(R.id.lateLoginStatusTextView)


        val dateTextView1: TextView = itemView.findViewById(R.id.dateTextView1)
        val clockInTimeTextView1: TextView = itemView.findViewById(R.id.clockInTimeTextView1)
        val lateLoginStatusTextView1: TextView = itemView.findViewById(R.id.lateLoginStatusTextView1)
        val clockOutTimeTextView1: TextView = itemView.findViewById(R.id.clockOutTimeTextView1)
        val totalHoursTextView1: TextView = itemView.findViewById(R.id.totalHoursTextView1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attendance_history, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendance = attendanceList[position]

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        attendance.date?.let { date ->
            val formattedDate = dateFormat.format(date)
            holder.dateTextView1.text = "$formattedDate"
        } ?: run {
            holder.dateTextView1.text = "Unknown"
        }

        attendance.clockInTime?.let { clockInTime ->
            val formattedClockInTime = timeFormat.format(clockInTime)
            holder.clockInTimeTextView1.text = "$formattedClockInTime"
        } ?: run {
            holder.clockInTimeTextView1.text = "Unknown"
        }

        attendance.clockOutTime?.let { clockOutTime ->
            val formattedClockOutTime = timeFormat.format(clockOutTime)
            holder.clockOutTimeTextView1.text = "$formattedClockOutTime"
        } ?: run {
            holder.clockOutTimeTextView1.text = "Unknown"
        }


        holder.totalHoursTextView1.text = "${attendance.totalHoursWorked} Hours"

        when (attendance.status) {
            "Present" -> {
                holder.lateLoginStatusTextView1.text = "Present"
                holder.lateLoginStatusTextView1.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.bright_green))
            }
            "Absent" -> {
                holder.lateLoginStatusTextView1.text = "Absent"
                holder.lateLoginStatusTextView1.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.app_red))
            }
            "Late" -> {
                holder.lateLoginStatusTextView1.text = "Late"
                holder.lateLoginStatusTextView1.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.orange))
            }
            "On-Leave" -> {
                holder.lateLoginStatusTextView1.text = "  Leave"
                holder.lateLoginStatusTextView1.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.app_red))
            }
            "Half-Day" -> {
                holder.lateLoginStatusTextView1.text = "Half-Day"
                holder.lateLoginStatusTextView1.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.bright_green))
            }
            else -> {
                holder.lateLoginStatusTextView1.text = "Unknown Status"
                holder.lateLoginStatusTextView1.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.hrms_yellow))
            }
        }
    }

    override fun getItemCount(): Int {
        return attendanceList.size
    }
}
