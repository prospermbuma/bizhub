package com.extrap.co.bizhub.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.utils.PreferenceManager;
import com.extrap.co.bizhub.viewmodels.SettingsViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {
    
    private SettingsViewModel viewModel;
    private PreferenceManager preferenceManager;
    
    // Notification Settings
    private SwitchMaterial pushNotificationsSwitch;
    private SwitchMaterial emailNotificationsSwitch;
    private SwitchMaterial smsNotificationsSwitch;
    private SwitchMaterial soundNotificationsSwitch;
    private SwitchMaterial vibrationNotificationsSwitch;
    
    // Sync Settings
    private SwitchMaterial autoSyncSwitch;
    private SwitchMaterial wifiOnlySyncSwitch;
    private SwitchMaterial backgroundSyncSwitch;
    
    // Location Settings
    private SwitchMaterial locationTrackingSwitch;
    private SwitchMaterial gpsTrackingSwitch;
    private SwitchMaterial locationHistorySwitch;
    
    // Privacy Settings
    private SwitchMaterial dataCollectionSwitch;
    private SwitchMaterial analyticsSwitch;
    private SwitchMaterial crashReportingSwitch;
    
    // App Settings
    private SwitchMaterial darkModeSwitch;
    private SwitchMaterial autoBackupSwitch;
    private SwitchMaterial offlineModeSwitch;
    
    // About Section
    private MaterialCardView aboutCard;
    private MaterialCardView privacyPolicyCard;
    private MaterialCardView termsOfServiceCard;
    private MaterialCardView contactSupportCard;
    private MaterialCardView rateAppCard;
    private MaterialCardView shareAppCard;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupViewModel();
        loadSettings();
        setupClickListeners();
    }
    
    private void initializeViews(View view) {
        // Notification Settings
        pushNotificationsSwitch = view.findViewById(R.id.push_notifications_switch);
        emailNotificationsSwitch = view.findViewById(R.id.email_notifications_switch);
        smsNotificationsSwitch = view.findViewById(R.id.sms_notifications_switch);
        soundNotificationsSwitch = view.findViewById(R.id.sound_notifications_switch);
        vibrationNotificationsSwitch = view.findViewById(R.id.vibration_notifications_switch);
        
        // Sync Settings
        autoSyncSwitch = view.findViewById(R.id.auto_sync_switch);
        wifiOnlySyncSwitch = view.findViewById(R.id.wifi_only_sync_switch);
        backgroundSyncSwitch = view.findViewById(R.id.background_sync_switch);
        
        // Location Settings
        locationTrackingSwitch = view.findViewById(R.id.location_tracking_switch);
        gpsTrackingSwitch = view.findViewById(R.id.gps_tracking_switch);
        locationHistorySwitch = view.findViewById(R.id.location_history_switch);
        
        // Privacy Settings
        dataCollectionSwitch = view.findViewById(R.id.data_collection_switch);
        analyticsSwitch = view.findViewById(R.id.analytics_switch);
        crashReportingSwitch = view.findViewById(R.id.crash_reporting_switch);
        
        // App Settings
        darkModeSwitch = view.findViewById(R.id.dark_mode_switch);
        autoBackupSwitch = view.findViewById(R.id.auto_backup_switch);
        offlineModeSwitch = view.findViewById(R.id.offline_mode_switch);
        
        // About Section
        aboutCard = view.findViewById(R.id.about_card);
        privacyPolicyCard = view.findViewById(R.id.privacy_policy_card);
        termsOfServiceCard = view.findViewById(R.id.terms_of_service_card);
        contactSupportCard = view.findViewById(R.id.contact_support_card);
        rateAppCard = view.findViewById(R.id.rate_app_card);
        shareAppCard = view.findViewById(R.id.share_app_card);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
    }
    
    private void loadSettings() {
        // Load notification settings
        pushNotificationsSwitch.setChecked(preferenceManager.areNotificationsEnabled());
        emailNotificationsSwitch.setChecked(preferenceManager.areEmailNotificationsEnabled());
        smsNotificationsSwitch.setChecked(preferenceManager.areSmsNotificationsEnabled());
        soundNotificationsSwitch.setChecked(preferenceManager.areSoundNotificationsEnabled());
        vibrationNotificationsSwitch.setChecked(preferenceManager.areVibrationNotificationsEnabled());
        
        // Load sync settings
        autoSyncSwitch.setChecked(preferenceManager.isAutoSyncEnabled());
        wifiOnlySyncSwitch.setChecked(preferenceManager.isWifiOnlySyncEnabled());
        backgroundSyncSwitch.setChecked(preferenceManager.isBackgroundSyncEnabled());
        
        // Load location settings
        locationTrackingSwitch.setChecked(preferenceManager.isLocationTrackingEnabled());
        gpsTrackingSwitch.setChecked(preferenceManager.isGpsTrackingEnabled());
        locationHistorySwitch.setChecked(preferenceManager.isLocationHistoryEnabled());
        
        // Load privacy settings
        dataCollectionSwitch.setChecked(preferenceManager.isDataCollectionEnabled());
        analyticsSwitch.setChecked(preferenceManager.isAnalyticsEnabled());
        crashReportingSwitch.setChecked(preferenceManager.isCrashReportingEnabled());
        
        // Load app settings
        darkModeSwitch.setChecked(preferenceManager.isDarkModeEnabled());
        autoBackupSwitch.setChecked(preferenceManager.isAutoBackupEnabled());
        offlineModeSwitch.setChecked(preferenceManager.isOfflineModeEnabled());
    }
    
    private void setupClickListeners() {
        // Notification Settings
        pushNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setNotificationsEnabled(isChecked);
            viewModel.updateNotificationSettings();
        });
        
        emailNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setEmailNotificationsEnabled(isChecked);
        });
        
        smsNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setSmsNotificationsEnabled(isChecked);
        });
        
        soundNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setSoundNotificationsEnabled(isChecked);
        });
        
        vibrationNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setVibrationNotificationsEnabled(isChecked);
        });
        
        // Sync Settings
        autoSyncSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setAutoSync(isChecked);
            viewModel.updateSyncSettings();
        });
        
        wifiOnlySyncSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setWifiOnlySync(isChecked);
        });
        
        backgroundSyncSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setBackgroundSync(isChecked);
        });
        
        // Location Settings
        locationTrackingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setLocationTracking(isChecked);
            viewModel.updateLocationSettings();
        });
        
        gpsTrackingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setGpsTracking(isChecked);
        });
        
        locationHistorySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setLocationHistory(isChecked);
        });
        
        // Privacy Settings
        dataCollectionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setDataCollection(isChecked);
            viewModel.updatePrivacySettings();
        });
        
        analyticsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setAnalytics(isChecked);
        });
        
        crashReportingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setCrashReporting(isChecked);
        });
        
        // App Settings
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setDarkMode(isChecked);
            viewModel.updateAppSettings();
            showRestartDialog();
        });
        
        autoBackupSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setAutoBackup(isChecked);
        });
        
        offlineModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setOfflineMode(isChecked);
        });
        
        // About Section
        aboutCard.setOnClickListener(v -> showAboutDialog());
        privacyPolicyCard.setOnClickListener(v -> navigateToPrivacyPolicy());
        termsOfServiceCard.setOnClickListener(v -> navigateToTermsOfService());
        contactSupportCard.setOnClickListener(v -> contactSupport());
        rateAppCard.setOnClickListener(v -> rateApp());
        shareAppCard.setOnClickListener(v -> shareApp());
    }
    
    private void showRestartDialog() {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Restart Required")
            .setMessage("Some settings require a restart to take effect. Would you like to restart the app now?")
            .setPositiveButton("Restart", (dialog, which) -> {
                // TODO: Implement app restart
                Toast.makeText(getContext(), "App restart functionality coming soon", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Later", null)
            .show();
    }
    
    private void showAboutDialog() {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("About BizHub")
            .setMessage("BizHub Field Service Management\nVersion 1.0.0\n\nA comprehensive field service management solution for technicians and service providers.")
            .setPositiveButton("OK", null)
            .show();
    }
    
    private void navigateToPrivacyPolicy() {
        // Navigate to privacy policy fragment
        if (getActivity() instanceof com.extrap.co.bizhub.activities.DashboardActivity) {
            com.extrap.co.bizhub.activities.DashboardActivity activity = 
                (com.extrap.co.bizhub.activities.DashboardActivity) getActivity();
            activity.loadFragment(new PrivacyPolicyFragment());
            activity.getSupportActionBar().setTitle(R.string.privacy_policy);
        }
    }
    
    private void navigateToTermsOfService() {
        // TODO: Navigate to terms of service
        Toast.makeText(getContext(), "Terms of Service", Toast.LENGTH_SHORT).show();
    }
    
    private void contactSupport() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:support@bizhub.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "BizHub Support Request");
        intent.putExtra(Intent.EXTRA_TEXT, "Please describe your issue here...");
        
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void rateApp() {
        // TODO: Open app store rating
        Toast.makeText(getContext(), "Rate app functionality coming soon", Toast.LENGTH_SHORT).show();
    }
    
    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out BizHub");
        intent.putExtra(Intent.EXTRA_TEXT, "I'm using BizHub for field service management. Check it out!");
        
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "Share via"));
        } else {
            Toast.makeText(getContext(), "No sharing app found", Toast.LENGTH_SHORT).show();
        }
    }
} 