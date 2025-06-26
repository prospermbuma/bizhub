package com.extrap.co.bizhub.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.activities.DashboardActivity;
import com.extrap.co.bizhub.activities.WorkOrderDetailsActivity;
import com.extrap.co.bizhub.data.entities.WorkOrder;

import java.util.Random;

public class NotificationManager {
    
    private static final String CHANNEL_WORK_ORDERS = "work_orders";
    private static final String CHANNEL_GENERAL = "general";
    private static final String CHANNEL_SYNC = "sync";
    private static final String CHANNEL_ALERTS = "alerts";
    
    private Context context;
    private android.app.NotificationManager notificationManager;
    private PreferenceManager preferenceManager;
    
    public NotificationManager(Context context) {
        this.context = context;
        this.notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.preferenceManager = new PreferenceManager(context);
        createNotificationChannels();
    }
    
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Work Orders Channel
            NotificationChannel workOrderChannel = new NotificationChannel(
                CHANNEL_WORK_ORDERS,
                "Work Orders",
                android.app.NotificationManager.IMPORTANCE_HIGH
            );
            workOrderChannel.setDescription("Notifications for new work orders and updates");
            workOrderChannel.enableVibration(preferenceManager.areVibrationNotificationsEnabled());
            workOrderChannel.setShowBadge(true);
            
            // General Channel
            NotificationChannel generalChannel = new NotificationChannel(
                CHANNEL_GENERAL,
                "General",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            );
            generalChannel.setDescription("General app notifications");
            generalChannel.enableVibration(preferenceManager.areVibrationNotificationsEnabled());
            
            // Sync Channel
            NotificationChannel syncChannel = new NotificationChannel(
                CHANNEL_SYNC,
                "Sync",
                android.app.NotificationManager.IMPORTANCE_LOW
            );
            syncChannel.setDescription("Data synchronization notifications");
            syncChannel.enableVibration(false);
            
            // Alerts Channel
            NotificationChannel alertsChannel = new NotificationChannel(
                CHANNEL_ALERTS,
                "Alerts",
                android.app.NotificationManager.IMPORTANCE_HIGH
            );
            alertsChannel.setDescription("Important alerts and warnings");
            alertsChannel.enableVibration(true);
            alertsChannel.setShowBadge(true);
            
