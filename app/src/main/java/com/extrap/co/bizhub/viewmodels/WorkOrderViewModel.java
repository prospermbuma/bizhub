package com.extrap.co.bizhub.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.entities.WorkOrder;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkOrderViewModel extends ViewModel {
    
    private MutableLiveData<List<WorkOrder>> workOrders;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;
    
    private ExecutorService executorService;
    private String currentFilter = "all";
    
    public WorkOrderViewModel() {
        workOrders = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
        
        executorService = FieldServiceApp.getInstance().getExecutorService();
        
        // Load initial data
        loadWorkOrders();
    }
    
    public LiveData<List<WorkOrder>> getWorkOrders() {
        return workOrders;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public void loadWorkOrders() {
        loadWorkOrdersByFilter(currentFilter);
    }
    
    public void loadWorkOrdersByFilter(String filter) {
        currentFilter = filter;
        isLoading.setValue(true);
        
        executorService.execute(() -> {
            try {
                List<WorkOrder> orders = null;
                
                switch (filter) {
                    case "all":
                        orders = FieldServiceApp.getInstance().getDatabase()
                            .workOrderDao().getAllWorkOrdersSync();
                        break;
                    case "pending":
                        orders = FieldServiceApp.getInstance().getDatabase()
                            .workOrderDao().getWorkOrdersByStatusSync("pending");
                        break;
                    case "in_progress":
                        orders = FieldServiceApp.getInstance().getDatabase()
                            .workOrderDao().getWorkOrdersByStatusSync("in_progress");
                        break;
                    case "completed":
                        orders = FieldServiceApp.getInstance().getDatabase()
                            .workOrderDao().getWorkOrdersByStatusSync("completed");
                        break;
                    case "today":
                        Calendar today = Calendar.getInstance();
                        long startOfDay = today.getTimeInMillis();
                        today.add(Calendar.DAY_OF_MONTH, 1);
                        long endOfDay = today.getTimeInMillis();
                        orders = FieldServiceApp.getInstance().getDatabase()
                            .workOrderDao().getWorkOrdersByDateRangeSync(startOfDay, endOfDay);
                        break;
                    case "completed_today":
                        Calendar today2 = Calendar.getInstance();
                        long startOfDay2 = today2.getTimeInMillis();
                        today2.add(Calendar.DAY_OF_MONTH, 1);
                        long endOfDay2 = today2.getTimeInMillis();
                        orders = FieldServiceApp.getInstance().getDatabase()
                            .workOrderDao().getWorkOrdersByStatusAndDate("completed", startOfDay2, endOfDay2);
                        break;
                }
                
                workOrders.postValue(orders);
                isLoading.postValue(false);
                
            } catch (Exception e) {
                errorMessage.postValue("Error loading work orders: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }
    
    public void loadTodayWorkOrders() {
        loadWorkOrdersByFilter("today");
    }
    
    public void loadPendingWorkOrders() {
        loadWorkOrdersByFilter("pending");
    }
    
    public void loadCompletedTodayWorkOrders() {
        loadWorkOrdersByFilter("completed_today");
    }
    
    public void addWorkOrder(WorkOrder workOrder) {
        executorService.execute(() -> {
            try {
                long id = FieldServiceApp.getInstance().getDatabase()
                    .workOrderDao().insertWorkOrder(workOrder);
                
                if (id > 0) {
                    // Reload work orders
                    loadWorkOrders();
                } else {
                    errorMessage.postValue("Failed to add work order");
                }
            } catch (Exception e) {
                errorMessage.postValue("Error adding work order: " + e.getMessage());
            }
        });
    }
    
    public void updateWorkOrder(WorkOrder workOrder) {
        executorService.execute(() -> {
            try {
                FieldServiceApp.getInstance().getDatabase()
                    .workOrderDao().updateWorkOrder(workOrder);
                
                // Reload work orders
                loadWorkOrders();
            } catch (Exception e) {
                errorMessage.postValue("Error updating work order: " + e.getMessage());
            }
        });
    }
    
    public void deleteWorkOrder(WorkOrder workOrder) {
        executorService.execute(() -> {
            try {
                FieldServiceApp.getInstance().getDatabase()
                    .workOrderDao().deleteWorkOrder(workOrder);
                
                // Reload work orders
                loadWorkOrders();
            } catch (Exception e) {
                errorMessage.postValue("Error deleting work order: " + e.getMessage());
            }
        });
    }
    
    public void updateWorkOrderStatus(long workOrderId, String newStatus) {
        executorService.execute(() -> {
            try {
                WorkOrder workOrder = FieldServiceApp.getInstance().getDatabase()
                    .workOrderDao().getWorkOrderById(workOrderId);
                
                if (workOrder != null) {
                    workOrder.setStatus(newStatus);
                    FieldServiceApp.getInstance().getDatabase()
                        .workOrderDao().updateWorkOrder(workOrder);
                    
                    // Reload work orders
                    loadWorkOrders();
                }
            } catch (Exception e) {
                errorMessage.postValue("Error updating status: " + e.getMessage());
            }
        });
    }
    
    public void searchWorkOrders(String query) {
        // TODO: Implement search functionality
        // For now, just reload all work orders
        loadWorkOrders();
    }

    // Additional methods for compatibility
    public LiveData<WorkOrder> getWorkOrderById(long workOrderId) {
        MutableLiveData<WorkOrder> workOrder = new MutableLiveData<>();
        executorService.execute(() -> {
            try {
                WorkOrder wo = FieldServiceApp.getInstance().getDatabase()
                    .workOrderDao().getWorkOrderByIdSync((int) workOrderId);
                workOrder.postValue(wo);
            } catch (Exception e) {
                errorMessage.postValue("Error loading work order: " + e.getMessage());
            }
        });
        return workOrder;
    }

    public LiveData<Integer> getWorkOrderCount() {
        MutableLiveData<Integer> count = new MutableLiveData<>();
        executorService.execute(() -> {
            try {
                int workOrderCount = FieldServiceApp.getInstance().getDatabase()
                    .workOrderDao().getWorkOrderCount();
                count.postValue(workOrderCount);
            } catch (Exception e) {
                errorMessage.postValue("Error getting work order count: " + e.getMessage());
            }
        });
        return count;
    }

    public LiveData<Integer> getPendingWorkOrderCount() {
        MutableLiveData<Integer> count = new MutableLiveData<>();
        executorService.execute(() -> {
            try {
                int pendingCount = FieldServiceApp.getInstance().getDatabase()
                    .workOrderDao().getPendingWorkOrderCount();
                count.postValue(pendingCount);
            } catch (Exception e) {
                errorMessage.postValue("Error getting pending count: " + e.getMessage());
            }
        });
        return count;
    }

    public LiveData<Integer> getInProgressWorkOrderCount() {
        MutableLiveData<Integer> count = new MutableLiveData<>();
        executorService.execute(() -> {
            try {
                int inProgressCount = FieldServiceApp.getInstance().getDatabase()
                    .workOrderDao().getInProgressWorkOrderCount();
                count.postValue(inProgressCount);
            } catch (Exception e) {
                errorMessage.postValue("Error getting in-progress count: " + e.getMessage());
            }
        });
        return count;
    }

    public LiveData<Integer> getCompletedWorkOrderCount() {
        MutableLiveData<Integer> count = new MutableLiveData<>();
        executorService.execute(() -> {
            try {
                Calendar today = Calendar.getInstance();
                long startOfDay = today.getTimeInMillis();
                int completedCount = FieldServiceApp.getInstance().getDatabase()
                    .workOrderDao().getCompletedWorkOrderCount(startOfDay);
                count.postValue(completedCount);
            } catch (Exception e) {
                errorMessage.postValue("Error getting completed count: " + e.getMessage());
            }
        });
        return count;
    }

    public LiveData<Integer> getHighPriorityWorkOrderCount() {
        MutableLiveData<Integer> count = new MutableLiveData<>();
        executorService.execute(() -> {
            try {
                int highPriorityCount = FieldServiceApp.getInstance().getDatabase()
                    .workOrderDao().getHighPriorityWorkOrderCount();
                count.postValue(highPriorityCount);
            } catch (Exception e) {
                errorMessage.postValue("Error getting high priority count: " + e.getMessage());
            }
        });
        return count;
    }
} 