package com.extrap.co.bizhub.activities;

import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.utils.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    
    private Switch notificationsSwitch;
    private Switch autoSyncSwitch;
    private Switch locationTrackingSwitch;
    private PreferenceManager preferenceManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initializeViews();
        setupToolbar();
        loadSettings();
        setupClickListeners();
    }
    
    private void initializeViews() {
        notificationsSwitch = findViewById(R.id.notifications_switch);
        autoSyncSwitch = findViewById(R.id.auto_sync_switch);
        locationTrackingSwitch = findViewById(R.id.location_tracking_switch);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void loadSettings() {
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        
        notificationsSwitch.setChecked(preferenceManager.areNotificationsEnabled());
        autoSyncSwitch.setChecked(preferenceManager.isAutoSyncEnabled());
        locationTrackingSwitch.setChecked(preferenceManager.isLocationTrackingEnabled());
    }
    
    private void setupClickListeners() {
        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setNotificationsEnabled(isChecked);
        });
        
        autoSyncSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setAutoSync(isChecked);
        });
        
        locationTrackingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setLocationTracking(isChecked);
        });
    }
} 