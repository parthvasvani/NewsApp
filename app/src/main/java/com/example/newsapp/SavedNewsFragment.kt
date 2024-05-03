package com.example.newsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedNewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val view = inflater.inflate(R.layout.fragment_saved_news, container, false)

        // Initialize your RecyclerView and ensure it has the correct data
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvNewsItems1)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Optionally, clear data if you don't want to show anything initially
        recyclerView.adapter = EmptyAdapter() // An adapter that displays no data

        return view
    }
}