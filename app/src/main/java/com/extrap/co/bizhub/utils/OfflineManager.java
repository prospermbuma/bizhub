package com.extrap.co.bizhub.utils;

import android.content.Context;
import android.util.Log;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.AppDatabase;
import com.extrap.co.bizhub.data.dao.CustomerDao;
import com.extrap.co.bizhub.data.dao.UserDao;
import com.extrap.co.bizhub.data.dao.WorkOrderDao;
import com.extrap.co.bizhub.data.entities.Customer;
import com.extrap.co.bizhub.data.entities.User;
import com.extrap.co.bizhub.data.entities.WorkOrder;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OfflineManager {
    
    private static final String TAG = "OfflineManager";
    
    private Context context;
    private AppDatabase database;
    private PreferenceManager preferenceManager;
    private NetworkUtils networkUtils;
    private ExecutorService executorService;
    
    private boolean isOfflineMode = false;
    private boolean isSyncing = false;
    
    public OfflineManager(Context context) {
        this.context = context;
        this.database = FieldServiceApp.getInstance().getDatabase();
        this.preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        this.networkUtils = FieldServiceApp.getInstance().getNetworkUtils();
        this.executorService = Executors.newFixedThreadPool(4);
        
        // Check initial offline mode status
        this.isOfflineMode = preferenceManager.isOfflineModeEnabled();
    }
    
    // Offline Mode Management
    public boolean isOfflineMode() {
        return isOfflineMode;
    }
    
    public void setOfflineMode(boolean offlineMode) {
        this.isOfflineMode = offlineMode;
        preferenceManager.setOfflineMode(offlineMode);
        
        if (!offlineMode && networkUtils.isNetworkAvailable()) {
            // Auto-sync when coming back online
            syncData();
        }
    }
    
    public boolean isSyncing() {
        return isSyncing;
    }
    
    // Data Caching
    public void cacheWorkOrders(List<WorkOrder> workOrders) {
        executorService.execute(() -> {
            try {
                WorkOrderDao workOrderDao = database.workOrderDao();
                for (WorkOrder workOrder : workOrders) {
                    workOrderDao.insertWorkOrder(workOrder);
                }
                Log.d(TAG, "Cached " + workOrders.size() + " work orders");
            } catch (Exception e) {
                Log.e(TAG, "Error caching work orders", e);
            }
        });
    }
    
    public void cacheCustomers(List<Customer> customers) {
        executorService.execute(() -> {
            try {
                CustomerDao customerDao = database.customerDao();
                for (Customer customer : customers) {
                    customerDao.insertCustomer(customer);
                }
                Log.d(TAG, "Cached " + customers.size() + " customers");
            } catch (Exception e) {
                Log.e(TAG, "Error caching customers", e);
            }
        });
    }
    
    public void cacheUsers(List<User> users) {
        executorService.execute(() -> {
            try {
                UserDao userDao = database.userDao();
                for (User user : users) {
                    userDao.insertUser(user);
                }
                Log.d(TAG, "Cached " + users.size() + " users");
            } catch (Exception e) {
                Log.e(TAG, "Error caching users", e);
            }
        });
    }
    
    // Offline Data Operations
    public void saveWorkOrderOffline(WorkOrder workOrder) {
        executorService.execute(() -> {
            try {
                workOrder.setSyncStatus("pending");
                database.workOrderDao().insertWorkOrder(workOrder);
                Log.d(TAG, "Saved work order offline: " + workOrder.getWorkOrderNumber());
            } catch (Exception e) {
                Log.e(TAG, "Error saving work order offline", e);
            }
        });
    }
    
    public void updateWorkOrderOffline(WorkOrder workOrder) {
        executorService.execute(() -> {
            try {
                workOrder.setSyncStatus("pending");
                database.workOrderDao().updateWorkOrder(workOrder);
                Log.d(TAG, "Updated work order offline: " + workOrder.getWorkOrderNumber());
            } catch (Exception e) {
                Log.e(TAG, "Error updating work order offline", e);
            }
        });
    }
    
    public void saveCustomerOffline(Customer customer) {
        executorService.execute(() -> {
            try {
                customer.setSyncStatus("pending");
                database.customerDao().insertCustomer(customer);
                Log.d(TAG, "Saved customer offline: " + customer.getName());
            } catch (Exception e) {
                Log.e(TAG, "Error saving customer offline", e);
            }
        });
    }
    
    public void updateCustomerOffline(Customer customer) {
        executorService.execute(() -> {
            try {
                customer.setSyncStatus("pending");
                database.customerDao().updateCustomer(customer);
                Log.d(TAG, "Updated customer offline: " + customer.getName());
            } catch (Exception e) {
                Log.e(TAG, "Error updating customer offline", e);
            }
        });
    }
    
    // Sync Management
    public void syncData() {
        if (isSyncing || !networkUtils.isNetworkAvailable()) {
            return;
        }
        
        isSyncing = true;
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Starting data sync...");
                
                // Sync pending work orders
                syncPendingWorkOrders();
                
                // Sync pending customers
                syncPendingCustomers();
                
                // Sync pending users
                syncPendingUsers();
                
                // Update sync timestamp
                preferenceManager.setLastSyncTime(System.currentTimeMillis());
                
                Log.d(TAG, "Data sync completed successfully");
                
            } catch (Exception e) {
                Log.e(TAG, "Error during data sync", e);
            } finally {
                isSyncing = false;
            }
        });
    }
    
    private void syncPendingWorkOrders() {
        try {
            List<WorkOrder> pendingWorkOrders = database.workOrderDao().getPendingSyncWorkOrders();
            
            for (WorkOrder workOrder : pendingWorkOrders) {
                // TODO: Implement actual API call to sync work order
                // For now, just mark as synced
                workOrder.setSyncStatus("synced");
                database.workOrderDao().updateWorkOrder(workOrder);
            }
            
            Log.d(TAG, "Synced " + pendingWorkOrders.size() + " pending work orders");
        } catch (Exception e) {
            Log.e(TAG, "Error syncing pending work orders", e);
        }
    }
    
    private void syncPendingCustomers() {
        try {
            List<Customer> pendingCustomers = database.customerDao().getPendingSyncCustomers();
            
            for (Customer customer : pendingCustomers) {
                // TODO: Implement actual API call to sync customer
                // For now, just mark as synced
                customer.setSyncStatus("synced");
                database.customerDao().updateCustomer(customer);
            }
            
            Log.d(TAG, "Synced " + pendingCustomers.size() + " pending customers");
        } catch (Exception e) {
            Log.e(TAG, "Error syncing pending customers", e);
        }
    }
    
    private void syncPendingUsers() {
        try {
            List<User> pendingUsers = database.userDao().getPendingSyncUsers();
            
            for (User user : pendingUsers) {
                // TODO: Implement actual API call to sync user
                // For now, just mark as synced
                user.setSyncStatus("synced");
                database.userDao().updateUser(user);
            }
            
            Log.d(TAG, "Synced " + pendingUsers.size() + " pending users");
        } catch (Exception e) {
            Log.e(TAG, "Error syncing pending users", e);
        }
    }
    
    // Background Sync
    public void scheduleBackgroundSync() {
        if (preferenceManager.isBackgroundSyncEnabled() && networkUtils.isNetworkAvailable()) {
            executorService.execute(() -> {
                try {
                    Thread.sleep(5000); // Wait 5 seconds
                    syncData();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Background sync interrupted", e);
                }
            });
        }
    }
    
    // Data Validation
    public boolean validateOfflineData() {
        try {
            // Check if essential data is available
            int workOrderCount = database.workOrderDao().getWorkOrderCount();
            int customerCount = database.customerDao().getCustomerCountSync();
            int userCount = database.userDao().getUserCount();
            
            Log.d(TAG, "Offline data validation - Work Orders: " + workOrderCount + 
                      ", Customers: " + customerCount + ", Users: " + userCount);
            
            return workOrderCount > 0 && customerCount > 0 && userCount > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error validating offline data", e);
            return false;
        }
    }
    
    // Cache Management
    public void clearCache() {
        executorService.execute(() -> {
            try {
                // Clear old data (keep last 30 days)
                long thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L);
                
                database.workOrderDao().deleteOldWorkOrders(thirtyDaysAgo);
                database.customerDao().deleteOldCustomers(thirtyDaysAgo);
                
                Log.d(TAG, "Cache cleared successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error clearing cache", e);
            }
        });
    }
    
    public void exportOfflineData() {
        executorService.execute(() -> {
            try {
                // TODO: Implement offline data export functionality
                Log.d(TAG, "Offline data export started");
            } catch (Exception e) {
                Log.e(TAG, "Error exporting offline data", e);
            }
        });
    }
    
    public void importOfflineData() {
        executorService.execute(() -> {
            try {
                // TODO: Implement offline data import functionality
                Log.d(TAG, "Offline data import started");
            } catch (Exception e) {
                Log.e(TAG, "Error importing offline data", e);
            }
        });
    }
    
    // Network State Management
    public void onNetworkStateChanged(boolean isConnected) {
        if (isConnected && !isOfflineMode) {
            // Network is back, sync pending data
            syncData();
        }
    }
    
    // Utility Methods
    public long getLastSyncTime() {
        return preferenceManager.getLastSyncTime();
    }
    
    public boolean hasPendingSync() {
        try {
            int pendingWorkOrders = database.workOrderDao().getPendingSyncWorkOrderCount();
            int pendingCustomers = database.customerDao().getPendingSyncCustomerCount();
            int pendingUsers = database.userDao().getPendingSyncUserCount();
            
            return (pendingWorkOrders + pendingCustomers + pendingUsers) > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error checking pending sync", e);
            return false;
        }
    }
    
    public int getPendingSyncCount() {
        try {
            int pendingWorkOrders = database.workOrderDao().getPendingSyncWorkOrderCount();
            int pendingCustomers = database.customerDao().getPendingSyncCustomerCount();
            int pendingUsers = database.userDao().getPendingSyncUserCount();
            
            return pendingWorkOrders + pendingCustomers + pendingUsers;
        } catch (Exception e) {
            Log.e(TAG, "Error getting pending sync count", e);
            return 0;
        }
    }
    
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
} 