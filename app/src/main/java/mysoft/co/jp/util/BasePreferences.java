package mysoft.co.jp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class BasePreferences {
    private static final String MYSOFT_PREFERENCES = "mysoft.co.jp.util.preferences";

    protected static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    protected static int getInt(Context context, String key) {
        return getSharedPreferences(context).getInt(key, 0);
    }

    protected static int getIntDefault(Context context, String key) {
        return getSharedPreferences(context).getInt(key, -1);
    }

    protected static boolean getBoolean(Context context, String key) {
        return getSharedPreferences(context).getBoolean(key, false);
    }

    protected static void setString(Context context, String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).apply();
    }

    protected static void setInt(Context context, String key, int value) {
        getSharedPreferences(context).edit().putInt(key, value).apply();
    }

    protected static void setBoolean(Context context, String key, boolean value) {
        getSharedPreferences(context).edit().putBoolean(key, value).apply();
    }

    protected static void setLong(Context context, String key, long value) {
        getSharedPreferences(context).edit().putLong(key, value).apply();
    }

    protected static long getLong(Context context, String key) {
        return getSharedPreferences(context).getLong(key, 0);
    }

    protected static void setFloat(Context context, String key, float value) {
        getSharedPreferences(context).edit().putFloat(key, value).apply();
    }

    protected static float getFloat(Context context, String key) {
        return getSharedPreferences(context).getFloat(key, 0);
    }

    protected static void clearKey(Context context, String key) {
        getSharedPreferences(context).edit().remove(key).apply();
    }

    protected static void clearAll(Context context) {
        getSharedPreferences(context).edit().clear();
    }

    protected static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(MYSOFT_PREFERENCES, Context.MODE_PRIVATE);
    }
}
