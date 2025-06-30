package com.extrap.co.bizhub.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ScheduleActivity extends AppCompatActivity {
    
    private static final String TAG = "ScheduleActivity";
    
    private Toolbar toolbar;
    private RecyclerView scheduleRecyclerView;
    private FloatingActionButton addScheduleFab;
    private TextView emptyStateText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ScheduleActivity onCreate started");
        
        try {
            setContentView(R.layout.activity_schedule);
            Log.d(TAG, "ScheduleActivity layout set successfully");
            
            // Initialize views
            initializeViews();
            
            // Setup toolbar
            setupToolbar();
            
            // Setup recycler view
            setupRecyclerView();
            
            // Setup click listeners
            setupClickListeners();
            
            Log.d(TAG, "ScheduleActivity onCreate completed successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in ScheduleActivity onCreate", e);
        }
    }
    
    private void initializeViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            scheduleRecyclerView = findViewById(R.id.schedule_recycler_view);
            addScheduleFab = findViewById(R.id.add_schedule_fab);
            emptyStateText = findViewById(R.id.empty_state_text);
            
            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
        }
    }
    
    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Schedule");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up toolbar", e);
        }
    }
    
    private void setupRecyclerView() {
        try {
            // TODO: Create schedule adapter and set it up
            // For now, just set up the layout manager
            scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } catch (Exception e) {
            Log.e(TAG, "Error setting up recycler view", e);
        }
    }
    
    private void setupClickListeners() {
        try {
            // Add schedule FAB
            if (addScheduleFab != null) {
                addScheduleFab.setOnClickListener(v -> {
                    showAddScheduleDialog();
                });
            }
            
            Log.d(TAG, "Click listeners setup successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners", e);
        }
    }
    
    private void showAddScheduleDialog() {
        try {
            // TODO: Show add schedule dialog
            Log.d(TAG, "Showing add schedule dialog");
        } catch (Exception e) {
            Log.e(TAG, "Error showing add schedule dialog", e);
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