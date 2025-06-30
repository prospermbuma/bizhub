package com.extrap.co.bizhub.data.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "documents")
public class Document {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String fileName;
    private String filePath;
    private String mimeType;
    private String source; // camera, gallery, file
    private long fileSize;
    private long createdAt;
    private String description;
    private int workOrderId; // Associated work order
    private int customerId; // Associated customer
    private String syncStatus = "synced"; // synced, pending, error
    
    // Constructors
    public Document() {}
    
    @Ignore
    public Document(String fileName, String filePath, String mimeType, String source) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.mimeType = mimeType;
        this.source = source;
        this.createdAt = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getWorkOrderId() {
        return workOrderId;
    }
    
    public void setWorkOrderId(int workOrderId) {
        this.workOrderId = workOrderId;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public String getSyncStatus() {
        return syncStatus;
    }
    
    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    // Additional utility methods
    public String getFileSizeFormatted() {
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            return String.format("%.1f KB", fileSize / 1024.0);
        } else {
            return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
        }
    }

    public boolean isImage() {
        return mimeType != null && (mimeType.equals("image/jpeg") || mimeType.equals("image/png") || mimeType.equals("image/gif"));
    }

    public boolean isPdf() {
        return mimeType != null && mimeType.equals("application/pdf");
    }

    public boolean isVideo() {
        return mimeType != null && (mimeType.equals("video/mp4") || mimeType.equals("video/avi") || mimeType.equals("video/mov"));
    }
} 