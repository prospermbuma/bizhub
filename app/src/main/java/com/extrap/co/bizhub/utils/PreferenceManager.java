package com.extrap.co.bizhub.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    
    private static final String PREF_NAME = "BizHubPrefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private static final String KEY_AUTO_SYNC = "auto_sync";
    private static final String KEY_LOCATION_TRACKING = "location_tracking";
    private static final String KEY_LAST_SYNC_TIME = "last_sync_time";
    private static final String KEY_APP_VERSION = "app_version";
    private static final String KEY_FIRST_LAUNCH = "first_launch";
    
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    
    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    
    // Session Management
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    public void setLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }
    
    public long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, -1);
    }
    
    public void setUserId(long userId) {
        editor.putLong(KEY_USER_ID, userId);
        editor.apply();
    }
    
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }
    
    public void setUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }
    
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }
    
    public void setUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }
    
    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, "");
    }
    
    public void setUserRole(String role) {
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }
    
    // Settings
    public boolean areNotificationsEnabled() {
        return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }
    
    public void setNotificationsEnabled(boolean enabled) {
        editor.putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled);
        editor.apply();
    }
    
    public boolean isAutoSyncEnabled() {
        return sharedPreferences.getBoolean(KEY_AUTO_SYNC, true);
    }
    
    public void setAutoSync(boolean enabled) {
        editor.putBoolean(KEY_AUTO_SYNC, enabled);
        editor.apply();
    }
    
    public boolean isLocationTrackingEnabled() {
        return sharedPreferences.getBoolean(KEY_LOCATION_TRACKING, true);
    }
    
    public void setLocationTracking(boolean enabled) {
        editor.putBoolean(KEY_LOCATION_TRACKING, enabled);
        editor.apply();
    }
    
    // Clear session
    public void clearUserSession() {
        editor.remove(KEY_IS_LOGGED_IN);
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USER_EMAIL);
        editor.remove(KEY_USER_NAME);
        editor.remove(KEY_USER_ROLE);
        editor.apply();
    }
    
    // App settings
    public void setLastSyncTime(long timestamp) {
        editor.putLong(KEY_LAST_SYNC_TIME, timestamp);
        editor.apply();
    }
    
    public long getLastSyncTime() {
        return sharedPreferences.getLong(KEY_LAST_SYNC_TIME, 0);
    }
    
    public void setAppVersion(String version) {
        editor.putString(KEY_APP_VERSION, version);
        editor.apply();
    }
    
    public String getAppVersion() {
        return sharedPreferences.getString(KEY_APP_VERSION, "");
    }
    
    public void setFirstLaunch(boolean isFirstLaunch) {
        editor.putBoolean(KEY_FIRST_LAUNCH, isFirstLaunch);
        editor.apply();
    }
    
    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(KEY_FIRST_LAUNCH, true);
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

    // Settings Methods
    public void setNotificationsEnabled(boolean enabled) {
        editor.putBoolean("notifications_enabled", enabled);
        editor.apply();
    }

    public boolean areNotificationsEnabled() {
        return sharedPreferences.getBoolean("notifications_enabled", true);
    }

    public void setEmailNotificationsEnabled(boolean enabled) {
        editor.putBoolean("email_notifications_enabled", enabled);
        editor.apply();
    }

    public boolean areEmailNotificationsEnabled() {
        return sharedPreferences.getBoolean("email_notifications_enabled", true);
    }

    public void setSmsNotificationsEnabled(boolean enabled) {
        editor.putBoolean("sms_notifications_enabled", enabled);
        editor.apply();
    }

    public boolean areSmsNotificationsEnabled() {
        return sharedPreferences.getBoolean("sms_notifications_enabled", false);
    }

    public void setSoundNotificationsEnabled(boolean enabled) {
        editor.putBoolean("sound_notifications_enabled", enabled);
        editor.apply();
    }

    public boolean areSoundNotificationsEnabled() {
        return sharedPreferences.getBoolean("sound_notifications_enabled", true);
    }

    public void setVibrationNotificationsEnabled(boolean enabled) {
        editor.putBoolean("vibration_notifications_enabled", enabled);
        editor.apply();
    }

    public boolean areVibrationNotificationsEnabled() {
        return sharedPreferences.getBoolean("vibration_notifications_enabled", true);
    }

    public void setAutoSync(boolean enabled) {
        editor.putBoolean("auto_sync_enabled", enabled);
        editor.apply();
    }

    public boolean isAutoSyncEnabled() {
        return sharedPreferences.getBoolean("auto_sync_enabled", true);
    }

    public void setWifiOnlySync(boolean enabled) {
        editor.putBoolean("wifi_only_sync_enabled", enabled);
        editor.apply();
    }

    public boolean isWifiOnlySyncEnabled() {
        return sharedPreferences.getBoolean("wifi_only_sync_enabled", false);
    }

    public void setBackgroundSync(boolean enabled) {
        editor.putBoolean("background_sync_enabled", enabled);
        editor.apply();
    }

    public boolean isBackgroundSyncEnabled() {
        return sharedPreferences.getBoolean("background_sync_enabled", true);
    }

    public void setLocationTracking(boolean enabled) {
        editor.putBoolean("location_tracking_enabled", enabled);
        editor.apply();
    }

    public boolean isLocationTrackingEnabled() {
        return sharedPreferences.getBoolean("location_tracking_enabled", true);
    }

    public void setGpsTracking(boolean enabled) {
        editor.putBoolean("gps_tracking_enabled", enabled);
        editor.apply();
    }

    public boolean isGpsTrackingEnabled() {
        return sharedPreferences.getBoolean("gps_tracking_enabled", true);
    }

    public void setLocationHistory(boolean enabled) {
        editor.putBoolean("location_history_enabled", enabled);
        editor.apply();
    }

    public boolean isLocationHistoryEnabled() {
        return sharedPreferences.getBoolean("location_history_enabled", true);
    }

    public void setDataCollection(boolean enabled) {
        editor.putBoolean("data_collection_enabled", enabled);
        editor.apply();
    }

    public boolean isDataCollectionEnabled() {
        return sharedPreferences.getBoolean("data_collection_enabled", true);
    }

    public void setAnalytics(boolean enabled) {
        editor.putBoolean("analytics_enabled", enabled);
        editor.apply();
    }

    public boolean isAnalyticsEnabled() {
        return sharedPreferences.getBoolean("analytics_enabled", true);
    }

    public void setCrashReporting(boolean enabled) {
        editor.putBoolean("crash_reporting_enabled", enabled);
        editor.apply();
    }

    public boolean isCrashReportingEnabled() {
        return sharedPreferences.getBoolean("crash_reporting_enabled", true);
    }

    public void setDarkMode(boolean enabled) {
        editor.putBoolean("dark_mode_enabled", enabled);
        editor.apply();
    }

    public boolean isDarkModeEnabled() {
        return sharedPreferences.getBoolean("dark_mode_enabled", false);
    }

    public void setAutoBackup(boolean enabled) {
        editor.putBoolean("auto_backup_enabled", enabled);
        editor.apply();
    }

    public boolean isAutoBackupEnabled() {
        return sharedPreferences.getBoolean("auto_backup_enabled", true);
    }

    public void setOfflineMode(boolean enabled) {
        editor.putBoolean("offline_mode_enabled", enabled);
        editor.apply();
    }

    public boolean isOfflineModeEnabled() {
        return sharedPreferences.getBoolean("offline_mode_enabled", false);
    }

    public void resetToDefaults() {
        editor.clear();
        editor.apply();
        
        // Set default values
        setNotificationsEnabled(true);
        setEmailNotificationsEnabled(true);
        setSmsNotificationsEnabled(false);
        setSoundNotificationsEnabled(true);
        setVibrationNotificationsEnabled(true);
        setAutoSync(true);
        setWifiOnlySync(false);
        setBackgroundSync(true);
        setLocationTracking(true);
        setGpsTracking(true);
        setLocationHistory(true);
        setDataCollection(true);
        setAnalytics(true);
        setCrashReporting(true);
        setDarkMode(false);
        setAutoBackup(true);
        setOfflineMode(false);
    }
} 