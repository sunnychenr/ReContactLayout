package com.example.recontactlayout.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.recontactlayout.ApplicationEx;

/**
 * Created by ChenR on 2017/11/15.
 */

public class PreferencesManager {
    private static final String APP_PREF_SETTING_FILE = "recontact_layout_app_setting_pref_file";

    private static PreferencesManager instance;
    private static SharedPreferences mSettingPref;
    private static SharedPreferences.Editor mEditor;

    private PreferencesManager() {
        Context context = ApplicationEx.getInstance().getApplicationContext();
        mSettingPref = context.getSharedPreferences(APP_PREF_SETTING_FILE, Context.MODE_PRIVATE);
        mEditor = mSettingPref.edit();
    }

    public static PreferencesManager getInstance() {
        if (instance == null) {
            synchronized (PreferencesManager.class){
                if (instance == null) {
                    instance = new PreferencesManager();
                }
            }
        }
        return instance;
    }

    private static final String PREF_KEY_CONTACTS_SHOW_LAYOUT_TYPE = "contacts_show_layout_type";
    public void setContactsLayoutType (boolean isListLayout) {
        mEditor.putBoolean(PREF_KEY_CONTACTS_SHOW_LAYOUT_TYPE, isListLayout).apply();
    }
    public boolean getContactsLayoutType() {
        return mSettingPref.getBoolean(PREF_KEY_CONTACTS_SHOW_LAYOUT_TYPE, false);
    }

}
