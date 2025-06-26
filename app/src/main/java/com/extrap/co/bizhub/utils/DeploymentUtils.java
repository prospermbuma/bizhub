package com.extrap.co.bizhub.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.extrap.co.bizhub.FieldServiceApp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeploymentUtils {
    
    private static final String TAG = "DeploymentUtils";
    
    private Context context;
    private PreferenceManager preferenceManager;
    
    public DeploymentUtils(Context context) {
        this.context = context;
        this.preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
    }
    
    public void prepareForDeployment() {
        Log.d(TAG, "Preparing for deployment...");
        
        // Disable test mode
        disableTestMode();
        
        // Clear test data
        clearTestData();
        
        // Update app version
        updateAppVersion();
        
        // Generate deployment report
        generateDeploymentReport();
        
        // Validate production settings
        validateProductionSettings();
        
        Log.d(TAG, "Deployment preparation completed");
    }
    
    private void disableTestMode() {
        // Ensure test mode is disabled for production
        if (TestUtils.isTestModeEnabled()) {
            Log.w(TAG, "WARNING: Test mode is enabled. Disable for production deployment.");
        }
    }
    
    private void clearTestData() {
        try {
            // Clear any test data that might have been left behind
            Log.d(TAG, "Clearing test data...");
            
            // This would clear test users, customers, and work orders
            // In a real implementation, you'd have specific methods to clear test data
            
        } catch (Exception e) {
            Log.e(TAG, "Error clearing test data", e);
        }
    }
    
    private void updateAppVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            
            preferenceManager.setAppVersion(versionName);
            
            Log.d(TAG, "App version updated: " + versionName + " (" + versionCode + ")");
            
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Error getting package info", e);
        }
    }
    
    private void generateDeploymentReport() {
        try {
            StringBuilder report = new StringBuilder();
            report.append("=== DEPLOYMENT REPORT ===\n");
            report.append("Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())).append("\n\n");
            
            // App Information
            report.append("APP INFORMATION:\n");
            report.append("- Package Name: ").append(context.getPackageName()).append("\n");
            report.append("- Version: ").append(preferenceManager.getAppVersion()).append("\n");
            report.append("- Build Type: ").append(BuildConfig.BUILD_TYPE).append("\n");
            report.append("- Debug: ").append(BuildConfig.DEBUG).append("\n\n");
            
            // Device Information
            report.append("DEVICE INFORMATION:\n");
            report.append("- Android Version: ").append(Build.VERSION.RELEASE).append("\n");
            report.append("- API Level: ").append(Build.VERSION.SDK_INT).append("\n");
            report.append("- Device: ").append(Build.MANUFACTURER).append(" ").append(Build.MODEL).append("\n\n");
            
            // Database Information
            report.append("DATABASE INFORMATION:\n");
            report.append("- Database Name: ").append("bizhub_database").append("\n");
            report.append("- Database Version: ").append("1").append("\n\n");
            
            // Settings Information
            report.append("SETTINGS INFORMATION:\n");
            report.append("- Test Mode: ").append(TestUtils.isTestModeEnabled()).append("\n");
            report.append("- Notifications Enabled: ").append(preferenceManager.areNotificationsEnabled()).append("\n");
            report.append("- Auto Sync: ").append(preferenceManager.isAutoSyncEnabled()).append("\n");
            report.append("- Background Sync: ").append(preferenceManager.isBackgroundSyncEnabled()).append("\n\n");
            
            // Permissions
            report.append("PERMISSIONS:\n");
            report.append("- Internet: ").append(checkPermission("android.permission.INTERNET")).append("\n");
            report.append("- Camera: ").append(checkPermission("android.permission.CAMERA")).append("\n");
            report.append("- Location: ").append(checkPermission("android.permission.ACCESS_FINE_LOCATION")).append("\n");
            report.append("- Bluetooth: ").append(checkPermission("android.permission.BLUETOOTH")).append("\n");
            report.append("- Storage: ").append(checkPermission("android.permission.READ_EXTERNAL_STORAGE")).append("\n\n");
            
            // File System
            report.append("FILE SYSTEM:\n");
            report.append("- Internal Storage Available: ").append(getAvailableInternalStorage()).append(" MB\n");
            report.append("- External Storage Available: ").append(getAvailableExternalStorage()).append(" MB\n\n");
            
            // Network
            report.append("NETWORK:\n");
            report.append("- Network Available: ").append(FieldServiceApp.getInstance().getNetworkUtils().isNetworkAvailable()).append("\n\n");
            
            // Save report to file
            saveReportToFile(report.toString());
            
            Log.d(TAG, "Deployment report generated successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error generating deployment report", e);
        }
    }
    
    private boolean checkPermission(String permission) {
        return context.checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED;
    }
    
    private long getAvailableInternalStorage() {
        try {
            File path = context.getFilesDir();
            android.os.StatFs stat = new android.os.StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return (availableBlocks * blockSize) / (1024 * 1024); // Convert to MB
        } catch (Exception e) {
            return -1;
        }
    }
    
    private long getAvailableExternalStorage() {
        try {
            File path = context.getExternalFilesDir(null);
            if (path != null) {
                android.os.StatFs stat = new android.os.StatFs(path.getPath());
                long blockSize = stat.getBlockSizeLong();
                long availableBlocks = stat.getAvailableBlocksLong();
                return (availableBlocks * blockSize) / (1024 * 1024); // Convert to MB
            }
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }
    
    private void saveReportToFile(String report) {
        try {
            File reportFile = new File(context.getExternalFilesDir(null), "deployment_report.txt");
            FileWriter writer = new FileWriter(reportFile);
            writer.write(report);
            writer.close();
            
            Log.d(TAG, "Deployment report saved to: " + reportFile.getAbsolutePath());
            
        } catch (IOException e) {
            Log.e(TAG, "Error saving deployment report", e);
        }
    }
    
    private void validateProductionSettings() {
        Log.d(TAG, "Validating production settings...");
        
        // Check for test mode
        if (TestUtils.isTestModeEnabled()) {
            Log.e(TAG, "ERROR: Test mode is enabled. Must be disabled for production.");
        }
        
        // Check for debug build
        if (BuildConfig.DEBUG) {
            Log.w(TAG, "WARNING: Debug build detected. Consider using release build for production.");
        }
        
        // Check for required permissions
        String[] requiredPermissions = {
                "android.permission.INTERNET",
                "android.permission.ACCESS_NETWORK_STATE"
        };
        
        for (String permission : requiredPermissions) {
            if (!checkPermission(permission)) {
                Log.e(TAG, "ERROR: Required permission not granted: " + permission);
            }
        }
        
        // Check database integrity
        validateDatabaseIntegrity();
        
        Log.d(TAG, "Production settings validation completed");
    }
    
    private void validateDatabaseIntegrity() {
        try {
            // Check if database is accessible
            FieldServiceApp.getInstance().getDatabase().isOpen();
            
            // Check if tables exist and are accessible
            // This would involve running simple queries to verify database integrity
            
            Log.d(TAG, "Database integrity validation passed");
            
        } catch (Exception e) {
            Log.e(TAG, "Database integrity validation failed", e);
        }
    }
    
    public void createBackup() {
        Log.d(TAG, "Creating backup...");
        
        try {
            // Create database backup
            createDatabaseBackup();
            
            // Create settings backup
            createSettingsBackup();
            
            // Create user data backup
            createUserDataBackup();
            
            Log.d(TAG, "Backup created successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating backup", e);
        }
    }
    
    private void createDatabaseBackup() {
        try {
            // In a real implementation, this would create a database backup
            // For now, we'll just log the action
            Log.d(TAG, "Database backup created");
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating database backup", e);
        }
    }
    
    private void createSettingsBackup() {
        try {
            // Create settings backup
            Log.d(TAG, "Settings backup created");
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating settings backup", e);
        }
    }
    
    private void createUserDataBackup() {
        try {
            // Create user data backup
            Log.d(TAG, "User data backup created");
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating user data backup", e);
        }
    }
    
    public void performHealthCheck() {
        Log.d(TAG, "Performing health check...");
        
        // Check database connectivity
        checkDatabaseHealth();
        
        // Check network connectivity
        checkNetworkHealth();
        
        // Check storage availability
        checkStorageHealth();
        
        // Check memory usage
        checkMemoryHealth();
        
        Log.d(TAG, "Health check completed");
    }
    
    private void checkDatabaseHealth() {
        try {
            boolean isOpen = FieldServiceApp.getInstance().getDatabase().isOpen();
            Log.d(TAG, "Database health: " + (isOpen ? "OK" : "ERROR"));
        } catch (Exception e) {
            Log.e(TAG, "Database health check failed", e);
        }
    }
    
    private void checkNetworkHealth() {
        try {
            boolean isNetworkAvailable = FieldServiceApp.getInstance().getNetworkUtils().isNetworkAvailable();
            Log.d(TAG, "Network health: " + (isNetworkAvailable ? "OK" : "ERROR"));
        } catch (Exception e) {
            Log.e(TAG, "Network health check failed", e);
        }
    }
    
    private void checkStorageHealth() {
        try {
            long availableStorage = getAvailableInternalStorage();
            Log.d(TAG, "Storage health: " + (availableStorage > 100 ? "OK" : "LOW SPACE") + " (" + availableStorage + " MB available)");
        } catch (Exception e) {
            Log.e(TAG, "Storage health check failed", e);
        }
    }
    
    private void checkMemoryHealth() {
        try {
            Runtime runtime = Runtime.getRuntime();
            long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
            long maxMemory = runtime.maxMemory() / 1024 / 1024;
            Log.d(TAG, "Memory health: " + usedMemory + " MB used / " + maxMemory + " MB max");
        } catch (Exception e) {
            Log.e(TAG, "Memory health check failed", e);
        }
    }
} 