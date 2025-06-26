package com.extrap.co.bizhub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.adapters.RecentActivityAdapter;
import com.extrap.co.bizhub.viewmodels.DashboardViewModel;
import com.google.android.material.card.MaterialCardView;

public class DashboardFragment extends Fragment {
    
    private DashboardViewModel viewModel;
    private RecentActivityAdapter recentActivityAdapter;
    
    // Dashboard cards
    private MaterialCardView todayTasksCard;
    private MaterialCardView pendingOrdersCard;
    private MaterialCardView completedTodayCard;
    private MaterialCardView totalCustomersCard;
    
    // Dashboard stats
    private android.widget.TextView todayTasksCount;
    private android.widget.TextView pendingOrdersCount;
    private android.widget.TextView completedTodayCount;
    private android.widget.TextView totalCustomersCount;
    
    // Recent activities
    private RecyclerView recentActivitiesRecyclerView;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupViewModel();
        setupRecyclerView();
        setupClickListeners();
        observeData();
    }
    
    private void initializeViews(View view) {
        // Dashboard cards
        todayTasksCard = view.findViewById(R.id.today_tasks_card);
        pendingOrdersCard = view.findViewById(R.id.pending_orders_card);
        completedTodayCard = view.findViewById(R.id.completed_today_card);
        totalCustomersCard = view.findViewById(R.id.total_customers_card);
        
        // Dashboard stats
        todayTasksCount = view.findViewById(R.id.today_tasks_count);
        pendingOrdersCount = view.findViewById(R.id.pending_orders_count);
        completedTodayCount = view.findViewById(R.id.completed_today_count);
        totalCustomersCount = view.findViewById(R.id.total_customers_count);
        
        // Recent activities
        recentActivitiesRecyclerView = view.findViewById(R.id.recent_activities_recycler_view);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
    }
    
    private void setupRecyclerView() {
        recentActivityAdapter = new RecentActivityAdapter();
        recentActivitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentActivitiesRecyclerView.setAdapter(recentActivityAdapter);
    }
    
    private void setupClickListeners() {
        todayTasksCard.setOnClickListener(v -> {
            // Navigate to work orders with today filter
            if (getActivity() instanceof com.extrap.co.bizhub.activities.DashboardActivity) {
                com.extrap.co.bizhub.activities.DashboardActivity activity = 
                    (com.extrap.co.bizhub.activities.DashboardActivity) getActivity();
                activity.loadFragment(new WorkOrdersFragment());
                activity.getSupportActionBar().setTitle("Today's Work Orders");
            }
        });
        
        pendingOrdersCard.setOnClickListener(v -> {
            // Navigate to work orders with pending filter
            if (getActivity() instanceof com.extrap.co.bizhub.activities.DashboardActivity) {
                com.extrap.co.bizhub.activities.DashboardActivity activity = 
                    (com.extrap.co.bizhub.activities.DashboardActivity) getActivity();
                activity.loadFragment(new WorkOrdersFragment());
                activity.getSupportActionBar().setTitle("Pending Work Orders");
            }
        });
        
        completedTodayCard.setOnClickListener(v -> {
            // Navigate to work orders with completed filter
            if (getActivity() instanceof com.extrap.co.bizhub.activities.DashboardActivity) {
                com.extrap.co.bizhub.activities.DashboardActivity activity = 
                    (com.extrap.co.bizhub.activities.DashboardActivity) getActivity();
                activity.loadFragment(new WorkOrdersFragment());
                activity.getSupportActionBar().setTitle("Completed Today");
            }
        });
        
        totalCustomersCard.setOnClickListener(v -> {
            // Navigate to customers
            if (getActivity() instanceof com.extrap.co.bizhub.activities.DashboardActivity) {
                com.extrap.co.bizhub.activities.DashboardActivity activity = 
                    (com.extrap.co.bizhub.activities.DashboardActivity) getActivity();
                activity.loadFragment(new CustomersFragment());
                activity.getSupportActionBar().setTitle(R.string.customers);
            }
        });
    }
    
    private void observeData() {
        // Observe dashboard stats
        viewModel.getTodayTasksCount().observe(getViewLifecycleOwner(), count -> {
            todayTasksCount.setText(String.valueOf(count));
        });
        
        viewModel.getPendingOrdersCount().observe(getViewLifecycleOwner(), count -> {
            pendingOrdersCount.setText(String.valueOf(count));
        });
        
        viewModel.getCompletedTodayCount().observe(getViewLifecycleOwner(), count -> {
            completedTodayCount.setText(String.valueOf(count));
        });
        
        viewModel.getTotalCustomersCount().observe(getViewLifecycleOwner(), count -> {
            totalCustomersCount.setText(String.valueOf(count));
        });
        
        // Observe recent activities
        viewModel.getRecentActivities().observe(getViewLifecycleOwner(), activities -> {
            recentActivityAdapter.updateActivities(activities);
        });
    }
} 