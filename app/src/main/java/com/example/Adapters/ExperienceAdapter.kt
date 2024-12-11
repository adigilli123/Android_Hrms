package com.example.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Interface.Experience
import com.example.Interface.SingleEmployeeModel
import com.example.hrms_xyug.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExperienceAdapter(private val experiences: List<Experience>) :
    RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_experience, parent, false)
        return ExperienceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        val experience = experiences[position]
        holder.bind(experience, position + 1)
    }

    override fun getItemCount(): Int = experiences.size

    class ExperienceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val experienceJob_Tv: TextView = itemView.findViewById(R.id.experienceJob_Tv)
        private val experienceCompany_Tv: TextView = itemView.findViewById(R.id.experienceCompany_Tv)
        private val experienceStartdatetext_Tv: TextView = itemView.findViewById(R.id.experienceStartdatetext_Tv)
        private val experienceenddate_Tv1: TextView = itemView.findViewById(R.id.experienceenddate_Tv1)
        private val experinceresponsibilities1_Tv: TextView = itemView.findViewById(R.id.experinceresponsibilities1_Tv)
        private val expereinceacheivements1_Tv: TextView = itemView.findViewById(R.id.expereinceacheivements1_Tv)
        private val experinceskills_Tv: TextView = itemView.findViewById(R.id.experinceskills_Tv)
        private val experienceDocument: ImageView = itemView.findViewById(R.id.experienceDocument)
        private val experienceArrow_Tv: TextView = itemView.findViewById(R.id.experienceArrow_Tv)
        private val experienceArrow_Tv2: TextView = itemView.findViewById(R.id.experienceArrow_Tv2)
        private val experience1: RelativeLayout = itemView.findViewById(R.id.experience1)
        private val primarytext_Tv: TextView = itemView.findViewById(R.id.primarytext_Tv)

        private fun formatDate(date: Date?): String {
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            return if (date != null) {
                dateFormat.format(date)
            } else {
                "N/A"  // Return a default value when date is null
            }
        }

        fun bind(experience: Experience, position: Int) {
            val formattedStartDate = formatDate(experience.startDate)
            val formattedEndDate = formatDate(experience.endDate)

            experienceJob_Tv.text = experience.role ?: "N/A"
            experienceCompany_Tv.text = experience.companyName ?: "N/A"
            experienceStartdatetext_Tv.text = formattedStartDate
            experienceenddate_Tv1.text = formattedEndDate
            experinceresponsibilities1_Tv.text = experience.jobResponsibility ?: "N/A"
            expereinceacheivements1_Tv.text = experience.achievments ?: "N/A"
            experinceskills_Tv.text = experience.skills ?: "N/A"

            val imageUrl = experience.document
            if (!imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(imageUrl)
                    .into(experienceDocument)
            } else {
                experienceDocument.setImageResource(R.drawable.ic_profile)
            }

            experienceArrow_Tv2.setOnClickListener {
                experience1.visibility = View.VISIBLE
                experienceArrow_Tv.visibility = View.VISIBLE
                experienceArrow_Tv2.visibility = View.GONE
            }

            experienceArrow_Tv.setOnClickListener {
                experience1.visibility = View.GONE
                experienceArrow_Tv.visibility = View.GONE
                experienceArrow_Tv2.visibility = View.VISIBLE
            }

            primarytext_Tv.text = "Experience $position"
        }
    }
}
