package com.example.newsapp

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
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

class SettingsFragment: Fragment() {

    private lateinit var themeRadioGroup: RadioGroup
    private lateinit var lightThemeRadioButton: RadioButton
    private lateinit var darkThemeRadioButton: RadioButton
    private lateinit var languageSpinner: Spinner
    private lateinit var sharedPreferences: SharedPreferences

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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        setupLanguageSelection()
        setupThemeSelection()
    }

    private fun setupLanguageSelection() {
        val languages = listOf(
            "Arabic", "German", "English", "Spanish", "French", "Hebrew",
            "Italian", "Dutch", "Norwegian", "Portuguese", "Russian",
            "Swedish", "Urdu", "Chinese"
        )
        val languageCodes = listOf(
            "ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt",
            "ru", "sv", "ud", "zh"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        // Set current language selection
        val currentLanguageCode = sharedPreferences.getString("language", "en") ?: "en"
        val currentIndex = languageCodes.indexOf(currentLanguageCode)
        if (currentIndex >= 0) {
            languageSpinner.setSelection(currentIndex)
        }

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguageCode = languageCodes[position]
                if (selectedLanguageCode != currentLanguageCode) {
                    saveLanguagePreference(selectedLanguageCode)
                    setLocale(selectedLanguageCode)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun saveLanguagePreference(languageCode: String) {
        sharedPreferences.edit().putString("language", languageCode).apply()
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)

        // Restart the activity to apply the new language
        requireActivity().recreate()
    }

    private fun setupThemeSelection() {
        // Set current theme selection
        val currentThemeMode = AppCompatDelegate.getDefaultNightMode()
        if (currentThemeMode == AppCompatDelegate.MODE_NIGHT_YES) {
            darkThemeRadioButton.isChecked = true
        } else {
            lightThemeRadioButton.isChecked = true
        }

        // Handle theme selection
        themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbLight -> setTheme(AppTheme.LIGHT)
                R.id.rbDark -> setTheme(AppTheme.DARK)
            }
        }
    }

    private fun setTheme(theme: AppTheme) {
        val mode = when (theme) {
            AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    enum class AppTheme {
        LIGHT,
        DARK
    }
}