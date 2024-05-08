package com.example.newsapp

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

class SavedArticleAdapter: RecyclerView.Adapter<SavedArticleAdapter.SavedArticleViewHolder>() {

    inner class SavedArticleViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: SavedArticle) {
            binding.apply {
                tvTitle.text = article.title
                tvSource.text = article.author
                val dateString = article.publishDate // e.g., "2023-10-01T15:30:00Z"

                val relativeTime = try {
                    // Define the input format
                    val inputFormat =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
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

                val imageUrl = article.imageUrl // assuming Article has an imageUrl field
                Glide.with(ivImage.context) // specify the context
                    .load(imageUrl)          // load the URL
                    .placeholder(R.drawable.placeholder) // placeholder while loading
                    .error(R.drawable.error) // error image if there's a problem
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(ivImage)
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<SavedArticle>() {
        override fun areItemsTheSame(oldItem: SavedArticle, newItem: SavedArticle): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SavedArticle, newItem: SavedArticle): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)
    override fun getItemCount() = news.size

    var news: List<SavedArticle>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedArticleAdapter.SavedArticleViewHolder {
        return SavedArticleViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: SavedArticleAdapter.SavedArticleViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position])
    }
}
