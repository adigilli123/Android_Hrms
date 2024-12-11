package com.example.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Interface.Education
import com.example.Interface.Experience
import com.example.hrms_xyug.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class EducationAdapter (private val eduaction: List<Education>) :
    RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edaucation, parent, false)
        return EducationViewHolder(view)
    }

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        val eduaction = eduaction[position]
        holder.bind(eduaction, position + 1)
    }

    override fun getItemCount(): Int = eduaction.size

    class EducationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val institutetext_Tv: TextView = itemView.findViewById(R.id.institutetext_Tv)
        private val gradetext_Tv: TextView = itemView.findViewById(R.id.gradetext_Tv)
        private val qualificationtext_Tv: TextView = itemView.findViewById(R.id.qualificationtext_Tv)
        private val coursetext_Tv: TextView = itemView.findViewById(R.id.coursetext_Tv)
        private val addyoptext_Tv: TextView = itemView.findViewById(R.id.addyoptext_Tv)
        private val educationimagetext_Tv: ImageView = itemView.findViewById(R.id.educationimagetext_Tv)
        private val experienceArrow_Tv: TextView = itemView.findViewById(R.id.experienceArrow_Tv)
        private val experienceArrow_Tv2: TextView = itemView.findViewById(R.id.experienceArrow_Tv2)
        private val educationdetyaiol: RelativeLayout = itemView.findViewById(R.id.educationdetyaiol)
        private  val eduationtext_Tv: TextView =itemView.findViewById(R.id.eduationtext_Tv)

        fun bind(education: Education, position: Int) {

            institutetext_Tv.text = education.institution ?: "N/A"
           // gradetext_Tv.text = education.grade ?: "N/A"
            qualificationtext_Tv.text = education.degree ?: "N/A"
            coursetext_Tv.text = education.specialization ?: "N/A"
            addyoptext_Tv.text = education.yearOfPassing ?: "N/A"

            val imageUrl = education.document
            if (!imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(imageUrl)
                    .into(educationimagetext_Tv)
            } else {
                educationimagetext_Tv.setImageResource(R.drawable.ic_profile)
            }

            experienceArrow_Tv2.setOnClickListener {
                educationdetyaiol.visibility = View.VISIBLE
                experienceArrow_Tv.visibility = View.VISIBLE
                experienceArrow_Tv2.visibility = View.GONE
            }

            experienceArrow_Tv.setOnClickListener {
                educationdetyaiol.visibility = View.GONE
                experienceArrow_Tv.visibility = View.GONE
                experienceArrow_Tv2.visibility = View.VISIBLE
            }

            eduationtext_Tv.text = "Education $position"
        }
    }
}