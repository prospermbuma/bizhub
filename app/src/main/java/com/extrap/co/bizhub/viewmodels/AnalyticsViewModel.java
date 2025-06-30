package com.extrap.co.bizhub.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.entities.WorkOrder;
import com.extrap.co.bizhub.data.entities.Customer;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsViewModel extends AndroidViewModel {
    private final FieldServiceApp app;
    private final MutableLiveData<AnalyticsStats> analyticsStats;
    private final MutableLiveData<List<BarEntry>> workOrdersByMonth;
    private final MutableLiveData<List<Entry>> performanceData;
    private final MutableLiveData<List<PieEntry>> statusDistribution;
    private final MutableLiveData<List<PieEntry>> priorityDistribution;

    public AnalyticsViewModel(Application application) {
        super(application);
        app = (FieldServiceApp) application;
        analyticsStats = new MutableLiveData<>();
        workOrdersByMonth = new MutableLiveData<>();
        performanceData = new MutableLiveData<>();
        statusDistribution = new MutableLiveData<>();
        priorityDistribution = new MutableLiveData<>();
        loadAnalyticsData();
    }

    public LiveData<AnalyticsStats> getAnalyticsStats() {
        return analyticsStats;
    }

    public LiveData<List<BarEntry>> getWorkOrdersByMonth() {
        return workOrdersByMonth;
    }

    public LiveData<List<Entry>> getPerformanceData() {
        return performanceData;
    }

    public LiveData<List<PieEntry>> getStatusDistribution() {
        return statusDistribution;
    }

    public LiveData<List<PieEntry>> getPriorityDistribution() {
        return priorityDistribution;
    }

    public void loadAnalyticsData() {
        // Load analytics data from database
        // This is a simplified implementation
        AnalyticsStats stats = new AnalyticsStats();
        
        // Get work order statistics
        List<WorkOrder> allWorkOrders = app.getDatabase().workOrderDao().getAllWorkOrdersSync();
        stats.totalWorkOrders = allWorkOrders.size();
        
        // Count by status
        stats.pendingWorkOrders = 0;
        stats.inProgressWorkOrders = 0;
        stats.completedWorkOrders = 0;
        
        for (WorkOrder workOrder : allWorkOrders) {
            switch (workOrder.getStatus()) {
                case "pending":
                    stats.pendingWorkOrders++;
                    break;
                case "in_progress":
                    stats.inProgressWorkOrders++;
                    break;
                case "completed":
                    stats.completedWorkOrders++;
                    break;
            }
        }
        
        // Get customer statistics
        List<Customer> allCustomers = app.getDatabase().customerDao().getAllCustomersSync();
        stats.totalCustomers = allCustomers.size();
        
        // Calculate completion rate
        if (stats.totalWorkOrders > 0) {
            stats.completionRate = (double) stats.completedWorkOrders / stats.totalWorkOrders * 100;
        }
        
        // Calculate average completion time
        stats.averageCompletionTime = calculateAverageCompletionTime(allWorkOrders);
        
        // Calculate customer satisfaction (placeholder)
        stats.customerSatisfaction = 85.5; // Placeholder value
        
        analyticsStats.setValue(stats);
        
        // Load chart data
        loadWorkOrdersByMonth(allWorkOrders);
        loadPerformanceData(allWorkOrders);
        loadStatusDistribution(allWorkOrders);
        loadPriorityDistribution(allWorkOrders);
    }

    private void loadWorkOrdersByMonth(List<WorkOrder> workOrders) {
        List<BarEntry> entries = new ArrayList<>();
        Map<Integer, Integer> monthlyCount = new HashMap<>();
        
        Calendar cal = Calendar.getInstance();
        for (WorkOrder workOrder : workOrders) {
            cal.setTimeInMillis(workOrder.getCreatedAt());
            int month = cal.get(Calendar.MONTH);
            monthlyCount.put(month, monthlyCount.getOrDefault(month, 0) + 1);
        }
        
        for (Map.Entry<Integer, Integer> entry : monthlyCount.entrySet()) {
            entries.add(new BarEntry(entry.getKey(), entry.getValue()));
        }
        
        workOrdersByMonth.setValue(entries);
    }

    private void loadPerformanceData(List<WorkOrder> workOrders) {
        List<Entry> entries = new ArrayList<>();
        int index = 0;
        
        for (WorkOrder workOrder : workOrders) {
            if (workOrder.getActualDuration() > 0) {
                entries.add(new Entry(index++, (float) workOrder.getActualDuration()));
            }
        }
        
        performanceData.setValue(entries);
    }

    private void loadStatusDistribution(List<WorkOrder> workOrders) {
        List<PieEntry> entries = new ArrayList<>();
        Map<String, Integer> statusCount = new HashMap<>();
        
        for (WorkOrder workOrder : workOrders) {
            String status = workOrder.getStatus();
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
        }
        
        int index = 0;
        for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        
        statusDistribution.setValue(entries);
    }

    private void loadPriorityDistribution(List<WorkOrder> workOrders) {
        List<PieEntry> entries = new ArrayList<>();
        Map<String, Integer> priorityCount = new HashMap<>();
        
        for (WorkOrder workOrder : workOrders) {
            String priority = workOrder.getPriority();
            priorityCount.put(priority, priorityCount.getOrDefault(priority, 0) + 1);
        }
        
        for (Map.Entry<String, Integer> entry : priorityCount.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        
        priorityDistribution.setValue(entries);
    }

    private double calculateAverageCompletionTime(List<WorkOrder> workOrders) {
        double totalTime = 0;
        int completedCount = 0;
        
        for (WorkOrder workOrder : workOrders) {
            if (workOrder.getActualDuration() > 0) {
                totalTime += workOrder.getActualDuration();
                completedCount++;
            }
        }
        
        return completedCount > 0 ? totalTime / completedCount : 0;
    }

    public static class AnalyticsStats {
        public int totalWorkOrders;
        public int pendingWorkOrders;
        public int inProgressWorkOrders;
        public int completedWorkOrders;
        public int totalCustomers;
        public double completionRate;
        public double averageCompletionTime;
        public double customerSatisfaction;

        public int getTotalWorkOrders() {
            return totalWorkOrders;
        }

        public int getCompletedWorkOrders() {
            return completedWorkOrders;
        }

        public double getAverageCompletionTime() {
            return averageCompletionTime;
        }

        public double getCustomerSatisfaction() {
            return customerSatisfaction;
        }
    }
} 