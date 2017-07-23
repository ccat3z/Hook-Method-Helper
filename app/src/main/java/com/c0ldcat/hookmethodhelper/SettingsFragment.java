package com.c0ldcat.hookmethodhelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.io.File;
import java.util.Map;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPrefReadable();

        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = getPreferenceManager().getSharedPreferences();

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        Map<String, ?> preferences = sharedPreferences.getAll();

        for (String key : preferences.keySet()) {
            Preference preference = findPreference(key);

            if (preference instanceof EditTextPreference) {
                updateSummary((EditTextPreference) preference);
            }
        }
    }

    @Override
    public void onPause() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
        setPrefReadable();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);

        if (preference instanceof EditTextPreference) {
            updateSummary((EditTextPreference) preference);
        }
    }

    private void updateSummary(EditTextPreference preference) {
        preference.setSummary(preference.getText());
    }

    private void setPrefReadable() {
        new File("/data/data/" + Common.PACKAGE_NAME + "/shared_prefs/" + Common.PACKAGE_NAME + "_preferences.xml").setReadable(true, false);
    }
}
