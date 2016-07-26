package com.honeywell.sweetspot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

//
// import android.support.v7.app.AppCompatActivity;

public class SweetSpotActivity extends Activity {

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(SweetSpotActivity.this, FloatingBubbleService.class);
        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_sweetspot);

        // Set the activity's fragment :
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent = new Intent(SweetSpotActivity.this, FloatingBubbleService.class);
//        startService(intent);
//        moveTaskToBack(true);
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        private SeekBarPreference _seekBarPref;

        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.activity_settings);


            // Get widgets :
            _seekBarPref = (SeekBarPreference) this.findPreference("SEEKBAR_VALUE");

            // Set listener :
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

            // Set seekbar summary :
            int radius = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getInt("SEEKBAR_VALUE", 50);
            _seekBarPref.setSummary(this.getString(R.string.settings_summary).replace("$1", ""+radius));
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            // Set seekbar summary :
            int radius = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getInt("SEEKBAR_VALUE", 50);
            _seekBarPref.setSummary(this.getString(R.string.settings_summary).replace("$1", ""+radius));
        }
    }
}
