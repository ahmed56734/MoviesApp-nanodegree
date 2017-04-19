package com.example.ahmed.moviesapp_nanodegree;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by ahmed on 4/18/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.fragment_settings);

        setPreferenceSummary(getString(R.string.pref_movies_sort_key));
    }

    private void setPreferenceSummary(String prefKey){

        Preference preference = findPreference(prefKey);

        if(preference instanceof ListPreference){
            preference.setSummary(((ListPreference) preference).getEntry());
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setPreferenceSummary(key);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
