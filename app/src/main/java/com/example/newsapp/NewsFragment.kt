package com.example.newsapp

import android.content.Intent
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresExtension
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.FragmentNewsBinding
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsFragment : Fragment() {

        private var _binding: FragmentNewsBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentNewsBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


        private lateinit var newsAdapter: NewsAdapter
        private lateinit var currentQuery: String

        @Deprecated("Deprecated in Java")
        @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            // Set up RecyclerView, adapters, and other logic from MainActivity here

            setupRecyclerView()
            currentQuery = "Apple"
            updateQuery(currentQuery)

            binding.searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    updateQuery(s.toString())
                    //newsAdapter.filter(s.toString()) // Update the adapter with the new query
                }

                override fun afterTextChanged(s: Editable?) {}
            })


            val tvTitle = view?.findViewById<TextView?>(R.id.tvTitle)
            tvTitle?.setOnClickListener {
                val intent = Intent(requireContext(), DetailNewsScreenActivity::class.java)
                startActivity(intent)
            } ?: run {
                Log.d("MainActivity", "tvTitle is null. Please check the layout or initialization.")
            }
        }

    private fun setupRecyclerView() = binding.rvNewsItems.apply {
        newsAdapter = NewsAdapter()
        adapter = newsAdapter
        layoutManager = LinearLayoutManager(context)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun updateQuery(newQuery: String) {
        currentQuery = newQuery
        fetchNewsData() // Fetch news data with the new query
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun fetchNewsData() {
        lifecycleScope.launch {
            binding.progressBar.isVisible = true

            val response: Response<NewsApiResponse> = try {
                RetrofitInterface.api.getNewsItems(currentQuery, "b221fec9d9584495b715d79e382fa713")
            } catch (e: IOException) {
                Log.e(TAG, "IOException, check your internet connection")
                binding.progressBar.isVisible = false
                return@launch
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                binding.progressBar.isVisible = false
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                newsAdapter.originalArticles.clear()
                newsAdapter.originalArticles.addAll(response.body()!!.articles)
                newsAdapter.news = response.body()!!.articles
            } else {
                Log.e(TAG, "Response not successful")
            }

            binding.progressBar.isVisible = false
        }

        }
    }