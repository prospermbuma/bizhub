package com.extrap.co.bizhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.adapters.WorkOrderAdapter;
import com.extrap.co.bizhub.data.entities.WorkOrder;
import com.extrap.co.bizhub.viewmodels.WorkOrderViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    
    private static final String TAG = "MainActivity";
    
    private Toolbar toolbar;
    private RecyclerView workOrdersRecyclerView;
    private FloatingActionButton addWorkOrderFab;
    private WorkOrderViewModel viewModel;
    private WorkOrderAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            
            Log.d(TAG, "MainActivity onCreate started");
            
            // Initialize managers
            // preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
            // networkUtils = FieldServiceApp.getInstance().getNetworkUtils();
            
            // Initialize views
            initializeViews();
            
            // Setup navigation
            setupToolbar();
            setupRecyclerView();
            setupViewModel();
            setupClickListeners();
            
            // Check for filter from intent
            String filter = getIntent().getStringExtra("filter");
            if (filter != null) {
                applyFilter(filter);
            }
            
            // Check authentication
            // checkAuthentication();
            
            Log.d(TAG, "MainActivity onCreate completed successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in MainActivity onCreate", e);
            // Check if this might be related to the unknown error
            if (e.getMessage() != null && e.getMessage().contains("unknown error")) {
                Log.e(TAG, "POTENTIAL SOURCE OF 'UNKNOWN ERROR' MESSAGE IN MainActivity!", e);
            }
            throw e; // Re-throw to maintain original behavior
        }
    }
    
    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        workOrdersRecyclerView = findViewById(R.id.work_orders_recycler_view);
        addWorkOrderFab = findViewById(R.id.add_work_order_fab);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.work_orders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void setupRecyclerView() {
        adapter = new WorkOrderAdapter();
        workOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        workOrdersRecyclerView.setAdapter(adapter);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(WorkOrderViewModel.class);
        viewModel.getWorkOrders().observe(this, workOrders -> {
            adapter.updateWorkOrders(workOrders);
            
            // If no work orders exist, add some sample data
            if (workOrders == null || workOrders.isEmpty()) {
                addSampleWorkOrders();
            }
        });
        
        // Load work orders when the activity starts
        viewModel.loadWorkOrders();
    }
    
    private void addSampleWorkOrders() {
        // Add some sample work orders for testing
        WorkOrder sample1 = new WorkOrder();
        sample1.setWorkOrderNumber("WO-001");
        sample1.setCustomerId(1);
        sample1.setServiceType("Maintenance");
        sample1.setDescription("Regular HVAC maintenance check");
        sample1.setStatus("pending");
        sample1.setPriority("medium");
        sample1.setScheduledDate(System.currentTimeMillis() + 86400000); // Tomorrow
        sample1.setDueDate(System.currentTimeMillis() + 172800000); // Day after tomorrow
        sample1.setCreatedAt(System.currentTimeMillis());
        sample1.setUpdatedAt(System.currentTimeMillis());
        
        WorkOrder sample2 = new WorkOrder();
        sample2.setWorkOrderNumber("WO-002");
        sample2.setCustomerId(2);
        sample2.setServiceType("Repair");
        sample2.setDescription("Fix broken water heater");
        sample2.setStatus("in_progress");
        sample2.setPriority("high");
        sample2.setScheduledDate(System.currentTimeMillis());
        sample2.setDueDate(System.currentTimeMillis() + 86400000);
        sample2.setCreatedAt(System.currentTimeMillis());
        sample2.setUpdatedAt(System.currentTimeMillis());
        
        WorkOrder sample3 = new WorkOrder();
        sample3.setWorkOrderNumber("WO-003");
        sample3.setCustomerId(3);
        sample3.setServiceType("Installation");
        sample3.setDescription("Install new security system");
        sample3.setStatus("completed");
        sample3.setPriority("low");
        sample3.setScheduledDate(System.currentTimeMillis() - 86400000); // Yesterday
        sample3.setDueDate(System.currentTimeMillis());
        sample3.setCreatedAt(System.currentTimeMillis());
        sample3.setUpdatedAt(System.currentTimeMillis());
        
        viewModel.addWorkOrder(sample1);
        viewModel.addWorkOrder(sample2);
        viewModel.addWorkOrder(sample3);
    }
    
    private void setupClickListeners() {
        addWorkOrderFab.setOnClickListener(v -> {
            // TODO: Navigate to add work order screen
            // Intent intent = new Intent(this, AddWorkOrderActivity.class);
            // startActivity(intent);
        });
        
        adapter.setOnItemClickListener(new WorkOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WorkOrder workOrder) {
                Intent intent = new Intent(MainActivity.this, WorkOrderActivity.class);
                intent.putExtra("work_order_id", workOrder.getId());
                startActivity(intent);
            }
            
            public void onStatusClick(WorkOrder workOrder) {
                // TODO: Handle status click
            }
        });
    }
    
    private void applyFilter(String filter) {
        switch (filter) {
            case "today":
                getSupportActionBar().setTitle("Today's Work Orders");
                viewModel.loadTodayWorkOrders();
                break;
            case "pending":
                getSupportActionBar().setTitle("Pending Work Orders");
                viewModel.loadPendingWorkOrders();
                break;
            case "completed_today":
                getSupportActionBar().setTitle("Completed Today");
                viewModel.loadCompletedTodayWorkOrders();
                break;
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_work_orders, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_filter) {
            // TODO: Show filter dialog
            return true;
        } else if (id == R.id.action_search) {
            // TODO: Show search
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
} 