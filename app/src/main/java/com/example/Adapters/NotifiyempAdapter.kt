package com.example.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Interface.Datum
import com.example.Leaves.ApplyLeave
import com.example.hrms_xyug.R
import com.squareup.picasso.Picasso

class NotifiyempAdapter (private val employeeList: ArrayList<Datum>) :
    RecyclerView.Adapter<NotifiyempAdapter.EmployeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notifyemp, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employeeList[position]
        holder.firstName.text = employee.firstName
        val photoUrl = employee.photo

        Log.d("EmployeeAdapter", "Photo URL: $photoUrl")

        if (!photoUrl.isNullOrEmpty()) {
            try {
                Picasso.get()
                    .load(photoUrl)
                    .into(holder.notifyEmpimage)
            } catch (e: Exception) {

                Log.e("EmployeeAdapter", "Error loading image: ${e.message}")
                holder.notifyEmpimage.setImageResource(R.drawable.ic_profile)
            }
        } else {

            holder.notifyEmpimage.setImageResource(R.drawable.ic_profile)
        }
        holder.designation.text = employee.status
        holder.notifyemplayout.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ApplyLeave::class.java)
            intent.putExtra("EMPLOYEE_NUMBER", employee.employeeNumber)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.findViewById(R.id.firstName)
        val notifyEmpimage: ImageView = itemView.findViewById(R.id.notifyemployee_img)
        val designation: TextView = itemView.findViewById(R.id.notifydesignation)
        val notifyemplayout: RelativeLayout = itemView.findViewById(R.id.notifyemp_layout)
    }
}