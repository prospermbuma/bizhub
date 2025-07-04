package com.extrap.co.bizhub.viewmodels;

import android.app.Application;
import android.location.Location;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.entities.WorkOrder;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapViewModel extends AndroidViewModel {
    
    private FieldServiceApp app;
    private MutableLiveData<List<WorkOrder>> workOrders;
    private MutableLiveData<Location> currentLocation;
    private MutableLiveData<String> errorMessage;
    private ExecutorService executorService;
    
    public MapViewModel(Application application) {
        super(application);
        app = (FieldServiceApp) application;
        workOrders = new MutableLiveData<>();
        currentLocation = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        executorService = Executors.newSingleThreadExecutor();
    }
    
    public LiveData<List<WorkOrder>> getWorkOrders() {
        return workOrders;
    }
    
    public LiveData<Location> getCurrentLocation() {
        return currentLocation;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public void loadWorkOrders() {
        new Thread(() -> {
            try {
                List<WorkOrder> orders = app.getDatabase().workOrderDao().getAllWorkOrdersSync();
                workOrders.postValue(orders);
            } catch (Exception e) {
                errorMessage.postValue("Error loading work orders: " + e.getMessage());
            }
        }).start();
    }
    
    public void setCurrentLocation(Location location) {
        currentLocation.postValue(location);
    }
    
    public void loadWorkOrdersByStatus(String status) {
        executorService.execute(() -> {
            try {
                List<WorkOrder> orders = app.getDatabase().workOrderDao().getWorkOrdersByStatusSync(status);
                workOrders.postValue(orders);
            } catch (Exception e) {
                errorMessage.postValue("Error loading work orders: " + e.getMessage());
            }
        });
    }
    
    public void loadWorkOrdersByPriority(String priority) {
        executorService.execute(() -> {
            try {
                List<WorkOrder> orders = app.getDatabase().workOrderDao().getWorkOrdersByPrioritySync(priority);
                workOrders.postValue(orders);
            } catch (Exception e) {
                errorMessage.postValue("Error loading work orders: " + e.getMessage());
            }
        });
    }
    
    public void loadWorkOrdersNearLocation(double latitude, double longitude, double radiusKm) {
        new Thread(() -> {
            try {
                // TODO: Implement location-based filtering
                List<WorkOrder> orders = app.getDatabase().workOrderDao().getAllWorkOrdersSync();
                workOrders.postValue(orders);
            } catch (Exception e) {
                errorMessage.postValue("Error loading nearby work orders: " + e.getMessage());
            }
        }).start();
    }
} 