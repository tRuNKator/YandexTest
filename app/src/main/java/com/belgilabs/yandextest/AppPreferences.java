package com.belgilabs.yandextest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String PREF_FILE_NAME  = "preferences";
    private static AppPreferences sPrefs;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private AppPreferences(Context context) {
        pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static AppPreferences initialize(Context context) {
        if (sPrefs == null) {
            sPrefs = new AppPreferences(context.getApplicationContext());
        }
        return sPrefs;
    }

    public static AppPreferences getInstance() {
        if (sPrefs != null) {
            return sPrefs;
        }

        throw new IllegalArgumentException("Call initialize(context) first");
    }

    public String getString(String key) {
        return pref.getString(key, null);
    }

    public void put(String key, String value) {
        doEdit();
        editor.putString(key, value);
        doCommit();
    }

    public void remove(String... keys) {
        doEdit();
        for (String key : keys) {
            editor.remove(key);
        }
        doCommit();
    }

    public void clear() {
        doEdit();
        editor.clear();
        doCommit();
    }

    @SuppressLint("CommitPrefEdits")
    private void doEdit() {
        editor = pref.edit();
    }

    private void doCommit() {
        if (editor != null) {
            editor.commit();
            editor = null;
        }
    }

}
