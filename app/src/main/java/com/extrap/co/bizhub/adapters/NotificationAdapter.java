package com.extrap.co.bizhub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.data.entities.NotificationItem;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    
    private List<NotificationItem> notifications;
    private final OnNotificationClickListener onNotificationClickListener;
    private final OnNotificationReadListener onNotificationReadListener;
    private final OnNotificationDeleteListener onNotificationDeleteListener;
    
    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationItem notification);
    }
    
    public interface OnNotificationReadListener {
        void onNotificationRead(NotificationItem notification);
    }
    
    public interface OnNotificationDeleteListener {
        void onNotificationDelete(NotificationItem notification);
    }
    
    public NotificationAdapter(List<NotificationItem> notifications,
                              OnNotificationClickListener onNotificationClickListener,
                              OnNotificationReadListener onNotificationReadListener,
                              OnNotificationDeleteListener onNotificationDeleteListener) {
        this.notifications = notifications;
        this.onNotificationClickListener = onNotificationClickListener;
        this.onNotificationReadListener = onNotificationReadListener;
        this.onNotificationDeleteListener = onNotificationDeleteListener;
    }
    
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem notification = notifications.get(position);
        holder.bind(notification);
    }
    
    @Override
    public int getItemCount() {
        return notifications.size();
    }
    
    public void updateNotifications(List<NotificationItem> newNotifications) {
        this.notifications = newNotifications;
        notifyDataSetChanged();
    }
    
    class NotificationViewHolder extends RecyclerView.ViewHolder {
        
        private MaterialCardView cardView;
        private TextView titleTextView;
        private TextView messageTextView;
        private TextView timeTextView;
        private TextView typeTextView;
        private ImageButton readButton;
        private ImageButton deleteButton;
        private View unreadIndicator;
        
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardView = itemView.findViewById(R.id.notification_card);
            titleTextView = itemView.findViewById(R.id.notification_title);
            messageTextView = itemView.findViewById(R.id.notification_message);
            timeTextView = itemView.findViewById(R.id.notification_time);
            typeTextView = itemView.findViewById(R.id.notification_type);
            readButton = itemView.findViewById(R.id.read_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
            unreadIndicator = itemView.findViewById(R.id.unread_indicator);
        }
        
        public void bind(NotificationItem notification) {
            titleTextView.setText(notification.getTitle());
            messageTextView.setText(notification.getMessage());
            
            // Format time
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
            timeTextView.setText(dateFormat.format(notification.getTimestamp()));
            
            // Set type label
            typeTextView.setText(getTypeLabel(notification.getType()));
            typeTextView.setBackgroundResource(getTypeBackground(notification.getType()));
            
            // Set unread indicator
            unreadIndicator.setVisibility(notification.isRead() ? View.GONE : View.VISIBLE);
            
            // Set card background based on read status
            if (notification.isRead()) {
                cardView.setCardBackgroundColor(itemView.getContext().getColor(R.color.card_background));
            } else {
                cardView.setCardBackgroundColor(itemView.getContext().getColor(R.color.primary_light));
            }
            
            // Set click listeners
            cardView.setOnClickListener(v -> {
                if (onNotificationClickListener != null) {
                    onNotificationClickListener.onNotificationClick(notification);
                }
            });
            
            readButton.setOnClickListener(v -> {
                if (onNotificationReadListener != null) {
                    onNotificationReadListener.onNotificationRead(notification);
                }
            });
            
            deleteButton.setOnClickListener(v -> {
                if (onNotificationDeleteListener != null) {
                    onNotificationDeleteListener.onNotificationDelete(notification);
                }
            });
            
            // Show/hide read button based on read status
            readButton.setVisibility(notification.isRead() ? View.GONE : View.VISIBLE);
        }
        
        private String getTypeLabel(String type) {
            switch (type) {
                case "work_order":
                    return "Work Order";
                case "alert":
                    return "Alert";
                case "sync":
                    return "Sync";
                case "general":
                    return "General";
                default:
                    return "Notification";
            }
        }
        
        private int getTypeBackground(String type) {
            switch (type) {
                case "work_order":
                    return R.color.chart_blue;
                case "alert":
                    return R.color.chart_red;
                case "sync":
                    return R.color.chart_green;
                case "general":
                    return R.color.chart_orange;
                default:
                    return R.color.chart_purple;
            }
        }
    }
} 