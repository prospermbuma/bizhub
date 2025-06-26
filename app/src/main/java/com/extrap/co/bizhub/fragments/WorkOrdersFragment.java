package com.extrap.co.bizhub.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.adapters.WorkOrderAdapter;
import com.extrap.co.bizhub.data.entities.WorkOrder;
import com.extrap.co.bizhub.viewmodels.WorkOrderViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class WorkOrdersFragment extends Fragment {
    
    private WorkOrderViewModel viewModel;
    private WorkOrderAdapter adapter;
    
    // Views
    private RecyclerView workOrdersRecyclerView;
    private TextInputEditText searchEditText;
    private ChipGroup filterChipGroup;
    private LinearLayout emptyState;
    private LinearLayout loadingState;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_work_orders, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupViewModel();
        setupRecyclerView();
        setupSearch();
        setupFilters();
        observeData();
    }
    
    private void initializeViews(View view) {
        workOrdersRecyclerView = view.findViewById(R.id.work_orders_recycler_view);
        searchEditText = view.findViewById(R.id.search_edit_text);
        filterChipGroup = view.findViewById(R.id.filter_chip_group);
        emptyState = view.findViewById(R.id.empty_state);
        loadingState = view.findViewById(R.id.loading_state);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(WorkOrderViewModel.class);
    }
    
    private void setupRecyclerView() {
        adapter = new WorkOrderAdapter();
        workOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        workOrdersRecyclerView.setAdapter(adapter);
        
        // Set click listeners
        adapter.setOnItemClickListener(new WorkOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WorkOrder workOrder) {
                // TODO: Navigate to work order details
                Snackbar.make(workOrdersRecyclerView, "Opening work order: " + workOrder.getWorkOrderNumber(), Snackbar.LENGTH_SHORT).show();
            }
            
            @Override
            public void onStatusClick(WorkOrder workOrder) {
                showStatusChangeDialog(workOrder);
            }
        });
    }
    
    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.searchWorkOrders(s.toString());
            }
        });
    }
    
    private void setupFilters() {
        filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.filter_all) {
                viewModel.loadWorkOrdersByFilter("all");
            } else if (checkedId == R.id.filter_pending) {
                viewModel.loadWorkOrdersByFilter("pending");
            } else if (checkedId == R.id.filter_in_progress) {
                viewModel.loadWorkOrdersByFilter("in_progress");
            } else if (checkedId == R.id.filter_completed) {
                viewModel.loadWorkOrdersByFilter("completed");
            } else if (checkedId == R.id.filter_today) {
                viewModel.loadWorkOrdersByFilter("today");
            }
        });
    }
    
    private void observeData() {
        // Observe work orders
        viewModel.getWorkOrders().observe(getViewLifecycleOwner(), workOrders -> {
            adapter.updateWorkOrders(workOrders);
            updateEmptyState(workOrders);
        });
        
        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingState.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) {
                workOrdersRecyclerView.setVisibility(View.GONE);
                emptyState.setVisibility(View.GONE);
            } else {
                workOrdersRecyclerView.setVisibility(View.VISIBLE);
            }
        });
        
        // Observe error messages
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Snackbar.make(workOrdersRecyclerView, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }
    
    private void updateEmptyState(List<WorkOrder> workOrders) {
        if (workOrders == null || workOrders.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            workOrdersRecyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            workOrdersRecyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    private void showStatusChangeDialog(WorkOrder workOrder) {
        // TODO: Show dialog to change work order status
        String[] statuses = {"pending", "in_progress", "completed", "cancelled"};
        
        // For now, just cycle through statuses
        String currentStatus = workOrder.getStatus();
        String newStatus = "pending";
        
        switch (currentStatus) {
            case "pending":
                newStatus = "in_progress";
                break;
            case "in_progress":
                newStatus = "completed";
                break;
            case "completed":
                newStatus = "cancelled";
                break;
            case "cancelled":
                newStatus = "pending";
                break;
        }
        
        viewModel.updateWorkOrderStatus(workOrder.getId(), newStatus);
        Snackbar.make(workOrdersRecyclerView, "Status updated to: " + newStatus, Snackbar.LENGTH_SHORT).show();
    }
} 