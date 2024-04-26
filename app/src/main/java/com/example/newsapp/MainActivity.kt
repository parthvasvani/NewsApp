package com.example.newsapp

import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.ActivityMainBinding
import java.io.IOException

const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInterface.api.getNewsItems("Apple", "b221fec9d9584495b715d79e382fa713")

            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }

            if (response.isSuccessful && response.body() != null) {
                // Get the list of articles from the response body
                val articles = response.body()!!.articles

                // Clear existing data in originalArticles
                newsAdapter.originalArticles.clear()

                // Add new data to originalArticles and set it in news
                newsAdapter.originalArticles.addAll(articles) // add data to the original list
                newsAdapter.news = articles // update the visible list

                binding.progressBar.isVisible = false // hide the progress bar
            } else {
                Log.e(TAG, "Response not successful")
                binding.progressBar.isVisible = false
            }

            /*if (response.isSuccessful && response.body() != null) { old version
                newsAdapter.news = response.body()!!.articles
            } else {
                Log.e(TAG, "Response not successful")
            }
            binding.progressBar.isVisible = false*/
        }


        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newsAdapter.filter(s.toString()) // Update the adapter with the new query
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun setupRecyclerView() = binding.rvNewsItems.apply {
        newsAdapter = NewsAdapter()
        adapter = newsAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }

}