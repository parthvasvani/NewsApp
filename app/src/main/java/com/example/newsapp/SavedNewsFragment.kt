package com.example.newsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedNewsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedArticleAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_saved_news, container, false)

        recyclerView = view.findViewById(R.id.rvNewsItems1)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = SavedArticleAdapter()  // Start with an empty list
        recyclerView.adapter = adapter

        // Load saved articles
        loadSavedArticles()

        return view
    }

    private fun loadSavedArticles() {
        lifecycleScope.launch {
            val db = NewsAppDatabase.getDatabase(requireContext())
            val savedArticles = withContext(Dispatchers.Main) {
                db.savedArticleDao().getSavedArticles()
            }
                adapter.news = savedArticles
                adapter.notifyDataSetChanged()
        }
    }
}
