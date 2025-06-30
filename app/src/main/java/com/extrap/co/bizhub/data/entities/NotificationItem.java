package com.extrap.co.bizhub.data.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class NotificationItem {
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String title;
    private String message;
    private String type; // work_order, alert, sync, general
    private long referenceId; // ID of related item (work order, etc.)
    private boolean isRead;
    private long timestamp;
    private String priority; // low, medium, high, urgent
    private String action; // action to take when clicked
    
    // Constructors
    public NotificationItem() {}
    
    @Ignore
    public NotificationItem(String title, String message, String type) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = false;
        this.timestamp = System.currentTimeMillis();
        this.priority = "medium";
    }
    
    @Ignore
    public NotificationItem(String title, String message, String type, long referenceId) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.referenceId = referenceId;
        this.isRead = false;
        this.timestamp = System.currentTimeMillis();
        this.priority = "medium";
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public long getReferenceId() {
        return referenceId;
    }
    
    public void setReferenceId(long referenceId) {
        this.referenceId = referenceId;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean read) {
        isRead = read;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    // Helper methods
    public boolean isHighPriority() {
        return "high".equals(priority) || "urgent".equals(priority);
    }
    
    public boolean isWorkOrderNotification() {
        return "work_order".equals(type);
    }
    
    public boolean isAlertNotification() {
        return "alert".equals(type);
    }
    
    public boolean isSyncNotification() {
        return "sync".equals(type);
    }
    
    public boolean isGeneralNotification() {
        return "general".equals(type);
    }
} 