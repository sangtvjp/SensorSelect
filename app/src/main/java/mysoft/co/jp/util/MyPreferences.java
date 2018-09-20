package mysoft.co.jp.util;

import android.content.Context;

public class MyPreferences extends BasePreferences {
    private static final String KEY_SENSOR_SETTING_ACCEL = "key_sensor_setting_accel";
    private static final String KEY_SENSOR_SETTING_GRYO = "key_sensor_setting_gryo";
    private static final String KEY_SENSOR_SETTING_PRESSURE = "key_sensor_setting_pressure";
    private static final String KEY_LED_SETTING_CH1 = "key_led_setting_ch1";
    private static final String KEY_LED_SETTING_CH2 = "key_led_setting_ch2";
    private static final String KEY_LED_SETTING_CH3 = "key_led_setting_ch3";
    private static final String KEY_LED_SETTING_CH4 = "key_led_setting_ch4";


    // getSensorSettingAccel
    public static boolean getSensorSettingAccel(Context context) {
        return getBoolean(context, KEY_SENSOR_SETTING_ACCEL);
    }

    public static void setSensorSettingAccel(Context context, boolean isAccel) {
        setBoolean(context, KEY_SENSOR_SETTING_ACCEL, isAccel);
    }

    // getSensorSettingGryo
    public static boolean getSensorSettingGryo(Context context) {
        return getBoolean(context, KEY_SENSOR_SETTING_GRYO);
    }

    public static void setSensorSettingGryo(Context context, boolean gryo) {
        setBoolean(context, KEY_SENSOR_SETTING_GRYO, gryo);
    }

    // getSensorSettingPressure
    public static boolean getSensorSettingPressure(Context context) {
        return getBoolean(context, KEY_SENSOR_SETTING_PRESSURE);
    }

    public static void setSensorSettingPressure(Context context, boolean pressure) {
        setBoolean(context, KEY_SENSOR_SETTING_PRESSURE, pressure);
    }

    // getLedSettingCh1
    public static int getLedSettingCh1(Context context) {
        return getInt(context, KEY_LED_SETTING_CH1);
    }

    public static void setLedSettingCh1(Context context, int value) {
        setInt(context, KEY_LED_SETTING_CH1, value);
    }

    // getLedSettingCh2

    public static int getLedSettingCh2(Context context) {
        return getInt(context, KEY_LED_SETTING_CH2);
    }

    public static void setLedSettingCh2(Context context, int value) {
        setInt(context, KEY_LED_SETTING_CH2, value);
    }

    // getLedSettingCh1

    public static int getLedSettingCh3(Context context) {
        return getInt(context, KEY_LED_SETTING_CH3);
    }

    public static void setLedSettingCh3(Context context, int value) {
        setInt(context, KEY_LED_SETTING_CH3, value);
    }

    // getLedSettingCh4
    public static int getLedSettingCh4(Context context) {
        return getInt(context, KEY_LED_SETTING_CH4);
    }

    public static void setLedSettingCh4(Context context, int value) {
        setInt(context, KEY_LED_SETTING_CH4, value);
    }

}
