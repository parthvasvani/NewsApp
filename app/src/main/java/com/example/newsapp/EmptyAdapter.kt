package com.example.newsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmptyAdapter: RecyclerView.Adapter<EmptyAdapter.EmptyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmptyViewHolder {
        // Inflate a simple empty view (can be a basic TextView or other minimal layout)
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return EmptyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) {
        // Optionally, set some text or other indication that the RecyclerView is empty
        (holder.itemView as TextView).text = "No items"
    }

    override fun getItemCount(): Int {
        return 0 // Return 0 to indicate that there's no data
    }

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}