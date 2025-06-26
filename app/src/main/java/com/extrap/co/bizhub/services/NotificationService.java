package com.extrap.co.bizhub.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.dao.WorkOrderDao;
import com.extrap.co.bizhub.data.entities.WorkOrder;
import com.extrap.co.bizhub.utils.NotificationManager;
import com.extrap.co.bizhub.utils.PreferenceManager;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationService extends Service {
    
    private static final String TAG = "NotificationService";
    private static final long CHECK_INTERVAL = 15; // minutes
    
    private NotificationManager notificationManager;
    private PreferenceManager preferenceManager;
    private WorkOrderDao workOrderDao;
    private ScheduledExecutorService scheduler;
    
    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = new NotificationManager(this);
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        workOrderDao = FieldServiceApp.getInstance().getDatabase().workOrderDao();
        scheduler = Executors.newScheduledThreadPool(1);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "NotificationService started");
        
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case "CHECK_OVERDUE_WORK_ORDERS":
                        checkOverdueWorkOrders();
                        break;
                    case "CHECK_SCHEDULED_WORK_ORDERS":
                        checkScheduledWorkOrders();
                        break;
                    case "SYNC_NOTIFICATION":
                        String message = intent.getStringExtra("message");
                        boolean isSuccess = intent.getBooleanExtra("is_success", false);
                        notificationManager.showSyncNotification(message, isSuccess);
                        break;
                }
            }
        }
        
        // Schedule periodic checks
        schedulePeriodicChecks();
        
        return START_STICKY;
    }
    
    private void schedulePeriodicChecks() {
        scheduler.scheduleAtFixedRate(() -> {
            if (preferenceManager.areNotificationsEnabled()) {
                checkOverdueWorkOrders();
                checkScheduledWorkOrders();
            }
        }, CHECK_INTERVAL, CHECK_INTERVAL, TimeUnit.MINUTES);
    }
    
    private void checkOverdueWorkOrders() {
        try {
            long currentTime = System.currentTimeMillis();
            List<WorkOrder> overdueWorkOrders = workOrderDao.getOverdueWorkOrders(currentTime);
            
            for (WorkOrder workOrder : overdueWorkOrders) {
                notificationManager.showOverdueWorkOrderAlert(workOrder);
            }
            
            if (!overdueWorkOrders.isEmpty()) {
                Log.d(TAG, "Found " + overdueWorkOrders.size() + " overdue work orders");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking overdue work orders", e);
        }
    }
    
    private void checkScheduledWorkOrders() {
        try {
            long currentTime = System.currentTimeMillis();
            long startOfDay = getStartOfDay(currentTime);
            long endOfDay = getEndOfDay(currentTime);
            
            List<WorkOrder> scheduledWorkOrders = workOrderDao.getWorkOrdersByDateRange(startOfDay, endOfDay);
            
            for (WorkOrder workOrder : scheduledWorkOrders) {
                if ("pending".equals(workOrder.getStatus())) {
                    // Check if it's time to send a reminder (e.g., 1 hour before scheduled time)
                    long scheduledTime = workOrder.getScheduledDate();
                    long reminderTime = scheduledTime - TimeUnit.HOURS.toMillis(1);
                    
                    if (currentTime >= reminderTime && currentTime <= scheduledTime) {
                        notificationManager.showWorkOrderReminder(workOrder);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error checking scheduled work orders", e);
        }
    }
    
    private long getStartOfDay(long timestamp) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    
    private long getEndOfDay(long timestamp) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23);
        calendar.set(java.util.Calendar.MINUTE, 59);
        calendar.set(java.util.Calendar.SECOND, 59);
        calendar.set(java.util.Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
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
        Log.d(TAG, "NotificationService destroyed");
    }
} 