package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var dailyReminder: DailyReminder

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        // XTODO 10 : Update theme based on value in ListPreference

        val settingsActivity = activity as SettingsActivity
        dailyReminder = settingsActivity.getDailyReminder()

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

        // XTODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        val switchPreference = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        switchPreference?.setOnPreferenceChangeListener { preference, newValue ->
                if (newValue as Boolean) {
                    dailyReminder.setDailyReminder(requireContext().applicationContext)
                } else {
                    dailyReminder.cancelAlarm(requireContext().applicationContext)
                }
                true
            }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}