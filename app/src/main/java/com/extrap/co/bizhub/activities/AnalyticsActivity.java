package com.extrap.co.bizhub.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.viewmodels.AnalyticsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {
    
    private AnalyticsViewModel viewModel;
    
    // Charts
    private BarChart workOrdersChart;
    private LineChart performanceChart;
    private PieChart statusChart;
    private PieChart priorityChart;
    
    // Stats Cards
    private MaterialCardView totalWorkOrdersCard;
    private MaterialCardView completedWorkOrdersCard;
    private MaterialCardView averageCompletionTimeCard;
    private MaterialCardView customerSatisfactionCard;
    
    // Stats TextViews
    private TextView totalWorkOrdersText;
    private TextView completedWorkOrdersText;
    private TextView averageCompletionTimeText;
    private TextView customerSatisfactionText;
    
    // Tab Layout
    private TabLayout tabLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);
        
        // Initialize views
        initializeViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup charts
        setupCharts();
        
        // Setup tabs
        setupTabs();
        
        // Observe data
        observeData();
        
        // Load analytics data
        viewModel.loadAnalyticsData();
    }
    
    private void initializeViews() {
        // Charts
        workOrdersChart = findViewById(R.id.work_orders_chart);
        performanceChart = findViewById(R.id.performance_chart);
        statusChart = findViewById(R.id.status_chart);
        priorityChart = findViewById(R.id.priority_chart);
        
        // Stats Cards
        totalWorkOrdersCard = findViewById(R.id.total_work_orders_card);
        completedWorkOrdersCard = findViewById(R.id.completed_work_orders_card);
        averageCompletionTimeCard = findViewById(R.id.average_completion_time_card);
        customerSatisfactionCard = findViewById(R.id.customer_satisfaction_card);
        
        // Stats TextViews
        totalWorkOrdersText = findViewById(R.id.total_work_orders_text);
        completedWorkOrdersText = findViewById(R.id.completed_work_orders_text);
        averageCompletionTimeText = findViewById(R.id.average_completion_time_text);
        customerSatisfactionText = findViewById(R.id.customer_satisfaction_text);
        
        // Tab Layout
        tabLayout = findViewById(R.id.tab_layout);
    }
    
    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Analytics & Reports");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupCharts() {
        // Setup Work Orders Bar Chart
        setupWorkOrdersChart();
        
        // Setup Performance Line Chart
        setupPerformanceChart();
        
        // Setup Status Pie Chart
        setupStatusChart();
        
        // Setup Priority Pie Chart
        setupPriorityChart();
    }
    
    private void setupWorkOrdersChart() {
        workOrdersChart.getDescription().setEnabled(false);
        workOrdersChart.setDrawGridBackground(false);
        workOrdersChart.setDrawBarShadow(false);
        workOrdersChart.setHighlightFullBarEnabled(false);
        
        XAxis xAxis = workOrdersChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        
        workOrdersChart.getAxisLeft().setDrawGridLines(true);
        workOrdersChart.getAxisRight().setEnabled(false);
        workOrdersChart.getLegend().setEnabled(true);
    }
    
    private void setupPerformanceChart() {
        performanceChart.getDescription().setEnabled(false);
        performanceChart.setDrawGridBackground(false);
        performanceChart.setTouchEnabled(true);
        performanceChart.setDragEnabled(true);
        performanceChart.setScaleEnabled(true);
        
        XAxis xAxis = performanceChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        
        performanceChart.getAxisLeft().setDrawGridLines(true);
        performanceChart.getAxisRight().setEnabled(false);
        performanceChart.getLegend().setEnabled(true);
    }
    
    private void setupStatusChart() {
        statusChart.getDescription().setEnabled(false);
        statusChart.setUsePercentValues(true);
        statusChart.getLegend().setEnabled(true);
        statusChart.setEntryLabelTextSize(12f);
        statusChart.setEntryLabelColor(Color.WHITE);
    }
    
    private void setupPriorityChart() {
        priorityChart.getDescription().setEnabled(false);
        priorityChart.setUsePercentValues(true);
        priorityChart.getLegend().setEnabled(true);
        priorityChart.setEntryLabelTextSize(12f);
        priorityChart.setEntryLabelColor(Color.WHITE);
    }
    
    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Overview"));
        tabLayout.addTab(tabLayout.newTab().setText("Performance"));
        tabLayout.addTab(tabLayout.newTab().setText("Trends"));
        tabLayout.addTab(tabLayout.newTab().setText("Reports"));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        showOverviewTab();
                        break;
                    case 1:
                        showPerformanceTab();
                        break;
                    case 2:
                        showTrendsTab();
                        break;
                    case 3:
                        showReportsTab();
                        break;
                }
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
    
    private void showOverviewTab() {
        // Show overview charts and stats
        workOrdersChart.setVisibility(android.view.View.VISIBLE);
        statusChart.setVisibility(android.view.View.VISIBLE);
        priorityChart.setVisibility(android.view.View.VISIBLE);
        performanceChart.setVisibility(android.view.View.GONE);
    }
    
    private void showPerformanceTab() {
        // Show performance charts
        performanceChart.setVisibility(android.view.View.VISIBLE);
        workOrdersChart.setVisibility(android.view.View.GONE);
        statusChart.setVisibility(android.view.View.GONE);
        priorityChart.setVisibility(android.view.View.GONE);
    }
    
    private void showTrendsTab() {
        // Show trend analysis
        performanceChart.setVisibility(android.view.View.VISIBLE);
        workOrdersChart.setVisibility(android.view.View.VISIBLE);
        statusChart.setVisibility(android.view.View.GONE);
        priorityChart.setVisibility(android.view.View.GONE);
    }
    
    private void showReportsTab() {
        // Show detailed reports
        workOrdersChart.setVisibility(android.view.View.VISIBLE);
        statusChart.setVisibility(android.view.View.VISIBLE);
        priorityChart.setVisibility(android.view.View.VISIBLE);
        performanceChart.setVisibility(android.view.View.VISIBLE);
    }
    
    private void observeData() {
        viewModel.getWorkOrdersByMonth().observe(this, data -> {
            updateWorkOrdersChart(data);
        });
        
        viewModel.getPerformanceData().observe(this, data -> {
            updatePerformanceChart(data);
        });
        
        viewModel.getStatusDistribution().observe(this, data -> {
            updateStatusChart(data);
        });
        
        viewModel.getPriorityDistribution().observe(this, data -> {
            updatePriorityChart(data);
        });
        
        viewModel.getAnalyticsStats().observe(this, stats -> {
            updateStatsCards(stats);
        });
    }
    
    private void updateWorkOrdersChart(List<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, "Work Orders");
        dataSet.setColor(Color.parseColor("#2196F3"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);
        
        BarData barData = new BarData(dataSet);
        workOrdersChart.setData(barData);
        workOrdersChart.invalidate();
    }
    
    private void updatePerformanceChart(List<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, "Completion Time (Hours)");
        dataSet.setColor(Color.parseColor("#4CAF50"));
        dataSet.setCircleColor(Color.parseColor("#4CAF50"));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);
        
        LineData lineData = new LineData(dataSet);
        performanceChart.setData(lineData);
        performanceChart.invalidate();
    }
    
    private void updateStatusChart(List<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "Work Order Status");
        dataSet.setColors(Color.parseColor("#4CAF50"), Color.parseColor("#FF9800"), 
                         Color.parseColor("#2196F3"), Color.parseColor("#F44336"));
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(statusChart));
        statusChart.setData(pieData);
        statusChart.invalidate();
    }
    
    private void updatePriorityChart(List<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "Work Order Priority");
        dataSet.setColors(Color.parseColor("#F44336"), Color.parseColor("#FF9800"), 
                         Color.parseColor("#2196F3"), Color.parseColor("#4CAF50"));
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(priorityChart));
        priorityChart.setData(pieData);
        priorityChart.invalidate();
    }
    
    private void updateStatsCards(AnalyticsViewModel.AnalyticsStats stats) {
        totalWorkOrdersText.setText(String.valueOf(stats.getTotalWorkOrders()));
        completedWorkOrdersText.setText(String.valueOf(stats.getCompletedWorkOrders()));
        averageCompletionTimeText.setText(String.format("%.1f hours", stats.getAverageCompletionTime()));
        customerSatisfactionText.setText(String.format("%.1f%%", stats.getCustomerSatisfaction()));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_analytics, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_export_report) {
            exportReport();
            return true;
        } else if (id == R.id.action_share_report) {
            shareReport();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void exportReport() {
        // TODO: Implement report export functionality
        Toast.makeText(this, "Exporting report...", Toast.LENGTH_SHORT).show();
    }
    
    private void shareReport() {
        // TODO: Implement report sharing functionality
        Toast.makeText(this, "Sharing report...", Toast.LENGTH_SHORT).show();
    }
} 