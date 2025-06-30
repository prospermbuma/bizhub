package com.extrap.co.bizhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.adapters.CustomerAdapter;
import com.extrap.co.bizhub.data.entities.Customer;
import com.extrap.co.bizhub.viewmodels.CustomerViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity {
    
    private static final String TAG = "CustomerActivity";
    
    private Toolbar toolbar;
    private RecyclerView customersRecyclerView;
    private CustomerAdapter customerAdapter;
    private CustomerViewModel viewModel;
    private FloatingActionButton addCustomerFab;
    private TextView emptyStateText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "CustomerActivity onCreate started");
        
        try {
            setContentView(R.layout.activity_customer);
            Log.d(TAG, "CustomerActivity layout set successfully");
            
            // Initialize ViewModel
            viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
            
            // Initialize views
            initializeViews();
            
            // Setup toolbar
            setupToolbar();
            
            // Setup recycler view
            setupRecyclerView();
            
            // Setup click listeners
            setupClickListeners();
            
            // Observe data
            observeData();
            
            // Load customers
            viewModel.loadCustomers();
            
            Log.d(TAG, "CustomerActivity onCreate completed successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in CustomerActivity onCreate", e);
        }
    }
    
    private void initializeViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            customersRecyclerView = findViewById(R.id.customers_recycler_view);
            addCustomerFab = findViewById(R.id.add_customer_fab);
            emptyStateText = findViewById(R.id.empty_state_text);
            
            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
        }
    }
    
    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.customers);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up toolbar", e);
        }
    }
    
    private void setupRecyclerView() {
        try {
            customerAdapter = new CustomerAdapter(new ArrayList<>(), customer -> {
                // Handle customer click - show customer details
                showCustomerDetails(customer);
            });
            
            customersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            customersRecyclerView.setAdapter(customerAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up recycler view", e);
        }
    }
    
    private void setupClickListeners() {
        try {
            // Add customer FAB
            if (addCustomerFab != null) {
                addCustomerFab.setOnClickListener(v -> {
                    showAddCustomerDialog();
                });
            }
            
            Log.d(TAG, "Click listeners setup successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners", e);
        }
    }
    
    private void showCustomerDetails(Customer customer) {
        try {
            // TODO: Navigate to customer details screen
            // For now, just show a simple dialog or navigate to a new activity
            Log.d(TAG, "Showing details for customer: " + customer.getName());
        } catch (Exception e) {
            Log.e(TAG, "Error showing customer details", e);
        }
    }
    
    private void showAddCustomerDialog() {
        try {
            // TODO: Show add customer dialog or navigate to add customer screen
            Log.d(TAG, "Showing add customer dialog");
        } catch (Exception e) {
            Log.e(TAG, "Error showing add customer dialog", e);
        }
    }
    
    private void observeData() {
        try {
            // Observe customers
            viewModel.getCustomers().observe(this, customers -> {
                if (customers != null) {
                    customerAdapter.updateCustomers(customers);
                    
                    // Show/hide empty state
                    if (customers.isEmpty()) {
                        if (emptyStateText != null) emptyStateText.setVisibility(View.VISIBLE);
                        if (customersRecyclerView != null) customersRecyclerView.setVisibility(View.GONE);
                    } else {
                        if (emptyStateText != null) emptyStateText.setVisibility(View.GONE);
                        if (customersRecyclerView != null) customersRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            });
            
            // Observe loading state
            viewModel.isLoading().observe(this, isLoading -> {
                // TODO: Show/hide loading indicator
            });
            
            // Observe error state
            viewModel.getError().observe(this, error -> {
                if (error != null) {
                    Log.e(TAG, "Error loading customers: " + error);
                    // TODO: Show error message to user
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error observing data", e);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
                return true;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            Log.e(TAG, "Error in onOptionsItemSelected", e);
            return super.onOptionsItemSelected(item);
        }
    }
} 