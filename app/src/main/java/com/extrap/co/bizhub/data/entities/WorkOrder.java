package com.extrap.co.bizhub.data.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "work_orders")
public class WorkOrder {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String workOrderNumber;
    private int customerId;
    private int assignedTechnicianId;
    private String serviceType; // repair, maintenance, installation, inspection
    private String priority; // low, medium, high, urgent
    private String status; // pending, in_progress, completed, cancelled
    private String description;
    private String location;
    private double latitude;
    private double longitude;
    private long scheduledDate;
    private long dueDate;
    private long startTime;
    private long completionTime;
    private double estimatedDuration; // in hours
    private double actualDuration; // in hours
    private double estimatedCost;
    private double actualCost;
    private String notes;
    private String customerSignature;
    private boolean isUrgent;
    private boolean isRecurring;
    private String recurringPattern; // daily, weekly, monthly, yearly
    private long createdAt;
    private long updatedAt;
    private String syncStatus = "synced"; // synced, pending, error
    
    // Constructors
    public WorkOrder() {}
    
    @Ignore
    public WorkOrder(String workOrderNumber, int customerId, String serviceType, String priority, String description) {
        this.workOrderNumber = workOrderNumber;
        this.customerId = customerId;
        this.serviceType = serviceType;
        this.priority = priority;
        this.description = description;
        this.status = "pending";
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    @Ignore
    public WorkOrder(String workOrderNumber, long customerId, String serviceType, String description, String priority, String status, long scheduledDate, long dueDate, long assignedTechnicianId) {
        this.workOrderNumber = workOrderNumber;
        this.customerId = (int) customerId;
        this.serviceType = serviceType;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.scheduledDate = scheduledDate;
        this.dueDate = dueDate;
        this.assignedTechnicianId = (int) assignedTechnicianId;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getWorkOrderNumber() {
        return workOrderNumber;
    }
    
    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public int getAssignedTechnicianId() {
        return assignedTechnicianId;
    }
    
    public void setAssignedTechnicianId(int assignedTechnicianId) {
        this.assignedTechnicianId = assignedTechnicianId;
    }
    
    public String getServiceType() {
        return serviceType;
    }
    
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public long getScheduledDate() {
        return scheduledDate;
    }
    
    public void setScheduledDate(long scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
    
    public long getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public long getCompletionTime() {
        return completionTime;
    }
    
    public void setCompletionTime(long completionTime) {
        this.completionTime = completionTime;
    }
    
    public double getEstimatedDuration() {
        return estimatedDuration;
    }
    
    public void setEstimatedDuration(double estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }
    
    public double getActualDuration() {
        return actualDuration;
    }
    
    public void setActualDuration(double actualDuration) {
        this.actualDuration = actualDuration;
    }
    
    public double getEstimatedCost() {
        return estimatedCost;
    }
    
    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }
    
    public double getActualCost() {
        return actualCost;
    }
    
    public void setActualCost(double actualCost) {
        this.actualCost = actualCost;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getCustomerSignature() {
        return customerSignature;
    }
    
    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
    }
    
    public boolean isUrgent() {
        return isUrgent;
    }
    
    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }
    
    public boolean isRecurring() {
        return isRecurring;
    }
    
    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }
    
    public String getRecurringPattern() {
        return recurringPattern;
    }
    
    public void setRecurringPattern(String recurringPattern) {
        this.recurringPattern = recurringPattern;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public long getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getSyncStatus() {
        return syncStatus;
    }
    
    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }
    
    // Helper methods
    public boolean isOverdue() {
        return dueDate > 0 && System.currentTimeMillis() > dueDate && !status.equals("completed");
    }
    
    public boolean isInProgress() {
        return status.equals("in_progress");
    }
    
    public boolean isCompleted() {
        return status.equals("completed");
    }
    
    public boolean isPending() {
        return status.equals("pending");
    }
    
    public boolean isHighPriority() {
        return "high".equals(priority) || "urgent".equals(priority);
    }

    // Additional methods for compatibility
    public String getCustomerName() {
        // This would typically be populated from a join query
        return "Customer " + customerId;
    }

    public void setCustomerName(String customerName) {
        // This is a placeholder - in a real app, you'd update the customer relationship
    }

    public void setAssignedTo(long assignedTo) {
        this.assignedTechnicianId = (int) assignedTo;
    }
} 