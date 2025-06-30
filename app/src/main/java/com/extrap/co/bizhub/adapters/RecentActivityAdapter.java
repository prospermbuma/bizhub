package com.extrap.co.bizhub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.data.entities.WorkOrder;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecentActivityAdapter extends RecyclerView.Adapter<RecentActivityAdapter.ViewHolder> {
    
    private List<WorkOrder> activities;
    private SimpleDateFormat dateFormat;
    
    public RecentActivityAdapter() {
        this.activities = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
    }
    
    public RecentActivityAdapter(List<WorkOrder> activities) {
        this.activities = activities;
        this.dateFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_recent_activity, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkOrder workOrder = activities.get(position);
        holder.bind(workOrder);
    }
    
    @Override
    public int getItemCount() {
        return activities.size();
    }
    
    public void updateActivities(List<WorkOrder> newActivities) {
        this.activities = newActivities;
        notifyDataSetChanged();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView workOrderNumber;
        private TextView customerName;
        private TextView serviceType;
        private TextView dateTime;
        private Chip statusChip;
        private Chip priorityChip;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workOrderNumber = itemView.findViewById(R.id.work_order_number);
            customerName = itemView.findViewById(R.id.customer_name);
            serviceType = itemView.findViewById(R.id.service_type);
            dateTime = itemView.findViewById(R.id.date_time);
            statusChip = itemView.findViewById(R.id.status_chip);
            priorityChip = itemView.findViewById(R.id.priority_chip);
        }
        
        public void bind(WorkOrder workOrder) {
            workOrderNumber.setText(workOrder.getWorkOrderNumber());
            customerName.setText(workOrder.getDescription());
            serviceType.setText(workOrder.getServiceType());
            dateTime.setText(dateFormat.format(new Date(workOrder.getCreatedAt())));
            
            // Set status chip
            statusChip.setText(workOrder.getStatus());
            switch (workOrder.getStatus()) {
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
            priorityChip.setText(workOrder.getPriority());
            switch (workOrder.getPriority()) {
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
    }
} 