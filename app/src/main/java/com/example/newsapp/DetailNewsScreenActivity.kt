package com.example.newsapp

import android.os.Bundle
import android.text.format.DateUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DetailNewsScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailnewsscreen)

        val title = intent.getStringExtra("EXTRA_TITLE")
        val description = intent.getStringExtra("EXTRA_DESCRIPTION")
        val imageUrl = intent.getStringExtra("EXTRA_IMAGE")
        val author = intent.getStringExtra("EXTRA_AUTHOR")
        val publish = intent.getStringExtra("EXTRA_DATE")

        val tvTitle: TextView = findViewById(R.id.tvTitle1)
        val tvDescription: TextView = findViewById(R.id.tvDescription)
        val ivItem: ImageView = findViewById<ImageView>(R.id.ivItem)
        val tvAuthor: TextView = findViewById(R.id.tvAuthor)
        val tvTime1: TextView = findViewById(R.id.tvTime1)

        tvTitle.text = title
        tvDescription.text = description

        if (imageUrl != null) {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // default image during loading
                .error(R.drawable.error) // default image if there's an error
                .into(ivItem)
        } else {
            ivItem.setImageResource(R.drawable.placeholder) // Default image
        }

        tvAuthor.text = author
        //tvTime1.text = publish

        val relativeTime = try {
            // Define the input format
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val date = inputFormat.parse(publish)

            // Get the current time in milliseconds
            val currentTime = System.currentTimeMillis()

            // Get the date's time in milliseconds
            val dateInMillis = date?.time ?: currentTime

            // Get a human-readable relative time span string
            DateUtils.getRelativeTimeSpanString(
                dateInMillis,
                currentTime,
                DateUtils.MINUTE_IN_MILLIS
            ).toString()

        } catch (e: ParseException) {
            // In case of parsing failure, return a default message
            "Invalid date"
        }
        tvTime1.text = relativeTime

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            val savedArticle = SavedArticle(
                id = "${title}_${author}",  // Unique ID for the article
                title = title ?: "",
                description = description ?: "",
                imageUrl = imageUrl,
                author = author,
                publishDate = publish
            )
            lifecycleScope.launch {
                val db = NewsAppDatabase.getDatabase(applicationContext)
                db.savedArticleDao().saveArticle(savedArticle)
            }
        }
    }

}