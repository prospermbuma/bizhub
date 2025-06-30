package com.extrap.co.bizhub.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.extrap.co.bizhub.data.entities.NotificationItem;

import java.util.List;

@Dao
public interface NotificationDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNotification(NotificationItem notification);
    
    @Update
    void updateNotification(NotificationItem notification);
    
    @Delete
    void deleteNotification(NotificationItem notification);
    
    @Query("SELECT * FROM notifications WHERE id = :notificationId")
    LiveData<NotificationItem> getNotificationById(int notificationId);
    
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    LiveData<List<NotificationItem>> getAllNotifications();
    
    @Query("SELECT * FROM notifications WHERE isRead = 0 ORDER BY timestamp DESC")
    LiveData<List<NotificationItem>> getUnreadNotifications();
    
    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    int getUnreadNotificationCount();
    
    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    void markAsRead(int notificationId);
    
    @Query("UPDATE notifications SET isRead = 1")
    void markAllAsRead();
    
    @Query("DELETE FROM notifications WHERE id = :notificationId")
    void deleteNotificationById(int notificationId);
    
    @Query("DELETE FROM notifications WHERE timestamp < :timestamp")
    void deleteOldNotifications(long timestamp);
    
    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    int getUnreadCount();
    
    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0")
    LiveData<Integer> getUnreadCountLive();
    
    @Query("SELECT * FROM notifications WHERE type = :type")
    LiveData<List<NotificationItem>> getNotificationsByType(String type);
    
    @Query("SELECT COUNT(*) FROM notifications WHERE isRead = 0 AND type = :type")
    int getUnreadCountByType(String type);
    
    @Query("SELECT * FROM notifications WHERE isRead = 0 AND priority = 'high' ORDER BY timestamp DESC")
    LiveData<List<NotificationItem>> getHighPriorityUnreadNotifications();
    
    @Query("SELECT * FROM notifications WHERE referenceId = :workOrderId ORDER BY timestamp DESC")
    LiveData<List<NotificationItem>> getWorkOrderNotifications(long workOrderId);
    
    @Query("SELECT * FROM notifications WHERE type = 'alert' ORDER BY timestamp DESC LIMIT 10")
    LiveData<List<NotificationItem>> getRecentAlerts();
    
    @Query("SELECT * FROM notifications WHERE type = 'sync' ORDER BY timestamp DESC LIMIT 10")
    LiveData<List<NotificationItem>> getRecentSyncNotifications();
    
    @Query("DELETE FROM notifications")
    void deleteAllNotifications();
    
    @Query("DELETE FROM notifications WHERE isRead = 1")
    void deleteReadNotifications();
} 