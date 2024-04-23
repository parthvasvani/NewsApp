package com.example.newsapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the current system time once to use for all items
        val currentTime = System.currentTimeMillis()

        // Data for RecyclerView, all with the same timestamp
        val items = mutableListOf(
            ListItem("News Title1", "Source", currentTime),
            ListItem("News Title2", "Source", currentTime + 60000),
            ListItem("News Title3", "Source", currentTime + 120000),
            ListItem("News Title4", "Source", currentTime + 180000)
        )
        //val adapter = SearchableNewsItemAdapter(items)
        val adapter = MyRecyclerViewAdapter(items, this)
        val recyclerView: RecyclerView = findViewById(R.id.itemRecyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val searchEditText = findViewById<TextInputEditText>(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

    }

}