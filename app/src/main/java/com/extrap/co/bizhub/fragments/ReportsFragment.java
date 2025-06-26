package com.extrap.co.bizhub.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.viewmodels.ReportsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportsFragment extends Fragment {
    
    private ReportsViewModel viewModel;
    
    // Statistics Cards
    private TextView totalWorkOrdersText;
    private TextView completedWorkOrdersText;
    private TextView pendingWorkOrdersText;
    private TextView totalCustomersText;
    private TextView totalRevenueText;
    private TextView averageCompletionTimeText;
    
    // Charts
    private PieChart workOrderStatusChart;
    private BarChart workOrdersByMonthChart;
    private LineChart revenueTrendChart;
    private BarChart workOrdersByPriorityChart;
    
    // Filter Controls
    private ChipGroup periodChipGroup;
    private ChipGroup chartTypeChipGroup;
    
    private String currentPeriod = "month";
    private String currentChartType = "status";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupViewModel();
        setupCharts();
        setupFilters();
        observeData();
    }
    
    private void initializeViews(View view) {
        // Statistics Cards
        totalWorkOrdersText = view.findViewById(R.id.total_work_orders_text);
        completedWorkOrdersText = view.findViewById(R.id.completed_work_orders_text);
        pendingWorkOrdersText = view.findViewById(R.id.pending_work_orders_text);
        totalCustomersText = view.findViewById(R.id.total_customers_text);
        totalRevenueText = view.findViewById(R.id.total_revenue_text);
        averageCompletionTimeText = view.findViewById(R.id.average_completion_time_text);
        
        // Charts
        workOrderStatusChart = view.findViewById(R.id.work_order_status_chart);
        workOrdersByMonthChart = view.findViewById(R.id.work_orders_by_month_chart);
        revenueTrendChart = view.findViewById(R.id.revenue_trend_chart);
        workOrdersByPriorityChart = view.findViewById(R.id.work_orders_by_priority_chart);
        
        // Filter Controls
        periodChipGroup = view.findViewById(R.id.period_chip_group);
        chartTypeChipGroup = view.findViewById(R.id.chart_type_chip_group);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ReportsViewModel.class);
    }
    
    private void setupCharts() {
        setupWorkOrderStatusChart();
        setupWorkOrdersByMonthChart();
        setupRevenueTrendChart();
        setupWorkOrdersByPriorityChart();
    }
    
    private void setupWorkOrderStatusChart() {
        workOrderStatusChart.setDescription(null);
        workOrderStatusChart.setHoleRadius(35f);
        workOrderStatusChart.setTransparentCircleRadius(40f);
        workOrderStatusChart.setDrawHoleEnabled(true);
        workOrderStatusChart.setRotationAngle(0);
        workOrderStatusChart.setRotationEnabled(true);
        workOrderStatusChart.setHighlightPerTapEnabled(true);
        workOrderStatusChart.setEntryLabelTextSize(12f);
        workOrderStatusChart.setEntryLabelColor(Color.BLACK);
        workOrderStatusChart.getLegend().setEnabled(true);
        workOrderStatusChart.getLegend().setTextSize(12f);
        workOrderStatusChart.setDrawEntryLabels(true);
    }
    
    private void setupWorkOrdersByMonthChart() {
        workOrdersByMonthChart.setDescription(null);
        workOrdersByMonthChart.setDrawGridBackground(false);
        workOrdersByMonthChart.setDrawBarShadow(false);
        workOrdersByMonthChart.setHighlightFullBarEnabled(false);
        
        XAxis xAxis = workOrdersByMonthChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        
        YAxis leftAxis = workOrdersByMonthChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);
        
        YAxis rightAxis = workOrdersByMonthChart.getAxisRight();
        rightAxis.setEnabled(false);
        
        workOrdersByMonthChart.getLegend().setEnabled(true);
        workOrdersByMonthChart.setFitBars(true);
    }
    
    private void setupRevenueTrendChart() {
        revenueTrendChart.setDescription(null);
        revenueTrendChart.setDrawGridBackground(false);
        revenueTrendChart.setHighlightPerDragEnabled(true);
        revenueTrendChart.setTouchEnabled(true);
        revenueTrendChart.setDragEnabled(true);
        revenueTrendChart.setScaleEnabled(true);
        revenueTrendChart.setPinchZoom(true);
        
        XAxis xAxis = revenueTrendChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        
        YAxis leftAxis = revenueTrendChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);
        
        YAxis rightAxis = revenueTrendChart.getAxisRight();
        rightAxis.setEnabled(false);
        
        revenueTrendChart.getLegend().setEnabled(true);
    }
    
    private void setupWorkOrdersByPriorityChart() {
        workOrdersByPriorityChart.setDescription(null);
        workOrdersByPriorityChart.setDrawGridBackground(false);
        workOrdersByPriorityChart.setDrawBarShadow(false);
        workOrdersByPriorityChart.setHighlightFullBarEnabled(false);
        
        XAxis xAxis = workOrdersByPriorityChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        
        YAxis leftAxis = workOrdersByPriorityChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);
        
        YAxis rightAxis = workOrdersByPriorityChart.getAxisRight();
        rightAxis.setEnabled(false);
        
        workOrdersByPriorityChart.getLegend().setEnabled(true);
        workOrdersByPriorityChart.setFitBars(true);
    }
    
    private void setupFilters() {
        // Period filters
        addPeriodChip("Week", "week");
        addPeriodChip("Month", "month");
        addPeriodChip("Quarter", "quarter");
        addPeriodChip("Year", "year");
        
        // Chart type filters
        addChartTypeChip("Status", "status");
        addChartTypeChip("Trend", "trend");
        addChartTypeChip("Priority", "priority");
        addChartTypeChip("Revenue", "revenue");
        
        periodChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                currentPeriod = (String) chip.getTag();
                viewModel.updatePeriod(currentPeriod);
            }
        });
        
        chartTypeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                currentChartType = (String) chip.getTag();
                updateChartVisibility();
            }
        });
    }
    
    private void addPeriodChip(String text, String tag) {
        Chip chip = new Chip(requireContext());
        chip.setText(text);
        chip.setTag(tag);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(true);
        periodChipGroup.addView(chip);
        
        if (tag.equals("month")) {
            chip.setChecked(true);
        }
    }
    
    private void addChartTypeChip(String text, String tag) {
        Chip chip = new Chip(requireContext());
        chip.setText(text);
        chip.setTag(tag);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(true);
        chartTypeChipGroup.addView(chip);
        
        if (tag.equals("status")) {
            chip.setChecked(true);
        }
    }
    
    private void updateChartVisibility() {
        workOrderStatusChart.setVisibility(currentChartType.equals("status") ? View.VISIBLE : View.GONE);
        workOrdersByMonthChart.setVisibility(currentChartType.equals("trend") ? View.VISIBLE : View.GONE);
        workOrdersByPriorityChart.setVisibility(currentChartType.equals("priority") ? View.VISIBLE : View.GONE);
        revenueTrendChart.setVisibility(currentChartType.equals("revenue") ? View.VISIBLE : View.GONE);
    }
    
    private void observeData() {
        // Statistics
        viewModel.getTotalWorkOrders().observe(getViewLifecycleOwner(), count -> {
            totalWorkOrdersText.setText(String.valueOf(count));
        });
        
        viewModel.getCompletedWorkOrders().observe(getViewLifecycleOwner(), count -> {
            completedWorkOrdersText.setText(String.valueOf(count));
        });
        
        viewModel.getPendingWorkOrders().observe(getViewLifecycleOwner(), count -> {
            pendingWorkOrdersText.setText(String.valueOf(count));
        });
        
        viewModel.getTotalCustomers().observe(getViewLifecycleOwner(), count -> {
            totalCustomersText.setText(String.valueOf(count));
        });
        
        viewModel.getTotalRevenue().observe(getViewLifecycleOwner(), revenue -> {
            totalRevenueText.setText("$" + String.format("%,.0f", revenue));
        });
        
        viewModel.getAverageCompletionTime().observe(getViewLifecycleOwner(), time -> {
            averageCompletionTimeText.setText(time + " hours");
        });
        
        // Charts
        viewModel.getWorkOrderStatusData().observe(getViewLifecycleOwner(), data -> {
            updateWorkOrderStatusChart(data);
        });
        
        viewModel.getWorkOrdersByMonthData().observe(getViewLifecycleOwner(), data -> {
            updateWorkOrdersByMonthChart(data);
        });
        
        viewModel.getRevenueTrendData().observe(getViewLifecycleOwner(), data -> {
            updateRevenueTrendChart(data);
        });
        
        viewModel.getWorkOrdersByPriorityData().observe(getViewLifecycleOwner(), data -> {
            updateWorkOrdersByPriorityChart(data);
        });
    }
    
    private void updateWorkOrderStatusChart(List<Float> data) {
        List<PieEntry> entries = new ArrayList<>();
        String[] labels = {"Completed", "In Progress", "Pending", "Cancelled"};
        int[] colors = {Color.rgb(76, 175, 80), Color.rgb(33, 150, 243), Color.rgb(255, 152, 0), Color.rgb(244, 67, 54)};
        
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) > 0) {
                entries.add(new PieEntry(data.get(i), labels[i]));
            }
        }
        
        PieDataSet dataSet = new PieDataSet(entries, "Work Order Status");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueFormatter(new PercentFormatter(workOrderStatusChart));
        
        PieData pieData = new PieData(dataSet);
        workOrderStatusChart.setData(pieData);
        workOrderStatusChart.invalidate();
    }
    
    private void updateWorkOrdersByMonthChart(List<Float> data) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            entries.add(new BarEntry(i, data.get(i)));
        }
        
        BarDataSet dataSet = new BarDataSet(entries, "Work Orders");
        dataSet.setColor(Color.rgb(25, 118, 210));
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        
        BarData barData = new BarData(dataSet);
        workOrdersByMonthChart.setData(barData);
        
        // Set month labels
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        workOrdersByMonthChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
        
        workOrdersByMonthChart.invalidate();
    }
    
    private void updateRevenueTrendChart(List<Float> data) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            entries.add(new Entry(i, data.get(i)));
        }
        
        LineDataSet dataSet = new LineDataSet(entries, "Revenue");
        dataSet.setColor(Color.rgb(76, 175, 80));
        dataSet.setCircleColor(Color.rgb(76, 175, 80));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f);
        
        LineData lineData = new LineData(dataSet);
        revenueTrendChart.setData(lineData);
        
        // Set month labels
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        revenueTrendChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(months));
        
        revenueTrendChart.invalidate();
    }
    
    private void updateWorkOrdersByPriorityChart(List<Float> data) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            entries.add(new BarEntry(i, data.get(i)));
        }
        
        BarDataSet dataSet = new BarDataSet(entries, "Work Orders");
        dataSet.setColor(Color.rgb(255, 152, 0));
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        
        BarData barData = new BarData(dataSet);
        workOrdersByPriorityChart.setData(barData);
        
        // Set priority labels
        String[] priorities = {"Low", "Medium", "High", "Urgent"};
        workOrdersByPriorityChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(priorities));
        
        workOrdersByPriorityChart.invalidate();
    }
} 