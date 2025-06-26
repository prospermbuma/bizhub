package com.extrap.co.bizhub.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.extrap.co.bizhub.data.entities.WorkOrder;

import java.util.List;

@Dao
public interface WorkOrderDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertWorkOrder(WorkOrder workOrder);
    
    @Update
    void updateWorkOrder(WorkOrder workOrder);
    
    @Delete
    void deleteWorkOrder(WorkOrder workOrder);
    
    @Query("SELECT * FROM work_orders WHERE id = :workOrderId")
    LiveData<WorkOrder> getWorkOrderById(int workOrderId);
    
    @Query("SELECT * FROM work_orders WHERE id = :workOrderId")
    WorkOrder getWorkOrderByIdSync(int workOrderId);
    
    @Query("SELECT * FROM work_orders WHERE workOrderNumber = :workOrderNumber")
    WorkOrder getWorkOrderByNumber(String workOrderNumber);
    
    @Query("SELECT * FROM work_orders WHERE assignedTechnicianId = :technicianId")
    LiveData<List<WorkOrder>> getWorkOrdersByTechnician(int technicianId);
    
    @Query("SELECT * FROM work_orders WHERE customerId = :customerId")
    LiveData<List<WorkOrder>> getWorkOrdersByCustomer(int customerId);
    
    @Query("SELECT * FROM work_orders WHERE status = :status")
    LiveData<List<WorkOrder>> getWorkOrdersByStatus(String status);
    
    @Query("SELECT * FROM work_orders WHERE priority = :priority")
    LiveData<List<WorkOrder>> getWorkOrdersByPriority(String priority);
    
    @Query("SELECT * FROM work_orders WHERE serviceType = :serviceType")
    LiveData<List<WorkOrder>> getWorkOrdersByServiceType(String serviceType);
    
    @Query("SELECT * FROM work_orders WHERE scheduledDate >= :startDate AND scheduledDate <= :endDate")
    LiveData<List<WorkOrder>> getWorkOrdersByDateRange(long startDate, long endDate);
    
    @Query("SELECT * FROM work_orders WHERE dueDate < :currentTime AND status != 'completed'")
    LiveData<List<WorkOrder>> getOverdueWorkOrders(long currentTime);
    
    @Query("SELECT * FROM work_orders WHERE status = 'pending' ORDER BY priority DESC, scheduledDate ASC")
    LiveData<List<WorkOrder>> getPendingWorkOrders();
    
    @Query("SELECT * FROM work_orders WHERE status = 'in_progress'")
    LiveData<List<WorkOrder>> getInProgressWorkOrders();
    
    @Query("SELECT * FROM work_orders WHERE status = 'completed' AND completionTime >= :startDate")
    LiveData<List<WorkOrder>> getCompletedWorkOrders(long startDate);
    
    @Query("SELECT * FROM work_orders ORDER BY createdAt DESC")
    LiveData<List<WorkOrder>> getAllWorkOrders();
    
    @Query("SELECT COUNT(*) FROM work_orders WHERE status = 'pending'")
    int getPendingWorkOrderCount();
    
    @Query("SELECT COUNT(*) FROM work_orders WHERE status = 'in_progress'")
    int getInProgressWorkOrderCount();
    
    @Query("SELECT COUNT(*) FROM work_orders WHERE status = 'completed' AND completionTime >= :startDate")
    int getCompletedWorkOrderCount(long startDate);
    
    @Query("SELECT COUNT(*) FROM work_orders WHERE priority = 'high' OR priority = 'urgent'")
    int getHighPriorityWorkOrderCount();
    
    @Query("UPDATE work_orders SET status = :status WHERE id = :workOrderId")
    void updateWorkOrderStatus(int workOrderId, String status);
    
    @Query("UPDATE work_orders SET assignedTechnicianId = :technicianId WHERE id = :workOrderId")
    void assignWorkOrder(int workOrderId, int technicianId);
    
    @Query("UPDATE work_orders SET startTime = :startTime WHERE id = :workOrderId")
    void startWorkOrder(int workOrderId, long startTime);
    
    @Query("UPDATE work_orders SET completionTime = :completionTime, actualDuration = :duration WHERE id = :workOrderId")
    void completeWorkOrder(int workOrderId, long completionTime, double duration);
    
    @Query("SELECT * FROM work_orders WHERE assignedTechnicianId = :technicianId AND status = 'pending' ORDER BY priority DESC, scheduledDate ASC")
    LiveData<List<WorkOrder>> getPendingWorkOrdersForTechnician(int technicianId);
    
    @Query("SELECT * FROM work_orders WHERE assignedTechnicianId = :technicianId AND status = 'in_progress'")
    LiveData<List<WorkOrder>> getInProgressWorkOrdersForTechnician(int technicianId);
    
    @Query("SELECT * FROM work_orders WHERE status = :status ORDER BY created_at DESC")
    List<WorkOrder> getWorkOrdersByStatus(String status);
    
    @Query("SELECT * FROM work_orders WHERE created_at BETWEEN :startDate AND :endDate ORDER BY created_at DESC")
    List<WorkOrder> getWorkOrdersByDateRange(long startDate, long endDate);
    
    @Query("SELECT * FROM work_orders WHERE status = :status AND created_at BETWEEN :startDate AND :endDate ORDER BY created_at DESC")
    List<WorkOrder> getWorkOrdersByStatusAndDate(String status, long startDate, long endDate);
    
    @Query("SELECT * FROM work_orders ORDER BY created_at DESC LIMIT :limit")
    List<WorkOrder> getRecentWorkOrders(int limit);
    
    @Query("SELECT * FROM work_orders WHERE assigned_to = :userId ORDER BY created_at DESC")
    List<WorkOrder> getWorkOrdersByUser(long userId);
    
    @Query("SELECT * FROM work_orders WHERE customer_id = :customerId ORDER BY created_at DESC")
    List<WorkOrder> getWorkOrdersByCustomer(long customerId);
    
    @Query("DELETE FROM work_orders WHERE id = :id")
    void deleteWorkOrderById(long id);
    
    // Additional method for ReportsViewModel
    @Query("SELECT * FROM work_orders")
    List<WorkOrder> getAllWorkOrdersSync();
    
    // Sync-related methods
    @Query("SELECT * FROM work_orders WHERE syncStatus = 'pending'")
    List<WorkOrder> getPendingSyncWorkOrders();
    
    @Query("SELECT COUNT(*) FROM work_orders WHERE syncStatus = 'pending'")
    int getPendingSyncWorkOrderCount();
    
    @Query("SELECT COUNT(*) FROM work_orders")
    int getWorkOrderCount();
    
    @Query("DELETE FROM work_orders WHERE createdAt < :timestamp")
    void deleteOldWorkOrders(long timestamp);
} 