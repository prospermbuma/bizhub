package com.extrap.co.bizhub.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    
    private static final String PREF_NAME = "field_service_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_AUTO_SYNC = "auto_sync";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private static final String KEY_LOCATION_TRACKING = "location_tracking";
    private static final String KEY_LAST_SYNC_TIME = "last_sync_time";
    private static final String KEY_APP_VERSION = "app_version";
    private static final String KEY_FIRST_LAUNCH = "first_launch";
    
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    
    public PreferenceManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }
    
    // User preferences
    public void setUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }
    
    public int getUserId() {
        return preferences.getInt(KEY_USER_ID, -1);
    }
    
    public void setUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }
    
    public String getUserEmail() {
        return preferences.getString(KEY_USER_EMAIL, "");
    }
    
    public void setUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }
    
    public String getUserName() {
        return preferences.getString(KEY_USER_NAME, "");
    }
    
    public void setUserRole(String role) {
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }
    
    public String getUserRole() {
        return preferences.getString(KEY_USER_ROLE, "");
    }
    
    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }
    
    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    // App settings
    public void setAutoSync(boolean enabled) {
        editor.putBoolean(KEY_AUTO_SYNC, enabled);
        editor.apply();
    }
    
    public boolean isAutoSyncEnabled() {
        return preferences.getBoolean(KEY_AUTO_SYNC, true);
    }
    
    public void setNotificationsEnabled(boolean enabled) {
        editor.putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled);
        editor.apply();
    }
    
    public boolean areNotificationsEnabled() {
        return preferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }
    
    public void setLocationTracking(boolean enabled) {
        editor.putBoolean(KEY_LOCATION_TRACKING, enabled);
        editor.apply();
    }
    
    public boolean isLocationTrackingEnabled() {
        return preferences.getBoolean(KEY_LOCATION_TRACKING, true);
    }
    
    public void setLastSyncTime(long timestamp) {
        editor.putLong(KEY_LAST_SYNC_TIME, timestamp);
        editor.apply();
    }
    
    public long getLastSyncTime() {
        return preferences.getLong(KEY_LAST_SYNC_TIME, 0);
    }
    
    public void setAppVersion(String version) {
        editor.putString(KEY_APP_VERSION, version);
        editor.apply();
    }
    
    public String getAppVersion() {
        return preferences.getString(KEY_APP_VERSION, "");
    }
    
    public void setFirstLaunch(boolean isFirstLaunch) {
        editor.putBoolean(KEY_FIRST_LAUNCH, isFirstLaunch);
        editor.apply();
    }
    
    public boolean isFirstLaunch() {
        return preferences.getBoolean(KEY_FIRST_LAUNCH, true);
    }
    
    // Clear all preferences
    public void clearAll() {
        editor.clear();
        editor.apply();
    }
    
    // Clear user data only
    public void clearUserData() {
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_USER_ROLE);
        editor.remove(KEY_IS_LOGGED_IN);
        editor.apply();
    }
} 