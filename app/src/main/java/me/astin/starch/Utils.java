package me.astin.starch;

import android.content.Context;
import android.os.Environment;
import android.util.TypedValue;
import java.net.URLConnection;
import android.net.ConnectivityManager;
import java.util.Calendar;
import java.util.TimeZone;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff;


public class Utils {
    public static String getAbsolutePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static String getBasePath() {
        return Utils.getAbsolutePath() + "/Floating_Apps_DB/";
    }

    public static int dip2px(Context ctx, int dips) {
        return dips * (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, ctx.getResources().getDisplayMetrics());
    }

    public static Drawable ColorFilter(Drawable drawable, int color) {
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        return drawable;
    }

    public static int getNetworkInfo(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);
        boolean mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        int info = 0;

        if(mobile) info = 1;
        else if(wifi) info = 0;
        else info = -1;

        return info;
    }

    public static String getCurrentTime() {
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT+09:00"));
        int YEAR = now.get(Calendar.YEAR);
        int MONTH = now.get(Calendar.MONTH);
        int DATE = now.get(Calendar.DATE);
        String[] weeks = {"일", "월", "화", "수", "목", "금", "토"};
        String week = weeks[now.get(Calendar.DAY_OF_WEEK) - 1];
        int AM_PM = now.get(Calendar.AM_PM);
        int HOUR = now.get(Calendar.HOUR);
        int MINUTE = now.get(Calendar.MINUTE);
        int SECOND = now.get(Calendar.SECOND);

        return YEAR + "년 " + MONTH + "월 " + DATE + "일 " + week + "요일 "+ (AM_PM == 0? "오전 " : "오후 ") + HOUR + "시 " + MINUTE + "분 " + SECOND + "초";
    }

    public static void saveData(Context ctx, String prefName, String prefValue) {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor prefsEditor = sPrefs.edit();
        prefsEditor.putString(prefName, prefValue);
        prefsEditor.commit();
    }

    public static void saveData(Context ctx, String prefName, int prefValue) {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor prefsEditor = sPrefs.edit();
        prefsEditor.putInt(prefName, prefValue);
        prefsEditor.commit();
    }

    public static void saveData(Context ctx, String prefName, Boolean prefValue) {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor prefsEditor = sPrefs.edit();
        prefsEditor.putBoolean(prefName, prefValue);
        prefsEditor.commit();
    }

    public static String readString(Context ctx, String prefName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getString(prefName, "");
    }

    public static int readInt(Context ctx, String prefName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getInt(prefName, -1);
    }

    public static Boolean readBoolean(Context ctx, String prefName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        return prefs.getBoolean(prefName, false);
    }

    public static void removeData(Context ctx, String prefName) {
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor prefsEditor = sPrefs.edit();
        prefsEditor.remove(prefName);
        prefsEditor.commit();
    }
}
