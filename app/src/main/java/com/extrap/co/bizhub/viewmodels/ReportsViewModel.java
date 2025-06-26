package com.extrap.co.bizhub.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.dao.CustomerDao;
import com.extrap.co.bizhub.data.dao.WorkOrderDao;
import com.extrap.co.bizhub.data.entities.Customer;
import com.extrap.co.bizhub.data.entities.WorkOrder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReportsViewModel extends AndroidViewModel {
    
    private WorkOrderDao workOrderDao;
    private CustomerDao customerDao;
    private ExecutorService executorService;
    
    private MutableLiveData<Integer> totalWorkOrders = new MutableLiveData<>();
    private MutableLiveData<Integer> completedWorkOrders = new MutableLiveData<>();
    private MutableLiveData<Integer> pendingWorkOrders = new MutableLiveData<>();
    private MutableLiveData<Integer> totalCustomers = new MutableLiveData<>();
    private MutableLiveData<Double> totalRevenue = new MutableLiveData<>();
    private MutableLiveData<String> averageCompletionTime = new MutableLiveData<>();
    
    private MutableLiveData<List<Float>> workOrderStatusData = new MutableLiveData<>();
    private MutableLiveData<List<Float>> workOrdersByMonthData = new MutableLiveData<>();
    private MutableLiveData<List<Float>> revenueTrendData = new MutableLiveData<>();
    private MutableLiveData<List<Float>> workOrdersByPriorityData = new MutableLiveData<>();
    
    private String currentPeriod = "month";
    
    public ReportsViewModel(Application application) {
        super(application);
        workOrderDao = FieldServiceApp.getInstance().getDatabase().workOrderDao();
        customerDao = FieldServiceApp.getInstance().getDatabase().customerDao();
        executorService = Executors.newFixedThreadPool(4);
        
        loadStatistics();
        loadChartData();
    }
    
    public LiveData<Integer> getTotalWorkOrders() {
        return totalWorkOrders;
    }
    
    public LiveData<Integer> getCompletedWorkOrders() {
        return completedWorkOrders;
    }
    
    public LiveData<Integer> getPendingWorkOrders() {
        return pendingWorkOrders;
    }
    
    public LiveData<Integer> getTotalCustomers() {
        return totalCustomers;
    }
    
    public LiveData<Double> getTotalRevenue() {
        return totalRevenue;
    }
    
    public LiveData<String> getAverageCompletionTime() {
        return averageCompletionTime;
    }
    
    public LiveData<List<Float>> getWorkOrderStatusData() {
        return workOrderStatusData;
    }
    
    public LiveData<List<Float>> getWorkOrdersByMonthData() {
        return workOrdersByMonthData;
    }
    
    public LiveData<List<Float>> getRevenueTrendData() {
        return revenueTrendData;
    }
    
    public LiveData<List<Float>> getWorkOrdersByPriorityData() {
        return workOrdersByPriorityData;
    }
    
    public void updatePeriod(String period) {
        this.currentPeriod = period;
        loadStatistics();
        loadChartData();
    }
    
    private void loadStatistics() {
        executorService.execute(() -> {
            try {
                // Load work order statistics
                List<WorkOrder> allWorkOrders = workOrderDao.getAllWorkOrdersSync();
                int total = allWorkOrders.size();
                int completed = 0;
                int pending = 0;
                double revenue = 0.0;
                long totalCompletionTime = 0;
                int completedCount = 0;
                
                for (WorkOrder workOrder : allWorkOrders) {
                    if ("completed".equals(workOrder.getStatus())) {
                        completed++;
                        // Calculate completion time (simplified)
                        long completionTime = workOrder.getUpdatedAt() - workOrder.getCreatedAt();
                        totalCompletionTime += completionTime;
                        completedCount++;
                        
                        // Calculate revenue (simplified - using priority as cost multiplier)
                        double baseCost = 100.0;
                        switch (workOrder.getPriority()) {
                            case "low":
                                revenue += baseCost * 0.8;
                                break;
                            case "medium":
                                revenue += baseCost * 1.0;
                                break;
                            case "high":
                                revenue += baseCost * 1.5;
                                break;
                            case "urgent":
                                revenue += baseCost * 2.0;
                                break;
                        }
                    } else if ("pending".equals(workOrder.getStatus())) {
                        pending++;
                    }
                }
                
                // Load customer statistics
                List<Customer> allCustomers = customerDao.getAllCustomersSync();
                int customerCount = allCustomers.size();
                
                // Calculate average completion time
                String avgCompletionTime = "0";
                if (completedCount > 0) {
                    long avgTime = totalCompletionTime / completedCount;
                    avgCompletionTime = String.format("%.1f", avgTime / (1000.0 * 60 * 60)); // Convert to hours
                }
                
                // Post results
                totalWorkOrders.postValue(total);
                completedWorkOrders.postValue(completed);
                pendingWorkOrders.postValue(pending);
                totalCustomers.postValue(customerCount);
                totalRevenue.postValue(revenue);
                averageCompletionTime.postValue(avgCompletionTime);
                
            } catch (Exception e) {
                // Handle error
            }
        });
    }
    
    private void loadChartData() {
        executorService.execute(() -> {
            try {
                List<WorkOrder> allWorkOrders = workOrderDao.getAllWorkOrdersSync();
                
                // Work Order Status Data
                loadWorkOrderStatusData(allWorkOrders);
                
                // Work Orders by Month Data
                loadWorkOrdersByMonthData(allWorkOrders);
                
                // Revenue Trend Data
                loadRevenueTrendData(allWorkOrders);
                
                // Work Orders by Priority Data
                loadWorkOrdersByPriorityData(allWorkOrders);
                
            } catch (Exception e) {
                // Handle error
            }
        });
    }
    
    private void loadWorkOrderStatusData(List<WorkOrder> workOrders) {
        int completed = 0, inProgress = 0, pending = 0, cancelled = 0;
        
        for (WorkOrder workOrder : workOrders) {
            switch (workOrder.getStatus()) {
                case "completed":
                    completed++;
                    break;
                case "in_progress":
                    inProgress++;
                    break;
                case "pending":
                    pending++;
                    break;
                case "cancelled":
                    cancelled++;
                    break;
            }
        }
        
        List<Float> data = new ArrayList<>();
        data.add((float) completed);
        data.add((float) inProgress);
        data.add((float) pending);
        data.add((float) cancelled);
        
        workOrderStatusData.postValue(data);
    }
    
    private void loadWorkOrdersByMonthData(List<WorkOrder> workOrders) {
        int[] monthlyCounts = new int[12];
        
        Calendar calendar = Calendar.getInstance();
        for (WorkOrder workOrder : workOrders) {
            calendar.setTimeInMillis(workOrder.getCreatedAt());
            int month = calendar.get(Calendar.MONTH);
            monthlyCounts[month]++;
        }
        
        List<Float> data = new ArrayList<>();
        for (int count : monthlyCounts) {
            data.add((float) count);
        }
        
        workOrdersByMonthData.postValue(data);
    }
    
    private void loadRevenueTrendData(List<WorkOrder> workOrders) {
        double[] monthlyRevenue = new double[12];
        
        Calendar calendar = Calendar.getInstance();
        for (WorkOrder workOrder : workOrders) {
            if ("completed".equals(workOrder.getStatus())) {
                calendar.setTimeInMillis(workOrder.getCreatedAt());
                int month = calendar.get(Calendar.MONTH);
                
                // Calculate revenue based on priority
                double baseCost = 100.0;
                switch (workOrder.getPriority()) {
                    case "low":
                        monthlyRevenue[month] += baseCost * 0.8;
                        break;
                    case "medium":
                        monthlyRevenue[month] += baseCost * 1.0;
                        break;
                    case "high":
                        monthlyRevenue[month] += baseCost * 1.5;
                        break;
                    case "urgent":
                        monthlyRevenue[month] += baseCost * 2.0;
                        break;
                }
            }
        }
        
        List<Float> data = new ArrayList<>();
        for (double revenue : monthlyRevenue) {
            data.add((float) revenue);
        }
        
        revenueTrendData.postValue(data);
    }
    
    private void loadWorkOrdersByPriorityData(List<WorkOrder> workOrders) {
        int low = 0, medium = 0, high = 0, urgent = 0;
        
        for (WorkOrder workOrder : workOrders) {
            switch (workOrder.getPriority()) {
                case "low":
                    low++;
                    break;
                case "medium":
                    medium++;
                    break;
                case "high":
                    high++;
                    break;
                case "urgent":
                    urgent++;
                    break;
            }
        }
        
        List<Float> data = new ArrayList<>();
        data.add((float) low);
        data.add((float) medium);
        data.add((float) high);
        data.add((float) urgent);
        
        workOrdersByPriorityData.postValue(data);
    }
    
    public void refreshData() {
        loadStatistics();
        loadChartData();
    }
    
    public void exportReport() {
        executorService.execute(() -> {
            // TODO: Implement report export functionality
        });
    }
    
    public void generatePDFReport() {
        executorService.execute(() -> {
            // TODO: Implement PDF report generation
        });
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
} 