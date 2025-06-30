package com.extrap.co.bizhub.activities;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InventoryActivity extends AppCompatActivity {
    
    private static final String TAG = "InventoryActivity";
    
    private Toolbar toolbar;
    private RecyclerView inventoryRecyclerView;
    private FloatingActionButton addItemFab;
    private TextView emptyStateText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "InventoryActivity onCreate started");
        
        try {
            setContentView(R.layout.activity_inventory);
            Log.d(TAG, "InventoryActivity layout set successfully");
            
            // Initialize views
            initializeViews();
            
            // Setup toolbar
            setupToolbar();
            
            // Setup recycler view
            setupRecyclerView();
            
            // Setup click listeners
            setupClickListeners();
            
            Log.d(TAG, "InventoryActivity onCreate completed successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in InventoryActivity onCreate", e);
        }
    }
    
    private void initializeViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            inventoryRecyclerView = findViewById(R.id.inventory_recycler_view);
            addItemFab = findViewById(R.id.add_item_fab);
            emptyStateText = findViewById(R.id.empty_state_text);
            
            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
        }
    }
    
    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.inventory);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up toolbar", e);
        }
    }
    
    private void setupRecyclerView() {
        try {
            // TODO: Create inventory adapter and set it up
            // For now, just set up the layout manager
            inventoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } catch (Exception e) {
            Log.e(TAG, "Error setting up recycler view", e);
        }
    }
    
    private void setupClickListeners() {
        try {
            // Add item FAB
            if (addItemFab != null) {
                addItemFab.setOnClickListener(v -> {
                    showAddItemDialog();
                });
            }
            
            Log.d(TAG, "Click listeners setup successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners", e);
        }
    }
    
    private void showAddItemDialog() {
        try {
            // TODO: Show add inventory item dialog
            Log.d(TAG, "Showing add item dialog");
        } catch (Exception e) {
            Log.e(TAG, "Error showing add item dialog", e);
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