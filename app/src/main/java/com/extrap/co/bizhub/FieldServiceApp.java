package com.extrap.co.bizhub;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.extrap.co.bizhub.data.AppDatabase;
import com.extrap.co.bizhub.utils.NetworkUtils;
import com.extrap.co.bizhub.utils.PreferenceManager;

public class FieldServiceApp extends Application {
    
    private static FieldServiceApp instance;
    private AppDatabase database;
    private PreferenceManager preferenceManager;
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        
        // Initialize database
        database = AppDatabase.getInstance(this);
        
        // Initialize preference manager
        preferenceManager = new PreferenceManager(this);
        
        // Create notification channels
        createNotificationChannels();
        
        // Initialize network monitoring
        NetworkUtils.init(this);
    }
    
    public static FieldServiceApp getInstance() {
        return instance;
    }
    
    public AppDatabase getDatabase() {
        return database;
    }
    
    public PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }
    
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            
            // Work Order Notifications
            NotificationChannel workOrderChannel = new NotificationChannel(
                "work_orders",
                "Work Orders",
                NotificationManager.IMPORTANCE_HIGH
            );
            workOrderChannel.setDescription("Notifications for new work orders and updates");
            
            // General Notifications
            NotificationChannel generalChannel = new NotificationChannel(
                "general",
                "General",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            generalChannel.setDescription("General app notifications");
            
            // Sync Notifications
            NotificationChannel syncChannel = new NotificationChannel(
                "sync",
                "Sync",
                NotificationManager.IMPORTANCE_LOW
            );
            syncChannel.setDescription("Data synchronization notifications");
            
            notificationManager.createNotificationChannel(workOrderChannel);
            notificationManager.createNotificationChannel(generalChannel);
            notificationManager.createNotificationChannel(syncChannel);
        }
    }
} 