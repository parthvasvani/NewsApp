package com.example.newsapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailNewsScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailnewsscreen)

        val title = intent.getStringExtra("title")
        val timestamp = intent.getLongExtra("timestamp",0L)

        val titleTextView: TextView = findViewById(R.id.tvTitle1)
        val dateTextView = findViewById<TextView>(R.id.tvTime1)

        // Convert the timestamp to a Date object
        val date = Date(timestamp)

        // Format the date as a readable string
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(date)


        titleTextView.text = title
        dateTextView.text = formattedDate



    }

}