            notificationManager.createNotificationChannel(workOrderChannel);
            notificationManager.createNotificationChannel(generalChannel);
            notificationManager.createNotificationChannel(syncChannel);
            notificationManager.createNotificationChannel(alertsChannel);
        }
    }
    
    public void showNewWorkOrderNotification(WorkOrder workOrder) {
        if (!preferenceManager.areNotificationsEnabled()) {
            return;
        }
        
        Intent intent = new Intent(context, WorkOrderDetailsActivity.class);
        intent.putExtra("work_order_id", workOrder.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            workOrder.getId(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_WORK_ORDERS)
            .setSmallIcon(R.drawable.ic_notification_work_order)
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_foreground))
            .setContentTitle("New Work Order: " + workOrder.getWorkOrderNumber())
            .setContentText(workOrder.getDescription())
            .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Priority: " + workOrder.getPriority() + "\n" + workOrder.getDescription()))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);
        
        if (preferenceManager.areSoundNotificationsEnabled()) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(soundUri);
        }
        
        if (preferenceManager.areVibrationNotificationsEnabled()) {
            builder.setVibrate(new long[]{0, 500, 200, 500});
        }
        
        NotificationManagerCompat.from(context).notify(workOrder.getId(), builder.build());
    }
    
    public void showWorkOrderUpdateNotification(WorkOrder workOrder, String updateType) {
        if (!preferenceManager.areNotificationsEnabled()) {
            return;
        }
        
        Intent intent = new Intent(context, WorkOrderDetailsActivity.class);
        intent.putExtra("work_order_id", workOrder.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            workOrder.getId(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        String title = "Work Order Updated";
        String message = workOrder.getWorkOrderNumber() + " - " + updateType;
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_WORK_ORDERS)
            .setSmallIcon(R.drawable.ic_notification_update)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);
        
        if (preferenceManager.areSoundNotificationsEnabled()) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(soundUri);
        }
        
        NotificationManagerCompat.from(context).notify(workOrder.getId() + 1000, builder.build());
    }
    
    public void showOverdueWorkOrderAlert(WorkOrder workOrder) {
        if (!preferenceManager.areNotificationsEnabled()) {
            return;
        }
        
        Intent intent = new Intent(context, WorkOrderDetailsActivity.class);
        intent.putExtra("work_order_id", workOrder.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            workOrder.getId(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setSmallIcon(R.drawable.ic_notification_alert)
            .setContentTitle("⚠️ Overdue Work Order")
            .setContentText(workOrder.getWorkOrderNumber() + " is overdue")
            .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Work Order " + workOrder.getWorkOrderNumber() + " is overdue. Please update the status."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setOngoing(true);
        
        if (preferenceManager.areSoundNotificationsEnabled()) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            builder.setSound(soundUri);
        }
        
        if (preferenceManager.areVibrationNotificationsEnabled()) {
            builder.setVibrate(new long[]{0, 1000, 500, 1000, 500, 1000});
        }
        
        NotificationManagerCompat.from(context).notify(workOrder.getId() + 2000, builder.build());
    }
    
    public void showSyncNotification(String message, boolean isSuccess) {
        if (!preferenceManager.areNotificationsEnabled()) {
            return;
        }
        
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        String title = isSuccess ? "Sync Successful" : "Sync Failed";
        int icon = isSuccess ? R.drawable.ic_notification_sync_success : R.drawable.ic_notification_sync_error;
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_SYNC)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);
        
        NotificationManagerCompat.from(context).notify(new Random().nextInt(1000), builder.build());
    }
    
    public void showGeneralNotification(String title, String message) {
        if (!preferenceManager.areNotificationsEnabled()) {
            return;
        }
        
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_GENERAL)
            .setSmallIcon(R.drawable.ic_notification_general)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);
        
        if (preferenceManager.areSoundNotificationsEnabled()) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(soundUri);
        }
        
        NotificationManagerCompat.from(context).notify(new Random().nextInt(1000), builder.build());
    }
    
    public void showPriorityAlert(String title, String message) {
        if (!preferenceManager.areNotificationsEnabled()) {
            return;
        }
        
        Intent intent = new Intent(context, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ALERTS)
            .setSmallIcon(R.drawable.ic_notification_priority)
            .setContentTitle("🚨 " + title)
            .setContentText(message)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);
        
        if (preferenceManager.areSoundNotificationsEnabled()) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            builder.setSound(soundUri);
        }
        
        if (preferenceManager.areVibrationNotificationsEnabled()) {
            builder.setVibrate(new long[]{0, 500, 200, 500, 200, 500});
        }
        
        NotificationManagerCompat.from(context).notify(new Random().nextInt(1000), builder.build());
    }
    
    public void cancelNotification(int notificationId) {
        NotificationManagerCompat.from(context).cancel(notificationId);
    }
    
    public void cancelAllNotifications() {
        NotificationManagerCompat.from(context).cancelAll();
    }
    
    public void showWorkOrderReminder(WorkOrder workOrder) {
        if (!preferenceManager.areNotificationsEnabled()) {
            return;
        }
        
        Intent intent = new Intent(context, WorkOrderDetailsActivity.class);
        intent.putExtra("work_order_id", workOrder.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            workOrder.getId(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_WORK_ORDERS)
            .setSmallIcon(R.drawable.ic_notification_reminder)
            .setContentTitle("⏰ Work Order Reminder")
            .setContentText(workOrder.getWorkOrderNumber() + " is scheduled for today")
            .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Work Order " + workOrder.getWorkOrderNumber() + " is scheduled for today. Don't forget to check it!"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);
        
        if (preferenceManager.areSoundNotificationsEnabled()) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(soundUri);
        }
        
        if (preferenceManager.areVibrationNotificationsEnabled()) {
            builder.setVibrate(new long[]{0, 300, 200, 300});
        }
        
        NotificationManagerCompat.from(context).notify(workOrder.getId() + 3000, builder.build());
    }
} 