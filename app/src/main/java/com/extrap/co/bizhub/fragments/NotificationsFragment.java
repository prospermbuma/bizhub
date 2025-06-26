package com.extrap.co.bizhub.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.adapters.NotificationAdapter;
import com.extrap.co.bizhub.data.entities.NotificationItem;
import com.extrap.co.bizhub.viewmodels.NotificationsViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    
    private NotificationsViewModel viewModel;
    private NotificationAdapter adapter;
    private RecyclerView recyclerView;
    private ChipGroup filterChipGroup;
    private TextView emptyStateText;
    
    private List<NotificationItem> allNotifications = new ArrayList<>();
    private String currentFilter = "all";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupViewModel();
        setupRecyclerView();
        setupFilters();
        observeData();
    }
    
    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.notifications_recycler_view);
        filterChipGroup = view.findViewById(R.id.filter_chip_group);
        emptyStateText = view.findViewById(R.id.empty_state_text);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
    }
    
    private void setupRecyclerView() {
        adapter = new NotificationAdapter(new ArrayList<>(), notification -> {
            // Handle notification click
            handleNotificationClick(notification);
        }, notification -> {
            // Handle notification mark as read
            viewModel.markAsRead(notification.getId());
        }, notification -> {
            // Handle notification delete
            viewModel.deleteNotification(notification.getId());
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupFilters() {
        // Add filter chips
        addFilterChip("All", "all");
        addFilterChip("Work Orders", "work_orders");
        addFilterChip("Alerts", "alerts");
        addFilterChip("Sync", "sync");
        addFilterChip("General", "general");
        
        filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                currentFilter = (String) chip.getTag();
                filterNotifications();
            }
        });
    }
    
    private void addFilterChip(String text, String tag) {
        Chip chip = new Chip(requireContext());
        chip.setText(text);
        chip.setTag(tag);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(true);
        filterChipGroup.addView(chip);
        
        if (tag.equals("all")) {
            chip.setChecked(true);
        }
    }
    
    private void observeData() {
        viewModel.getAllNotifications().observe(getViewLifecycleOwner(), notifications -> {
            allNotifications = notifications;
            filterNotifications();
        });
        
        viewModel.getFilteredNotifications().observe(getViewLifecycleOwner(), notifications -> {
            adapter.updateNotifications(notifications);
            updateEmptyState(notifications.isEmpty());
        });
        
        viewModel.getUnreadCount().observe(getViewLifecycleOwner(), count -> {
            // Update badge or unread count display
            updateUnreadBadge(count);
        });
    }
    
    private void filterNotifications() {
        List<NotificationItem> filteredNotifications = new ArrayList<>();
        
        for (NotificationItem notification : allNotifications) {
            boolean matchesFilter = matchesFilterCriteria(notification);
            if (matchesFilter) {
                filteredNotifications.add(notification);
            }
        }
        
        adapter.updateNotifications(filteredNotifications);
        updateEmptyState(filteredNotifications.isEmpty());
    }
    
    private boolean matchesFilterCriteria(NotificationItem notification) {
        switch (currentFilter) {
            case "work_orders":
                return "work_order".equals(notification.getType());
            case "alerts":
                return "alert".equals(notification.getType());
            case "sync":
                return "sync".equals(notification.getType());
            case "general":
                return "general".equals(notification.getType());
            default:
                return true; // "all" filter
        }
    }
    
    private void handleNotificationClick(NotificationItem notification) {
        // Mark as read
        viewModel.markAsRead(notification.getId());
        
        // Navigate based on notification type
        switch (notification.getType()) {
            case "work_order":
                navigateToWorkOrder(notification.getReferenceId());
                break;
            case "alert":
                // Handle alert navigation
                break;
            case "sync":
                // Handle sync navigation
                break;
            case "general":
                // Handle general notification
                break;
        }
    }
    
    private void navigateToWorkOrder(long workOrderId) {
        // TODO: Navigate to work order details
        // Intent intent = new Intent(getContext(), WorkOrderDetailsActivity.class);
        // intent.putExtra("work_order_id", workOrderId);
        // startActivity(intent);
    }
    
    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            emptyStateText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    private void updateUnreadBadge(int count) {
        // TODO: Update navigation badge or unread count display
        if (getActivity() instanceof com.extrap.co.bizhub.activities.DashboardActivity) {
            com.extrap.co.bizhub.activities.DashboardActivity activity = 
                (com.extrap.co.bizhub.activities.DashboardActivity) getActivity();
            // activity.updateNotificationBadge(count);
        }
    }
    
    public void markAllAsRead() {
        viewModel.markAllAsRead();
    }
    
    public void clearAllNotifications() {
        viewModel.clearAllNotifications();
    }
} 