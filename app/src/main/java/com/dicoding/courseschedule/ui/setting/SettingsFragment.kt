package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.dicoding.courseschedule.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        // XTODO 10 : Update theme based on value in ListPreference

        val listPreference = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        listPreference?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                val nightMode = when (newValue) {
                    getString(R.string.pref_dark_auto) -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    getString(R.string.pref_dark_on) -> AppCompatDelegate.MODE_NIGHT_YES
                    getString(R.string.pref_dark_off) -> AppCompatDelegate.MODE_NIGHT_NO
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                updateTheme(nightMode)
                true
            }

        // TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}