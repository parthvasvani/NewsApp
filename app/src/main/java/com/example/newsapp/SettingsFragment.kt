package com.example.newsapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import java.util.Locale

private const val SELECTED_LANGUAGE = "selected_language"
class SettingsFragment: Fragment() {

    private lateinit var themeRadioGroup: RadioGroup
    private lateinit var lightThemeRadioButton: RadioButton
    private lateinit var darkThemeRadioButton: RadioButton
    private lateinit var languageSpinner: Spinner


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        themeRadioGroup = view.findViewById(R.id.rgTheme)
        lightThemeRadioButton = view.findViewById(R.id.rbLight)
        darkThemeRadioButton = view.findViewById(R.id.rbDark)
        languageSpinner = view.findViewById(R.id.spLanguage)

        // Handle theme selection
        themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbLight -> setTheme(AppTheme.LIGHT)
                R.id.rbDark -> setTheme(AppTheme.DARK)
            }
        }

        // Populate language spinner
        val languages = arrayOf("English", "Spanish", "French", "German")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        // Handle language selection
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedLanguage = languages[position]
                setLanguage(selectedLanguage)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun setTheme(theme: AppTheme) {
        // Implement theme change logic here

        val mode = when (theme) {
            AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun setLanguage(language: String) {
        // Implement language change logic here
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context?.resources?.updateConfiguration(config, requireContext().resources.displayMetrics)

        // Save selected language for next app launch
        val preferences = context?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        preferences?.edit()?.putString(SELECTED_LANGUAGE, language)?.apply()
    }

    enum class AppTheme {
        LIGHT,
        DARK
    }
}