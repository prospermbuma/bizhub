package com.extrap.co.bizhub.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.entities.WorkOrder;

import java.util.Calendar;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {
    
    private final MutableLiveData<Integer> todayTasksCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> pendingOrdersCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> completedTodayCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalCustomersCount = new MutableLiveData<>(0);
    private final MutableLiveData<List<WorkOrder>> recentActivities = new MutableLiveData<>();
    
    public DashboardViewModel(Application application) {
        super(application);
        loadDashboardData();
    }
    
    private void loadDashboardData() {
        // Load today's tasks count
        loadTodayTasksCount();
        
        // Load pending orders count
        loadPendingOrdersCount();
        
        // Load completed today count
        loadCompletedTodayCount();
        
        // Load total customers count
        loadTotalCustomersCount();
        
        // Load recent activities
        loadRecentActivities();
    }
    
    private void loadTodayTasksCount() {
        // Get today's start and end timestamps
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfDay = calendar.getTimeInMillis();
        
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        long endOfDay = calendar.getTimeInMillis();
        
        // Query work orders for today
        FieldServiceApp.getInstance().getDatabase().workOrderDao()
            .getWorkOrdersByDateRange(startOfDay, endOfDay)
            .observeForever(workOrders -> {
                if (workOrders != null) {
                    todayTasksCount.setValue(workOrders.size());
                }
            });
    }
    
    private void loadPendingOrdersCount() {
        FieldServiceApp.getInstance().getDatabase().workOrderDao()
            .getPendingWorkOrders()
            .observeForever(workOrders -> {
                if (workOrders != null) {
                    pendingOrdersCount.setValue(workOrders.size());
                }
            });
    }
    
    private void loadCompletedTodayCount() {
        // Get today's start timestamp
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfDay = calendar.getTimeInMillis();
        
        // Query completed work orders for today
        FieldServiceApp.getInstance().getDatabase().workOrderDao()
            .getCompletedWorkOrders(startOfDay)
            .observeForever(workOrders -> {
                if (workOrders != null) {
                    completedTodayCount.setValue(workOrders.size());
                }
            });
    }
    
    private void loadTotalCustomersCount() {
        FieldServiceApp.getInstance().getDatabase().customerDao()
            .getAllActiveCustomers()
            .observeForever(customers -> {
                if (customers != null) {
                    totalCustomersCount.setValue(customers.size());
                }
            });
    }
    
    private void loadRecentActivities() {
        // Get recent work orders (last 10)
        FieldServiceApp.getInstance().getDatabase().workOrderDao()
            .getAllWorkOrders()
            .observeForever(workOrders -> {
                if (workOrders != null) {
                    // Take only the first 10 work orders
                    List<WorkOrder> recent = workOrders.size() > 10 ? 
                        workOrders.subList(0, 10) : workOrders;
                    recentActivities.setValue(recent);
                }
            });
    }
    
    public LiveData<Integer> getTodayTasksCount() {
        return todayTasksCount;
    }
    
    public LiveData<Integer> getPendingOrdersCount() {
        return pendingOrdersCount;
    }
    
    public LiveData<Integer> getCompletedTodayCount() {
        return completedTodayCount;
    }
    
    public LiveData<Integer> getTotalCustomersCount() {
        return totalCustomersCount;
    }
    
    public LiveData<List<WorkOrder>> getRecentActivities() {
        return recentActivities;
    }
    
    public void refreshData() {
        loadDashboardData();
    }
} 