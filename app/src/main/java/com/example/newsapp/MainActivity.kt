package com.example.newsapp

import android.content.Intent
import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var currentQuery: String

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*setupRecyclerView()
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

        val tvTitle = findViewById<TextView?>(R.id.tvTitle)
        tvTitle?.setOnClickListener {
            val intent = Intent(this, DetailNewsScreenActivity::class.java)
            startActivity(intent)
        } ?: run {
            Log.d("MainActivity", "tvTitle is null. Please check the layout or initialization.")
        }
    }

    private fun setupRecyclerView() = binding.rvNewsItems.apply {
        newsAdapter = NewsAdapter()
        adapter = newsAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
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
        }*/


        val newsFragment = NewsFragment()
        val savedFragment = SavedNewsFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(newsFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {item ->
            when (item.itemId) {
                R.id.i_news -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, newsFragment)
                        .commit()
                    true
                }
                R.id.i_save ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, savedFragment)
                        .commit()
                    true
                }
                R.id.i_settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, settingsFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
    private fun setCurrentFragment(fragment: androidx.fragment.app.Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
    }

