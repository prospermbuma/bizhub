package com.extrap.co.bizhub.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.services.SyncService;
import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.utils.NetworkUtils;
import com.extrap.co.bizhub.utils.OfflineManager;
import com.extrap.co.bizhub.utils.PreferenceManager;
import com.extrap.co.bizhub.viewmodels.OfflineViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OfflineFragment extends Fragment {
    
    private OfflineViewModel viewModel;
    private OfflineManager offlineManager;
    private NetworkUtils networkUtils;
    private PreferenceManager preferenceManager;
    
    // UI Components
    private SwitchMaterial offlineModeSwitch;
    private SwitchMaterial autoSyncSwitch;
    private SwitchMaterial backgroundSyncSwitch;
    private SwitchMaterial wifiOnlySyncSwitch;
    
    private MaterialCardView syncStatusCard;
    private TextView syncStatusText;
    private TextView lastSyncTimeText;
    private TextView pendingSyncCountText;
    private CircularProgressIndicator syncProgressIndicator;
    
    private MaterialButton syncNowButton;
    private MaterialButton cacheDataButton;
    private MaterialButton clearCacheButton;
    private MaterialButton exportDataButton;
    private MaterialButton importDataButton;
    
    private TextView networkStatusText;
    private TextView offlineDataStatusText;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offline, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupViewModel();
        setupOfflineManager();
        setupClickListeners();
        observeData();
        updateUI();
    }
    
    private void initializeViews(View view) {
        // Switches
        offlineModeSwitch = view.findViewById(R.id.offline_mode_switch);
        autoSyncSwitch = view.findViewById(R.id.auto_sync_switch);
        backgroundSyncSwitch = view.findViewById(R.id.background_sync_switch);
        wifiOnlySyncSwitch = view.findViewById(R.id.wifi_only_sync_switch);
        
        // Sync Status
        syncStatusCard = view.findViewById(R.id.sync_status_card);
        syncStatusText = view.findViewById(R.id.sync_status_text);
        lastSyncTimeText = view.findViewById(R.id.last_sync_time_text);
        pendingSyncCountText = view.findViewById(R.id.pending_sync_count_text);
        syncProgressIndicator = view.findViewById(R.id.sync_progress_indicator);
        
        // Buttons
        syncNowButton = view.findViewById(R.id.sync_now_button);
        cacheDataButton = view.findViewById(R.id.cache_data_button);
        clearCacheButton = view.findViewById(R.id.clear_cache_button);
        exportDataButton = view.findViewById(R.id.export_data_button);
        importDataButton = view.findViewById(R.id.import_data_button);
        
        // Status Text
        networkStatusText = view.findViewById(R.id.network_status_text);
        offlineDataStatusText = view.findViewById(R.id.offline_data_status_text);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(OfflineViewModel.class);
    }
    
    private void setupOfflineManager() {
        offlineManager = new OfflineManager(requireContext());
        networkUtils = FieldServiceApp.getInstance().getNetworkUtils();
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
    }
    
    private void setupClickListeners() {
        // Switches
        offlineModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            offlineManager.setOfflineMode(isChecked);
            updateUI();
            showOfflineModeDialog(isChecked);
        });
        
        autoSyncSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setAutoSync(isChecked);
        });
        
        backgroundSyncSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setBackgroundSync(isChecked);
        });
        
        wifiOnlySyncSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setWifiOnlySync(isChecked);
        });
        
        // Buttons
        syncNowButton.setOnClickListener(v -> {
            if (networkUtils.isNetworkAvailable()) {
                startSync();
            } else {
                showNetworkErrorDialog();
            }
        });
        
        cacheDataButton.setOnClickListener(v -> {
            showCacheDataDialog();
        });
        
        clearCacheButton.setOnClickListener(v -> {
            showClearCacheDialog();
        });
        
        exportDataButton.setOnClickListener(v -> {
            exportOfflineData();
        });
        
        importDataButton.setOnClickListener(v -> {
            importOfflineData();
        });
    }
    
    private void observeData() {
        viewModel.getSyncStatus().observe(getViewLifecycleOwner(), status -> {
            updateSyncStatus(status);
        });
        
        viewModel.getPendingSyncCount().observe(getViewLifecycleOwner(), count -> {
            updatePendingSyncCount(count);
        });
        
        viewModel.getLastSyncTime().observe(getViewLifecycleOwner(), timestamp -> {
            updateLastSyncTime(timestamp);
        });
        
        viewModel.getNetworkStatus().observe(getViewLifecycleOwner(), isConnected -> {
            updateNetworkStatus(isConnected);
        });
        
        viewModel.getOfflineDataStatus().observe(getViewLifecycleOwner(), isValid -> {
            updateOfflineDataStatus(isValid);
        });
    }
    
    private void updateUI() {
        // Update switches
        offlineModeSwitch.setChecked(offlineManager.isOfflineMode());
        autoSyncSwitch.setChecked(preferenceManager.isAutoSyncEnabled());
        backgroundSyncSwitch.setChecked(preferenceManager.isBackgroundSyncEnabled());
        wifiOnlySyncSwitch.setChecked(preferenceManager.isWifiOnlySyncEnabled());
        
        // Update sync status
        updateSyncStatus(offlineManager.isSyncing() ? "Syncing..." : "Idle");
        updatePendingSyncCount(offlineManager.getPendingSyncCount());
        updateLastSyncTime(offlineManager.getLastSyncTime());
        updateNetworkStatus(networkUtils.isNetworkAvailable());
        updateOfflineDataStatus(offlineManager.validateOfflineData());
    }
    
    private void updateSyncStatus(String status) {
        syncStatusText.setText(status);
        if ("Syncing...".equals(status)) {
            syncProgressIndicator.setVisibility(View.VISIBLE);
            syncProgressIndicator.setIndeterminate(true);
            syncNowButton.setEnabled(false);
        } else {
            syncProgressIndicator.setVisibility(View.GONE);
            syncNowButton.setEnabled(true);
        }
    }
    
    private void updatePendingSyncCount(int count) {
        if (count > 0) {
            pendingSyncCountText.setText(count + " items pending sync");
            pendingSyncCountText.setVisibility(View.VISIBLE);
        } else {
            pendingSyncCountText.setVisibility(View.GONE);
        }
    }
    
    private void updateLastSyncTime(long timestamp) {
        if (timestamp > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
            lastSyncTimeText.setText("Last sync: " + dateFormat.format(new Date(timestamp)));
        } else {
            lastSyncTimeText.setText("Never synced");
        }
    }
    
    private void updateNetworkStatus(boolean isConnected) {
        if (isConnected) {
            networkStatusText.setText("🟢 Online");
            networkStatusText.setTextColor(requireContext().getColor(R.color.success));
        } else {
            networkStatusText.setText("🔴 Offline");
            networkStatusText.setTextColor(requireContext().getColor(R.color.error));
        }
    }
    
    private void updateOfflineDataStatus(boolean isValid) {
        if (isValid) {
            offlineDataStatusText.setText("✅ Offline data available");
            offlineDataStatusText.setTextColor(requireContext().getColor(R.color.success));
        } else {
            offlineDataStatusText.setText("⚠️ Limited offline data");
            offlineDataStatusText.setTextColor(requireContext().getColor(R.color.warning));
        }
    }
    
    private void startSync() {
        Intent intent = new Intent(requireContext(), SyncService.class);
        intent.setAction("SYNC_NOW");
        requireContext().startService(intent);
        
        updateSyncStatus("Syncing...");
    }
    
    private void showOfflineModeDialog(boolean isOffline) {
        String title = isOffline ? "Offline Mode Enabled" : "Offline Mode Disabled";
        String message = isOffline ? 
            "The app will work without internet connection. Some features may be limited." :
            "The app will sync data when connected to the internet.";
        
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }
    
    private void showNetworkErrorDialog() {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("OK", null)
            .show();
    }
    
    private void showCacheDataDialog() {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cache Data")
            .setMessage("This will download and cache data for offline use. Continue?")
            .setPositiveButton("Cache", (dialog, which) -> {
                Intent intent = new Intent(requireContext(), SyncService.class);
                intent.setAction("CACHE_DATA");
                requireContext().startService(intent);
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
    
    private void showClearCacheDialog() {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Clear Cache")
            .setMessage("This will clear all cached data. This action cannot be undone. Continue?")
            .setPositiveButton("Clear", (dialog, which) -> {
                Intent intent = new Intent(requireContext(), SyncService.class);
                intent.setAction("CLEAR_CACHE");
                requireContext().startService(intent);
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
    
    private void exportOfflineData() {
        // TODO: Implement offline data export
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Export Data")
            .setMessage("Export functionality coming soon!")
            .setPositiveButton("OK", null)
            .show();
    }
    
    private void importOfflineData() {
        // TODO: Implement offline data import
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Import Data")
            .setMessage("Import functionality coming soon!")
            .setPositiveButton("OK", null)
            .show();
    }
} 