package com.example.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Interface.AllEmployeeModel
import com.example.Interface.SingleEmployeeModel
import com.example.hrms_xyug.R
import com.example.hrms_xyug.SingleEmployeeActivity
import com.squareup.picasso.Picasso

class AllEmpAdapter(private val context: Context, private var employeeList: List<AllEmployeeModel>) : RecyclerView.Adapter<AllEmpAdapter.EmployeeViewHolder>() {

    private var filteredList: List<AllEmployeeModel> = employeeList

    class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val employeeImg: ImageView = itemView.findViewById(R.id.employee_img)
        val allemp: RelativeLayout = itemView.findViewById(R.id.allemp_layout)
        val employeeName: TextView = itemView.findViewById(R.id.allempname_Tv)
        val employeePosition: TextView = itemView.findViewById(R.id.allempdesignation_Tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_employee, parent, false)
        return EmployeeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = filteredList[position]

        holder.employeeName.text = employee.employeeName
        holder.employeePosition.text = employee.jobTitle

        val photoUrl = employee.photo

        Log.d("EmployeeAdapter", "Photo URL: $photoUrl")

        if (!photoUrl.isNullOrEmpty()) {
            try {
                Picasso.get()
                    .load(photoUrl)
                    .into(holder.employeeImg)
            } catch (e: Exception) {

                Log.e("EmployeeAdapter", "Error loading image: ${e.message}")
                holder.employeeImg.setImageResource(R.drawable.ic_profile)
            }
        } else {

            holder.employeeImg.setImageResource(R.drawable.ic_profile)
        }

        holder.allemp.setOnClickListener {
            val intent = Intent(context, SingleEmployeeActivity::class.java)
            intent.putExtra("employee_number", employee.employeeNumber)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = filteredList.size

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            employeeList
        } else {
            employeeList.filter {
                it.employeeName?.contains(query, ignoreCase = true) == true
            }
        }
        notifyDataSetChanged()
    }
}
