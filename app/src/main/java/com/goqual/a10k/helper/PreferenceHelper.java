package com.goqual.a10k.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by ladmusician on 4/6/16.
 */
public class PreferenceHelper {
    private final String PREF_NAME = "com.goqual.10k";
    private Context mContext;
    private static PreferenceHelper instance;

    public static PreferenceHelper getInstance(Context ctx) {
        if(instance == null) {
            instance = new PreferenceHelper(ctx);
        }
        return instance;
    }

    public PreferenceHelper(Context context) {
        mContext = context;
    }

    public void put(String key, Object val) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (val instanceof Boolean) {
            editor.putBoolean(key, (Boolean)val);
        } else if (val instanceof Integer) {
            editor.putInt(key, (int)val);
        } else if (val instanceof String) {
            editor.putString(key, (String)val);
        } else {
            editor.putString(key, (String)val);
        }

        editor.commit();
    }

    public void deleteAllValues() {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Map<String, ?> values = pref.getAll();
        for(String key : values.keySet()) {
            editor.remove(key);
        }
        editor.apply();
    }

    public int getValue(String key, int dftInt) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getInt(key, dftInt);
        }
        catch(Exception e) {
            return dftInt;
        }
    }

    public String getStringValue(String key, String dftInt) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getString(key, dftInt);
        }
        catch(Exception e) {
            return dftInt;
        }
    }

    public boolean getBooleanValue(String key) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        try {
            return pref.getBoolean(key, false);
        } catch (Exception e) {
            return false;
        }
    }
}
