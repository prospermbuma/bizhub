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
import com.extrap.co.bizhub.adapters.WorkOrderAdapter;
import com.extrap.co.bizhub.data.entities.WorkOrder;
import com.extrap.co.bizhub.viewmodels.ReportsViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    
    private static final String TAG = "ReportActivity";
    
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private MaterialCardView dailyReportCard;
    private MaterialCardView weeklyReportCard;
    private MaterialCardView monthlyReportCard;
    private RecyclerView workOrdersRecyclerView;
    private WorkOrderAdapter workOrderAdapter;
    private ReportsViewModel viewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ReportActivity onCreate started");
        
        try {
            setContentView(R.layout.activity_report);
            Log.d(TAG, "ReportActivity layout set successfully");
            
            // Initialize ViewModel
            viewModel = new ViewModelProvider(this).get(ReportsViewModel.class);
            
            // Initialize views
            initializeViews();
            
            // Setup toolbar
            setupToolbar();
            
            // Setup tabs
            setupTabs();
            
            // Setup recycler view
            setupRecyclerView();
            
            // Setup click listeners
            setupClickListeners();
            
            // Observe data
            observeData();
            
            Log.d(TAG, "ReportActivity onCreate completed successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in ReportActivity onCreate", e);
        }
    }
    
    private void initializeViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            tabLayout = findViewById(R.id.tab_layout);
            dailyReportCard = findViewById(R.id.daily_report_card);
            weeklyReportCard = findViewById(R.id.weekly_report_card);
            monthlyReportCard = findViewById(R.id.monthly_report_card);
            workOrdersRecyclerView = findViewById(R.id.work_orders_recycler_view);
            
            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
        }
    }
    
    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.reports);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up toolbar", e);
        }
    }
    
    private void setupTabs() {
        try {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.daily_report));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.weekly_report));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.monthly_report));
            
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    loadReportForTab(tab.getPosition());
                }
                
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}
                
                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        } catch (Exception e) {
            Log.e(TAG, "Error setting up tabs", e);
        }
    }
    
    private void setupRecyclerView() {
        try {
            workOrderAdapter = new WorkOrderAdapter(new ArrayList<>(), workOrder -> {
                // Handle work order click
                Intent intent = new Intent(ReportActivity.this, WorkOrderDetailsActivity.class);
                intent.putExtra("work_order_id", workOrder.getId());
                startActivity(intent);
            });
            
            workOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            workOrdersRecyclerView.setAdapter(workOrderAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up recycler view", e);
        }
    }
    
    private void setupClickListeners() {
        try {
            // Daily report card
            if (dailyReportCard != null) {
                dailyReportCard.setOnClickListener(v -> {
                    tabLayout.selectTab(tabLayout.getTabAt(0));
                });
            }
            
            // Weekly report card
            if (weeklyReportCard != null) {
                weeklyReportCard.setOnClickListener(v -> {
                    tabLayout.selectTab(tabLayout.getTabAt(1));
                });
            }
            
            // Monthly report card
            if (monthlyReportCard != null) {
                monthlyReportCard.setOnClickListener(v -> {
                    tabLayout.selectTab(tabLayout.getTabAt(2));
                });
            }
            
            Log.d(TAG, "Click listeners setup successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners", e);
        }
    }
    
    private void loadReportForTab(int position) {
        try {
            switch (position) {
                case 0: // Daily
                    viewModel.loadDailyReport();
                    break;
                case 1: // Weekly
                    viewModel.loadWeeklyReport();
                    break;
                case 2: // Monthly
                    viewModel.loadMonthlyReport();
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading report for tab", e);
        }
    }
    
    private void observeData() {
        try {
            // Observe work orders
            viewModel.getWorkOrders().observe(this, workOrders -> {
                if (workOrders != null) {
                    workOrderAdapter.updateWorkOrders(workOrders);
                }
            });
            
            // Observe report stats
            viewModel.getRevenue().observe(this, revenue -> {
                // Update revenue display
            });
            
            viewModel.getJobsCompleted().observe(this, jobsCompleted -> {
                // Update jobs completed display
            });
            
            viewModel.getCustomerSatisfaction().observe(this, satisfaction -> {
                // Update customer satisfaction display
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