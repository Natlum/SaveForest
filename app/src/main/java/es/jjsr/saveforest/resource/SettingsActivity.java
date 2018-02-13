package es.jjsr.saveforest.resource;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Locale;

import es.jjsr.saveforest.R;
import es.jjsr.saveforest.StartActivity;

public class SettingsActivity extends PreferenceActivity {

    private CheckBoxPreference checkboxPrefAdmin;
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(toolbar, 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkboxPrefAdmin = (CheckBoxPreference) getPreferenceManager().findPreference("pref_admin");

        checkboxPrefAdmin.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("Preferences", "Pref " + preference.getKey() + " changed to " + newValue.toString());
                return true;
            }
        });

        Preference langPreference = getPreferenceScreen().findPreference("pref_language");
        langPreference.setOnPreferenceChangeListener(languageChangeListener);
    }

    Preference.OnPreferenceChangeListener languageChangeListener = new Preference.OnPreferenceChangeListener(){

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch (newValue.toString()){
                case "en":
                    setLocale("en");
                    //Toast.makeText(getApplicationContext(), "Locale in English!", Toast.LENGTH_LONG).show();
                    break;
                case "es":
                    setLocale("es");
                    //Toast.makeText(getApplicationContext(), "Locale en Español!", Toast.LENGTH_LONG).show();
                    break;
            }
            return true;
        }
    };

    //* manually changing current locale/
    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, StartActivity.class);
        finish();
        startActivity(refresh);
    }

}
