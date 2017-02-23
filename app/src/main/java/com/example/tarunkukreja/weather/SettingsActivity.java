package com.example.tarunkukreja.weather;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by tarunkukreja on 02/01/17.
 */

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add 'general' preferences, defined in the XML file
              // TODO: Add preferences from XML
        addPreferencesFromResource(R.xml.pref_general);


                               // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
                                       // updated when the preference changes.
                                               // TODO: Add preference
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_location))); // preferences are found by their keys
        bindPreferenceSummaryToValue(findPreference(getString(R.string.listPref_key)));
       // bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_enable_notifications_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference){

        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                                         .getString(preference.getKey(), "")) ;




    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        ListPreference listPreference ;
       // CheckBoxPreference checkBoxPreference ;

        String preferenceValue = newValue.toString() ;

        if(preference instanceof ListPreference){

            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values)

            listPreference = (ListPreference)preference ;
            int prefIndex = listPreference.findIndexOfValue(preferenceValue) ;

            if(prefIndex >= 0){
                preference.setSummary(listPreference.getEntries()[prefIndex]); // entries are the strings which are displayed
                // summary is the string which is
                // visible below the title
            }

        }
//        else if(preference instanceof CheckBoxPreference){
//
//            checkBoxPreference = (CheckBoxPreference)preference ;
//            if(checkBoxPreference.isChecked()){
//                preference.setSummary(checkBoxPreference.getSummaryOn());
//            }
//            else {
//                preference.setSummary(checkBoxPreference.getSummaryOff());
//            }
//        }
        else
        {  //For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(preferenceValue);
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
       @Override
        public Intent getParentActivityIntent() {
                return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }


}
