package com.extrap.co.bizhub.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.utils.NetworkUtils;
import com.extrap.co.bizhub.utils.NotificationManager;
import com.extrap.co.bizhub.utils.OfflineManager;
import com.extrap.co.bizhub.utils.PreferenceManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SyncService extends Service {
    
    private static final String TAG = "SyncService";
    private static final long SYNC_INTERVAL = 30; // minutes
    private static final long INITIAL_DELAY = 5; // minutes
    
    private OfflineManager offlineManager;
    private NetworkUtils networkUtils;
    private PreferenceManager preferenceManager;
    private NotificationManager notificationManager;
    private ScheduledExecutorService scheduler;
    private ExecutorService executorService;
    
    @Override
    public void onCreate() {
        super.onCreate();
        offlineManager = new OfflineManager(this);
        networkUtils = FieldServiceApp.getInstance().getNetworkUtils();
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        notificationManager = new NotificationManager(this);
        scheduler = Executors.newScheduledThreadPool(1);
        executorService = Executors.newFixedThreadPool(2);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "SyncService started");
        
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case "SYNC_NOW":
                        performSync();
                        break;
                    case "CACHE_DATA":
                        cacheData();
                        break;
                    case "VALIDATE_OFFLINE_DATA":
                        validateOfflineData();
                        break;
                    case "CLEAR_CACHE":
                        clearCache();
                        break;
                }
            }
        }
        
        // Schedule periodic sync
        schedulePeriodicSync();
        
        return START_STICKY;
    }
    
    private void schedulePeriodicSync() {
        if (preferenceManager.isBackgroundSyncEnabled()) {
            scheduler.scheduleAtFixedRate(() -> {
                if (networkUtils.isNetworkAvailable() && !offlineManager.isOfflineMode()) {
                    performSync();
                }
            }, INITIAL_DELAY, SYNC_INTERVAL, TimeUnit.MINUTES);
            
            Log.d(TAG, "Periodic sync scheduled every " + SYNC_INTERVAL + " minutes");
        }
    }
    
    private void performSync() {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Starting sync operation...");
                
                if (!networkUtils.isNetworkAvailable()) {
                    Log.d(TAG, "No network available, skipping sync");
                    return;
                }
                
                if (offlineManager.isOfflineMode()) {
                    Log.d(TAG, "Offline mode enabled, skipping sync");
                    return;
                }
                
                // Check if there's pending data to sync
                if (offlineManager.hasPendingSync()) {
                    Log.d(TAG, "Found pending data to sync");
                    offlineManager.syncData();
                    
                    // Show sync notification
                    notificationManager.showSyncNotification("Data synchronized successfully", true);
                } else {
                    Log.d(TAG, "No pending data to sync");
                }
                
                // Update last sync time
                preferenceManager.setLastSyncTime(System.currentTimeMillis());
                
            } catch (Exception e) {
                Log.e(TAG, "Error during sync operation", e);
                notificationManager.showSyncNotification("Sync failed: " + e.getMessage(), false);
            }
        });
    }
    
    private void cacheData() {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Starting data caching...");
                
                // Cache work orders
                cacheWorkOrders();
                
                // Cache customers
                cacheCustomers();
                
                // Cache users
                cacheUsers();
                
                Log.d(TAG, "Data caching completed");
                
            } catch (Exception e) {
                Log.e(TAG, "Error during data caching", e);
            }
        });
    }
    
    private void cacheWorkOrders() {
        try {
            // TODO: Implement API call to fetch work orders
            // For now, just log the operation
            Log.d(TAG, "Caching work orders...");
            
            // Simulate API call delay
            Thread.sleep(1000);
            
        } catch (Exception e) {
            Log.e(TAG, "Error caching work orders", e);
        }
    }
    
    private void cacheCustomers() {
        try {
            // TODO: Implement API call to fetch customers
            Log.d(TAG, "Caching customers...");
            
            // Simulate API call delay
            Thread.sleep(1000);
            
        } catch (Exception e) {
            Log.e(TAG, "Error caching customers", e);
        }
    }
    
    private void cacheUsers() {
        try {
            // TODO: Implement API call to fetch users
            Log.d(TAG, "Caching users...");
            
            // Simulate API call delay
            Thread.sleep(1000);
            
        } catch (Exception e) {
            Log.e(TAG, "Error caching users", e);
        }
    }
    
    private void validateOfflineData() {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Validating offline data...");
                
                boolean isValid = offlineManager.validateOfflineData();
                
                if (isValid) {
                    Log.d(TAG, "Offline data validation passed");
                } else {
                    Log.w(TAG, "Offline data validation failed");
                    // Trigger data refresh
                    cacheData();
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error validating offline data", e);
            }
        });
    }
    
    private void clearCache() {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Clearing cache...");
                offlineManager.clearCache();
                Log.d(TAG, "Cache cleared successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error clearing cache", e);
            }
        });
    }
    
    // Network state monitoring
    public void onNetworkStateChanged(boolean isConnected) {
        if (isConnected) {
            Log.d(TAG, "Network connected, checking for pending sync");
            offlineManager.onNetworkStateChanged(true);
        } else {
            Log.d(TAG, "Network disconnected");
        }
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        if (offlineManager != null) {
            offlineManager.shutdown();
        }
        Log.d(TAG, "SyncService destroyed");
    }
} 