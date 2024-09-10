package com.example.healthapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SymptomAdapter(private val symptoms: List<Symptom>) : RecyclerView.Adapter<SymptomAdapter.SymptomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_symptom, parent, false)
        return SymptomViewHolder(view)
    }

    override fun onBindViewHolder(holder: SymptomViewHolder, position: Int) {
        holder.bind(symptoms[position])
    }

    override fun getItemCount(): Int = symptoms.size

    inner class SymptomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val symptomName: TextView = itemView.findViewById(R.id.symptom_name)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rating_bar)

        fun bind(symptom: Symptom) {
            symptomName.text = symptom.name
            ratingBar.rating = symptom.rating.toFloat()

            ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
                symptom.rating = rating.toInt()
            }
        }
    }
}