package com.extrap.co.bizhub.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.dao.NotificationDao;
import com.extrap.co.bizhub.data.entities.NotificationItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationsViewModel extends AndroidViewModel {
    
    private NotificationDao notificationDao;
    private ExecutorService executorService;
    
    private MutableLiveData<List<NotificationItem>> filteredNotifications = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<String> successMessage = new MutableLiveData<>();
    
    public NotificationsViewModel(Application application) {
        super(application);
        notificationDao = FieldServiceApp.getInstance().getDatabase().notificationDao();
        executorService = Executors.newFixedThreadPool(4);
    }
    
    public LiveData<List<NotificationItem>> getAllNotifications() {
        return notificationDao.getAllNotifications();
    }
    
    public LiveData<List<NotificationItem>> getFilteredNotifications() {
        return filteredNotifications;
    }
    
    public LiveData<Integer> getUnreadCount() {
        return notificationDao.getUnreadCount();
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }
    
    public void addNotification(NotificationItem notification) {
        executorService.execute(() -> {
            try {
                long id = notificationDao.insertNotification(notification);
                if (id > 0) {
                    successMessage.postValue("Notification added");
                } else {
                    errorMessage.postValue("Failed to add notification");
                }
            } catch (Exception e) {
                errorMessage.postValue("Error adding notification: " + e.getMessage());
            }
        });
    }
    
    public void markAsRead(long notificationId) {
        executorService.execute(() -> {
            try {
                notificationDao.markAsRead(notificationId);
                successMessage.postValue("Notification marked as read");
            } catch (Exception e) {
                errorMessage.postValue("Error marking notification as read: " + e.getMessage());
            }
        });
    }
    
    public void markAllAsRead() {
        executorService.execute(() -> {
            try {
                notificationDao.markAllAsRead();
                successMessage.postValue("All notifications marked as read");
            } catch (Exception e) {
                errorMessage.postValue("Error marking notifications as read: " + e.getMessage());
            }
        });
    }
    
    public void deleteNotification(long notificationId) {
        executorService.execute(() -> {
            try {
                notificationDao.deleteNotificationById(notificationId);
                successMessage.postValue("Notification deleted");
            } catch (Exception e) {
                errorMessage.postValue("Error deleting notification: " + e.getMessage());
            }
        });
    }
    
    public void clearAllNotifications() {
        executorService.execute(() -> {
            try {
                notificationDao.deleteAllNotifications();
                successMessage.postValue("All notifications cleared");
            } catch (Exception e) {
                errorMessage.postValue("Error clearing notifications: " + e.getMessage());
            }
        });
    }
    
    public void deleteReadNotifications() {
        executorService.execute(() -> {
            try {
                notificationDao.deleteReadNotifications();
                successMessage.postValue("Read notifications deleted");
            } catch (Exception e) {
                errorMessage.postValue("Error deleting read notifications: " + e.getMessage());
            }
        });
    }
    
    public void deleteOldNotifications(long olderThan) {
        executorService.execute(() -> {
            try {
                notificationDao.deleteOldNotifications(olderThan);
                successMessage.postValue("Old notifications deleted");
            } catch (Exception e) {
                errorMessage.postValue("Error deleting old notifications: " + e.getMessage());
            }
        });
    }
    
    public LiveData<List<NotificationItem>> getNotificationsByType(String type) {
        return notificationDao.getNotificationsByType(type);
    }
    
    public LiveData<List<NotificationItem>> getUnreadNotifications() {
        return notificationDao.getUnreadNotifications();
    }
    
    public LiveData<Integer> getUnreadCountByType(String type) {
        return notificationDao.getUnreadCountByType(type);
    }
    
    public LiveData<List<NotificationItem>> getHighPriorityUnreadNotifications() {
        return notificationDao.getHighPriorityUnreadNotifications();
    }
    
    public LiveData<List<NotificationItem>> getWorkOrderNotifications(long workOrderId) {
        return notificationDao.getWorkOrderNotifications(workOrderId);
    }
    
    public LiveData<List<NotificationItem>> getRecentAlerts() {
        return notificationDao.getRecentAlerts();
    }
    
    public LiveData<List<NotificationItem>> getRecentSyncNotifications() {
        return notificationDao.getRecentSyncNotifications();
    }
    
    public void createWorkOrderNotification(String title, String message, long workOrderId) {
        NotificationItem notification = new NotificationItem(title, message, "work_order", workOrderId);
        notification.setPriority("medium");
        addNotification(notification);
    }
    
    public void createAlertNotification(String title, String message) {
        NotificationItem notification = new NotificationItem(title, message, "alert");
        notification.setPriority("high");
        addNotification(notification);
    }
    
    public void createSyncNotification(String title, String message, boolean isSuccess) {
        NotificationItem notification = new NotificationItem(title, message, "sync");
        notification.setPriority(isSuccess ? "low" : "medium");
        addNotification(notification);
    }
    
    public void createGeneralNotification(String title, String message) {
        NotificationItem notification = new NotificationItem(title, message, "general");
        notification.setPriority("medium");
        addNotification(notification);
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
} 