package com.extrap.co.bizhub.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.data.entities.WorkOrder;
import com.extrap.co.bizhub.viewmodels.WorkOrderViewModel;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WorkOrderDetailsActivity extends AppCompatActivity {
    
    private WorkOrderViewModel viewModel;
    private WorkOrder currentWorkOrder;
    private long workOrderId;
    
    // Views
    private TextView workOrderNumberText;
    private TextView customerNameText;
    private TextView serviceTypeText;
    private TextView descriptionText;
    private TextView scheduledDateText;
    private TextView createdDateText;
    private TextView assignedToText;
    private Chip statusChip;
    private Chip priorityChip;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_details);
        
        // Get work order ID from intent
        workOrderId = getIntent().getLongExtra("work_order_id", -1);
        if (workOrderId == -1) {
            Toast.makeText(this, "Invalid work order", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initializeViews();
        setupToolbar();
        setupViewModel();
        loadWorkOrderDetails();
    }
    
    private void initializeViews() {
        workOrderNumberText = findViewById(R.id.work_order_number_text);
        customerNameText = findViewById(R.id.customer_name_text);
        serviceTypeText = findViewById(R.id.service_type_text);
        descriptionText = findViewById(R.id.description_text);
        scheduledDateText = findViewById(R.id.scheduled_date_text);
        createdDateText = findViewById(R.id.created_date_text);
        assignedToText = findViewById(R.id.assigned_to_text);
        statusChip = findViewById(R.id.status_chip);
        priorityChip = findViewById(R.id.priority_chip);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Work Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(WorkOrderViewModel.class);
    }
    
    private void loadWorkOrderDetails() {
        // TODO: Load work order details from database
        // For now, create a sample work order
        currentWorkOrder = new WorkOrder();
        currentWorkOrder.setId(workOrderId);
        currentWorkOrder.setWorkOrderNumber("WO-" + String.format("%03d", (int) workOrderId));
        currentWorkOrder.setCustomerName("AC Repair - John Smith");
        currentWorkOrder.setServiceType("Repair");
        currentWorkOrder.setDescription("AC not cooling properly, needs inspection and repair");
        currentWorkOrder.setPriority("high");
        currentWorkOrder.setStatus("pending");
        currentWorkOrder.setScheduledDate(System.currentTimeMillis());
        currentWorkOrder.setCreatedAt(System.currentTimeMillis());
        currentWorkOrder.setAssignedTo(1L);
        
        displayWorkOrderDetails();
    }
    
    private void displayWorkOrderDetails() {
        if (currentWorkOrder == null) return;
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        
        workOrderNumberText.setText(currentWorkOrder.getWorkOrderNumber());
        customerNameText.setText(currentWorkOrder.getCustomerName());
        serviceTypeText.setText(currentWorkOrder.getServiceType());
        descriptionText.setText(currentWorkOrder.getDescription());
        scheduledDateText.setText(dateFormat.format(new Date(currentWorkOrder.getScheduledDate())));
        createdDateText.setText(dateFormat.format(new Date(currentWorkOrder.getCreatedAt())));
        assignedToText.setText("Mike Smith"); // TODO: Get actual user name
        
        // Set status chip
        statusChip.setText(currentWorkOrder.getStatus().replace("_", " ").toUpperCase());
        switch (currentWorkOrder.getStatus()) {
            case "pending":
                statusChip.setChipBackgroundColorResource(R.color.status_pending);
                break;
            case "in_progress":
                statusChip.setChipBackgroundColorResource(R.color.status_in_progress);
                break;
            case "completed":
                statusChip.setChipBackgroundColorResource(R.color.status_completed);
                break;
            case "cancelled":
                statusChip.setChipBackgroundColorResource(R.color.status_cancelled);
                break;
        }
        
        // Set priority chip
        priorityChip.setText(currentWorkOrder.getPriority().toUpperCase());
        switch (currentWorkOrder.getPriority()) {
            case "high":
            case "urgent":
                priorityChip.setChipBackgroundColorResource(R.color.priority_high);
                break;
            case "medium":
                priorityChip.setChipBackgroundColorResource(R.color.priority_medium);
                break;
            case "low":
                priorityChip.setChipBackgroundColorResource(R.color.priority_low);
                break;
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_work_order_details, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_edit) {
            // TODO: Navigate to edit work order
            Toast.makeText(this, "Edit work order", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_delete) {
            showDeleteConfirmationDialog();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Delete Work Order")
            .setMessage("Are you sure you want to delete this work order? This action cannot be undone.")
            .setPositiveButton("Delete", (dialog, which) -> {
                // TODO: Delete work order
                Toast.makeText(this, "Work order deleted", Toast.LENGTH_SHORT).show();
                finish();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
} 