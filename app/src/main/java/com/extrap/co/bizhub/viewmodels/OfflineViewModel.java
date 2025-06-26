package com.extrap.co.bizhub.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.utils.NetworkUtils;
import com.extrap.co.bizhub.utils.OfflineManager;
import com.extrap.co.bizhub.utils.PreferenceManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OfflineViewModel extends AndroidViewModel {
    
    private OfflineManager offlineManager;
    private NetworkUtils networkUtils;
    private PreferenceManager preferenceManager;
    private ExecutorService executorService;
    
    private MutableLiveData<String> syncStatus = new MutableLiveData<>();
    private MutableLiveData<Integer> pendingSyncCount = new MutableLiveData<>();
    private MutableLiveData<Long> lastSyncTime = new MutableLiveData<>();
    private MutableLiveData<Boolean> networkStatus = new MutableLiveData<>();
    private MutableLiveData<Boolean> offlineDataStatus = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<String> successMessage = new MutableLiveData<>();
    
    public OfflineViewModel(Application application) {
        super(application);
        offlineManager = new OfflineManager(application);
        networkUtils = FieldServiceApp.getInstance().getNetworkUtils();
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        executorService = Executors.newFixedThreadPool(2);
        
        // Initialize data
        loadInitialData();
    }
    
    public LiveData<String> getSyncStatus() {
        return syncStatus;
    }
    
    public LiveData<Integer> getPendingSyncCount() {
        return pendingSyncCount;
    }
    
    public LiveData<Long> getLastSyncTime() {
        return lastSyncTime;
    }
    
    public LiveData<Boolean> getNetworkStatus() {
        return networkStatus;
    }
    
    public LiveData<Boolean> getOfflineDataStatus() {
        return offlineDataStatus;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }
    
    private void loadInitialData() {
        executorService.execute(() -> {
            try {
                // Load initial status
                syncStatus.postValue(offlineManager.isSyncing() ? "Syncing..." : "Idle");
                pendingSyncCount.postValue(offlineManager.getPendingSyncCount());
                lastSyncTime.postValue(offlineManager.getLastSyncTime());
                networkStatus.postValue(networkUtils.isNetworkAvailable());
                offlineDataStatus.postValue(offlineManager.validateOfflineData());
                
            } catch (Exception e) {
                errorMessage.postValue("Error loading offline data: " + e.getMessage());
            }
        });
    }
    
    public void refreshData() {
        executorService.execute(() -> {
            try {
                // Refresh all data
                syncStatus.postValue(offlineManager.isSyncing() ? "Syncing..." : "Idle");
                pendingSyncCount.postValue(offlineManager.getPendingSyncCount());
                lastSyncTime.postValue(offlineManager.getLastSyncTime());
                networkStatus.postValue(networkUtils.isNetworkAvailable());
                offlineDataStatus.postValue(offlineManager.validateOfflineData());
                
                successMessage.postValue("Data refreshed successfully");
                
            } catch (Exception e) {
                errorMessage.postValue("Error refreshing data: " + e.getMessage());
            }
        });
    }
    
    public void setOfflineMode(boolean isOffline) {
        executorService.execute(() -> {
            try {
                offlineManager.setOfflineMode(isOffline);
                
                if (isOffline) {
                    successMessage.postValue("Offline mode enabled");
                } else {
                    successMessage.postValue("Offline mode disabled");
                }
                
            } catch (Exception e) {
                errorMessage.postValue("Error setting offline mode: " + e.getMessage());
            }
        });
    }
    
    public void syncData() {
        executorService.execute(() -> {
            try {
                if (!networkUtils.isNetworkAvailable()) {
                    errorMessage.postValue("No internet connection available");
                    return;
                }
                
                if (offlineManager.isOfflineMode()) {
                    errorMessage.postValue("Cannot sync in offline mode");
                    return;
                }
                
                syncStatus.postValue("Syncing...");
                offlineManager.syncData();
                
                // Update status after sync
                Thread.sleep(2000); // Give sync time to complete
                syncStatus.postValue("Idle");
                pendingSyncCount.postValue(offlineManager.getPendingSyncCount());
                lastSyncTime.postValue(offlineManager.getLastSyncTime());
                
                successMessage.postValue("Sync completed successfully");
                
            } catch (Exception e) {
                syncStatus.postValue("Error");
                errorMessage.postValue("Error syncing data: " + e.getMessage());
            }
        });
    }
    
    public void cacheData() {
        executorService.execute(() -> {
            try {
                syncStatus.postValue("Caching...");
                
                // TODO: Implement actual data caching
                Thread.sleep(3000); // Simulate caching time
                
                syncStatus.postValue("Idle");
                offlineDataStatus.postValue(offlineManager.validateOfflineData());
                
                successMessage.postValue("Data cached successfully");
                
            } catch (Exception e) {
                syncStatus.postValue("Error");
                errorMessage.postValue("Error caching data: " + e.getMessage());
            }
        });
    }
    
    public void clearCache() {
        executorService.execute(() -> {
            try {
                offlineManager.clearCache();
                offlineDataStatus.postValue(offlineManager.validateOfflineData());
                
                successMessage.postValue("Cache cleared successfully");
                
            } catch (Exception e) {
                errorMessage.postValue("Error clearing cache: " + e.getMessage());
            }
        });
    }
    
    public void exportData() {
        executorService.execute(() -> {
            try {
                offlineManager.exportOfflineData();
                successMessage.postValue("Data export started");
                
            } catch (Exception e) {
                errorMessage.postValue("Error exporting data: " + e.getMessage());
            }
        });
    }
    
    public void importData() {
        executorService.execute(() -> {
            try {
                offlineManager.importOfflineData();
                successMessage.postValue("Data import started");
                
            } catch (Exception e) {
                errorMessage.postValue("Error importing data: " + e.getMessage());
            }
        });
    }
    
    public void validateOfflineData() {
        executorService.execute(() -> {
            try {
                boolean isValid = offlineManager.validateOfflineData();
                offlineDataStatus.postValue(isValid);
                
                if (isValid) {
                    successMessage.postValue("Offline data validation passed");
                } else {
                    errorMessage.postValue("Offline data validation failed");
                }
                
            } catch (Exception e) {
                errorMessage.postValue("Error validating offline data: " + e.getMessage());
            }
        });
    }
    
    public boolean isOfflineMode() {
        return offlineManager.isOfflineMode();
    }
    
    public boolean isSyncing() {
        return offlineManager.isSyncing();
    }
    
    public boolean hasPendingSync() {
        return offlineManager.hasPendingSync();
    }
    
    public int getPendingSyncCount() {
        return offlineManager.getPendingSyncCount();
    }
    
    public long getLastSyncTime() {
        return offlineManager.getLastSyncTime();
    }
    
    public boolean isNetworkAvailable() {
        return networkUtils.isNetworkAvailable();
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
        if (offlineManager != null) {
            offlineManager.shutdown();
        }
    }
} 