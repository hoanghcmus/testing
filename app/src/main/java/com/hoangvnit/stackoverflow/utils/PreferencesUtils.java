package com.hoangvnit.stackoverflow.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;
import com.hoangvnit.stackoverflow.R;

public class PreferencesUtils {
    private static PreferencesUtils instance;

    private SharedPreferences prefs;

    public PreferencesUtils(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public static PreferencesUtils getInstance(Context context) {
        if (instance == null)
            instance = new PreferencesUtils(context);
        return instance;
    }

    public void setBooleanValue(String KEY, boolean value) {
        prefs.edit().putBoolean(KEY, value).apply();
    }

    public boolean getBooleanValue(String KEY, boolean defvalue) {
        return prefs.getBoolean(KEY, defvalue);
    }

    public void setStringValue(String KEY, String value) {
        prefs.edit().putString(KEY, value).apply();
    }

    public String getStringValue(String KEY, String defvalue) {
        return prefs.getString(KEY, defvalue);
    }

    public void set(String KEY, Object value) {
        prefs.edit().putString(KEY, new Gson().toJson(value)).apply();
    }

    public <T> T get(String KEY, Class<T> mModelClass) {
        Object object = null;
        try {
            object = new Gson().fromJson(prefs.getString(KEY, ""), mModelClass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Primitives.wrap(mModelClass).cast(object);
    }

    public void setIntValue(String KEY, int value) {
        prefs.edit().putInt(KEY, value).apply();
    }

    public int getIntValue(String KEY, int defValue) {
        return prefs.getInt(KEY, defValue);
    }

    public void setLongValue(String KEY, long value) {
        prefs.edit().putLong(KEY, value).apply();
    }

    public long getLongValue(String KEY, long defValue) {
        return prefs.getLong(KEY, defValue);
    }

    public void setFloatValue(String KEY, float value) {
        prefs.edit().putFloat(KEY, value).apply();
    }

    public float getFloatValue(String KEY, float defValue) {
        return prefs.getFloat(KEY, defValue);
    }

    public void removeValue(String KEY) {
        prefs.edit().remove(KEY).apply();
    }

    public double getDoubleValue(String KEY, double defValue) {
        return Double.parseDouble(getStringValue(KEY, String.valueOf(defValue)));
    }

    public void setDoubleValue(String KEY, double defValue) {
        setStringValue(KEY, String.valueOf(defValue));
    }

    public boolean contain(String KEY) {
        return prefs.contains(KEY);
    }
}
