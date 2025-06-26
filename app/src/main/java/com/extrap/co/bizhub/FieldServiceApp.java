package com.extrap.co.bizhub;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.extrap.co.bizhub.data.AppDatabase;
import com.extrap.co.bizhub.data.entities.Customer;
import com.extrap.co.bizhub.data.entities.User;
import com.extrap.co.bizhub.data.entities.WorkOrder;
import com.extrap.co.bizhub.utils.NetworkUtils;
import com.extrap.co.bizhub.utils.PreferenceManager;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FieldServiceApp extends Application {
    
    private static FieldServiceApp instance;
    private AppDatabase database;
    private PreferenceManager preferenceManager;
    private NetworkUtils networkUtils;
    private ExecutorService executorService;
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        
        // Initialize managers
        preferenceManager = new PreferenceManager(this);
        networkUtils = new NetworkUtils(this);
        executorService = Executors.newFixedThreadPool(4);
        
        // Initialize database
        database = AppDatabase.getInstance(this);
        
        // Initialize sample data
        initializeSampleData();
        
        // Create notification channels
        createNotificationChannels();
        
        // Initialize network monitoring
        NetworkUtils.init(this);
    }
    
    private void initializeSampleData() {
        executorService.execute(() -> {
            // Check if sample data already exists
            if (database.userDao().getAllUsers().isEmpty()) {
                // Create sample users
                User adminUser = new User("admin@bizhub.com", "password123", "John", "Doe", "+1234567890", "admin");
                long adminId = database.userDao().insertUser(adminUser);
                
                User technician = new User("tech@bizhub.com", "password123", "Mike", "Smith", "+1234567891", "technician");
                long techId = database.userDao().insertUser(technician);
                
                // Create sample customers
                Customer customer1 = new Customer("John Smith", "john.smith@email.com", "+1234567892", "123 Main St, City, State");
                customer1.setCompany("AC Repair Services");
                customer1.setPhone("+1234567892");
                customer1.setPremium(true);
                customer1.setDateAdded(System.currentTimeMillis());
                long customer1Id = database.customerDao().insertCustomer(customer1);
                
                Customer customer2 = new Customer("Sarah Johnson", "sarah.j@email.com", "+1234567893", "456 Oak Ave, City, State");
                customer2.setCompany("Plumbing Solutions");
                customer2.setPhone("+1234567893");
                customer2.setPremium(false);
                customer2.setDateAdded(System.currentTimeMillis() - 86400000);
                long customer2Id = database.customerDao().insertCustomer(customer2);
                
                Customer customer3 = new Customer("Bob Wilson", "bob.w@email.com", "+1234567894", "789 Pine Rd, City, State");
                customer3.setCompany("Electrical Works");
                customer3.setPhone("+1234567894");
                customer3.setPremium(true);
                customer3.setDateAdded(System.currentTimeMillis() - 172800000);
                long customer3Id = database.customerDao().insertCustomer(customer3);
                
                Customer customer4 = new Customer("Lisa Brown", "lisa.b@email.com", "+1234567895", "321 Elm St, City, State");
                customer4.setCompany("HVAC Systems");
                customer4.setPhone("+1234567895");
                customer4.setPremium(false);
                customer4.setDateAdded(System.currentTimeMillis() - 259200000);
                long customer4Id = database.customerDao().insertCustomer(customer4);
                
                // Create sample work orders
                Calendar calendar = Calendar.getInstance();
                long now = calendar.getTimeInMillis();
                
                // Today's work orders
                WorkOrder workOrder1 = new WorkOrder("WO-001", customer1Id, "AC Repair", "AC not cooling properly", "high", "pending", now, now, techId);
                database.workOrderDao().insertWorkOrder(workOrder1);
                
                WorkOrder workOrder2 = new WorkOrder("WO-002", customer2Id, "Plumbing", "Leaky faucet repair", "medium", "in_progress", now - 3600000, now - 3600000, techId);
                database.workOrderDao().insertWorkOrder(workOrder2);
                
                WorkOrder workOrder3 = new WorkOrder("WO-003", customer3Id, "Electrical", "Light fixture installation", "low", "completed", now - 7200000, now - 7200000, techId);
                database.workOrderDao().insertWorkOrder(workOrder3);
                
                // Yesterday's work orders
                long yesterday = now - (24 * 60 * 60 * 1000);
                WorkOrder workOrder4 = new WorkOrder("WO-004", customer4Id, "HVAC", "Furnace maintenance", "medium", "completed", yesterday, yesterday, techId);
                database.workOrderDao().insertWorkOrder(workOrder4);
                
                WorkOrder workOrder5 = new WorkOrder("WO-005", customer1Id, "AC Repair", "Filter replacement", "low", "completed", yesterday - 3600000, yesterday - 3600000, techId);
                database.workOrderDao().insertWorkOrder(workOrder5);
                
                // Pending work orders
                WorkOrder workOrder6 = new WorkOrder("WO-006", customer2Id, "Plumbing", "Drain cleaning", "high", "pending", now + 86400000, now + 86400000, techId);
                database.workOrderDao().insertWorkOrder(workOrder6);
                
                WorkOrder workOrder7 = new WorkOrder("WO-007", customer3Id, "Electrical", "Circuit breaker replacement", "urgent", "pending", now + 172800000, now + 172800000, techId);
                database.workOrderDao().insertWorkOrder(workOrder7);
            }
        });
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
    
    public NetworkUtils getNetworkUtils() {
        return networkUtils;
    }
    
    public ExecutorService getExecutorService() {
        return executorService;
    }
    
    public static Context getContext() {
        return instance.getApplicationContext();
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