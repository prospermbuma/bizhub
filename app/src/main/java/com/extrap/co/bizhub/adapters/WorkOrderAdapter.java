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

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.ViewHolder> {
    
    private List<WorkOrder> workOrders;
    private SimpleDateFormat dateFormat;
    private OnItemClickListener onItemClickListener;
    
    public interface OnItemClickListener {
        void onItemClick(WorkOrder workOrder);
        void onStatusClick(WorkOrder workOrder);
    }
    
    public WorkOrderAdapter(List<WorkOrder> workOrders) {
        this.workOrders = workOrders != null ? workOrders : new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
    }
    
    public WorkOrderAdapter() {
        this.workOrders = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_work_order, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WorkOrder workOrder = workOrders.get(position);
        holder.bind(workOrder);
    }
    
    @Override
    public int getItemCount() {
        return workOrders.size();
    }
    
    public void updateWorkOrders(List<WorkOrder> newWorkOrders) {
        this.workOrders = newWorkOrders != null ? newWorkOrders : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView workOrderNumber;
        private TextView customerName;
        private TextView serviceType;
        private TextView description;
        private TextView scheduledDate;
        private Chip statusChip;
        private Chip priorityChip;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            workOrderNumber = itemView.findViewById(R.id.work_order_number);
            customerName = itemView.findViewById(R.id.customer_name);
            serviceType = itemView.findViewById(R.id.service_type);
            description = itemView.findViewById(R.id.description);
            scheduledDate = itemView.findViewById(R.id.scheduled_date);
            statusChip = itemView.findViewById(R.id.status_chip);
            priorityChip = itemView.findViewById(R.id.priority_chip);
            
            // Set click listeners
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(workOrders.get(position));
                }
            });
            
            statusChip.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onStatusClick(workOrders.get(position));
                }
            });
        }
        
        public void bind(WorkOrder workOrder) {
            workOrderNumber.setText(workOrder.getWorkOrderNumber());
            customerName.setText(workOrder.getCustomerName());
            serviceType.setText(workOrder.getServiceType());
            description.setText(workOrder.getDescription());
            scheduledDate.setText(dateFormat.format(new Date(workOrder.getScheduledDate())));
            
            // Set status chip
            statusChip.setText(workOrder.getStatus().replace("_", " ").toUpperCase());
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
            priorityChip.setText(workOrder.getPriority().toUpperCase());
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