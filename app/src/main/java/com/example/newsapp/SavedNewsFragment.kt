package com.example.newsapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class SavedNewsFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedArticleAdapter
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val view = inflater.inflate(R.layout.fragment_saved_news, container, false)

        recyclerView = view.findViewById(R.id.rvNewsItems1)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val onItemClick: (SavedArticle) -> Unit = { article ->
            val intent = Intent(requireContext(), DetailNewsScreenActivity::class.java).apply {
                putExtra("EXTRA_TITLE", article.title)
                putExtra("EXTRA_DESCRIPTION", article.description)
                putExtra("EXTRA_IMAGE", article.imageUrl)
                putExtra("EXTRA_AUTHOR", article.author)
                putExtra("EXTRA_DATE", article.publishDate)
            }
            startActivity(intent)
        }

        adapter = SavedArticleAdapter(onItemClick)  // Start with an empty list
        recyclerView.adapter = adapter

        // Load saved articles
        loadSavedArticles()
        applySavedLanguage()

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

    private fun applySavedLanguage() {
        val languageCode = sharedPreferences.getString("language", "en") ?: "en"
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        requireContext().createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "language") {
            applySavedLanguage()
            requireActivity().recreate() // Recreate the activity to apply the language change
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}
