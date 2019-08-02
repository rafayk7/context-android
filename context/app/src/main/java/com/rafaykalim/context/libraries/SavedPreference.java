package com.rafaykalim.context.libraries;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SavedPreference {
    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void addToSavedWebpages(Context context, Integer howMany)
    {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        Integer currAmt = getPreferences(context).getInt(PreferencesUtility.NUM_SAVED_WEBPAGES, 0);
        editor.putInt(PreferencesUtility.NUM_SAVED_WEBPAGES, currAmt + howMany);
        editor.apply();
    }

    public static void addToSavedWebpages(Context context)
    {
        addToSavedWebpages(context, 1);
    }

    private class PreferencesUtility {
        // Values for Shared Prefrences
        public static final String NUM_SAVED_WEBPAGES = "number_of_webpages";
        public static final String SAVED_WEBPAGE_PREFIX = "webpage_file_prefix";
    }
}
