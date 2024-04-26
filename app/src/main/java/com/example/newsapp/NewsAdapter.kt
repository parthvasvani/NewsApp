package com.example.newsapp

import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.newsapp.databinding.ListItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    val originalArticles = mutableListOf<Article>()
    inner class NewsViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(article: Article){
           binding.apply {
                tvTitle.text = article.title
                tvSource.text = article.source.toString()
                //tvTimeStamp.text = article.publishedAt


               root.setOnClickListener {
                   val context = it.context
                   val intent = Intent(context, DetailNewsScreenActivity::class.java)

                   // Pass the article data to the new activity
                   intent.putExtra("EXTRA_IMAGE", article.urlToImage)
                   intent.putExtra("EXTRA_TITLE", article.title)
                   intent.putExtra("EXTRA_DESCRIPTION", article.description)
                   intent.putExtra("EXTRA_AUTHOR",article.author)
                   intent.putExtra("EXTRA_DATE",article.publishedAt)
                   // Start the new activity
                   context.startActivity(intent)
               }


               // Parse the publishedAt string into a Date object
               val dateString = article.publishedAt // e.g., "2023-10-01T15:30:00Z"

               val relativeTime = try {
                   // Define the input format
                   val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                       timeZone = TimeZone.getTimeZone("UTC")
                   }
                   val date = inputFormat.parse(dateString)

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

               tvTimeStamp.text = relativeTime

               /*val formattedDate = try {      version 1 for date and time
                   // Define the input format from the data source
                   val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                       timeZone = TimeZone.getTimeZone("UTC")
                   }
                   val date = inputFormat.parse(dateString)

                   // Define the output format for display
                   val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US)

                   // Format the date object into the desired string representation
                   outputFormat.format(date)
               } catch (e: ParseException) {
                   // Handle parsing exceptions
                   "Invalid date"
               }

               tvTimeStamp.text = formattedDate*/

               val imageUrl = article.urlToImage // assuming Article has an imageUrl field
               Glide.with(ivImage.context) // specify the context
                   .load(imageUrl)          // load the URL
                   .placeholder(R.drawable.placeholder) // placeholder while loading
                   .error(R.drawable.error) // error image if there's a problem
                   .apply(RequestOptions.bitmapTransform(CircleCrop()))
                   .into(ivImage)
           }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    override fun getItemCount() = news.size

    var news: List<Article>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


    fun filter(query: String) {
        if (query.isEmpty()) {
            differ.submitList(originalArticles.toList()) // Reset to original data
        } else {
            val filteredList = originalArticles.filter { article ->
                article.title.contains(query, ignoreCase = true) ||
                    article.description?.contains(query, ignoreCase = true) ?: false
            }
            differ.submitList(filteredList) // Update the list with filtered data
        }
    }
}