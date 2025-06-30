package com.extrap.co.bizhub.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.utils.PreferenceManager;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {
    
    private static final String TAG = "SettingsActivity";
    
    private SwitchMaterial notificationsSwitch;
    private SwitchMaterial autoSyncSwitch;
    private SwitchMaterial locationTrackingSwitch;
    private PreferenceManager preferenceManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SettingsActivity onCreate started");
        
        try {
            setContentView(R.layout.activity_settings);
            Log.d(TAG, "SettingsActivity layout set successfully");
            
            initializeViews();
            setupToolbar();
            loadSettings();
            setupClickListeners();
            
            Log.d(TAG, "SettingsActivity onCreate completed successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in SettingsActivity onCreate", e);
            // Show error message to user
            finish();
        }
    }
    
    private void initializeViews() {
        try {
            notificationsSwitch = findViewById(R.id.notifications_switch);
            autoSyncSwitch = findViewById(R.id.auto_sync_switch);
            locationTrackingSwitch = findViewById(R.id.location_tracking_switch);
            
            // Check if views were found
            if (notificationsSwitch == null) {
                Log.e(TAG, "notifications_switch not found in layout");
            }
            if (autoSyncSwitch == null) {
                Log.e(TAG, "auto_sync_switch not found in layout");
            }
            if (locationTrackingSwitch == null) {
                Log.e(TAG, "location_tracking_switch not found in layout");
            }
            
            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
        }
    }
    
    private void setupToolbar() {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.settings);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            } else {
                Log.e(TAG, "Toolbar not found in layout");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up toolbar", e);
        }
    }
    
    private void loadSettings() {
        try {
            preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
            
            if (notificationsSwitch != null) {
                notificationsSwitch.setChecked(preferenceManager.areNotificationsEnabled());
            }
            if (autoSyncSwitch != null) {
                autoSyncSwitch.setChecked(preferenceManager.isAutoSyncEnabled());
            }
            if (locationTrackingSwitch != null) {
                locationTrackingSwitch.setChecked(preferenceManager.isLocationTrackingEnabled());
            }
            
            Log.d(TAG, "Settings loaded successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error loading settings", e);
        }
    }
    
    private void setupClickListeners() {
        try {
            if (notificationsSwitch != null) {
                notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    try {
                        preferenceManager.setNotificationsEnabled(isChecked);
                        Log.d(TAG, "Notifications setting changed to: " + isChecked);
                    } catch (Exception e) {
                        Log.e(TAG, "Error saving notifications setting", e);
                    }
                });
            }
            
            if (autoSyncSwitch != null) {
                autoSyncSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    try {
                        preferenceManager.setAutoSync(isChecked);
                        Log.d(TAG, "Auto sync setting changed to: " + isChecked);
                    } catch (Exception e) {
                        Log.e(TAG, "Error saving auto sync setting", e);
                    }
                });
            }
            
            if (locationTrackingSwitch != null) {
                locationTrackingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    try {
                        preferenceManager.setLocationTracking(isChecked);
                        Log.d(TAG, "Location tracking setting changed to: " + isChecked);
                    } catch (Exception e) {
                        Log.e(TAG, "Error saving location tracking setting", e);
                    }
                });
            }
            
            Log.d(TAG, "Click listeners setup successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners", e);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
                return true;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            Log.e(TAG, "Error in onOptionsItemSelected", e);
            return super.onOptionsItemSelected(item);
        }
    }
} 