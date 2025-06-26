package com.extrap.co.bizhub.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.extrap.co.bizhub.data.dao.CustomerDao;
import com.extrap.co.bizhub.data.dao.UserDao;
import com.extrap.co.bizhub.data.dao.WorkOrderDao;
import com.extrap.co.bizhub.data.entities.Customer;
import com.extrap.co.bizhub.data.entities.User;
import com.extrap.co.bizhub.data.entities.WorkOrder;

@Database(
    entities = {User.class, Customer.class, WorkOrder.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "field_service_db";
    private static volatile AppDatabase INSTANCE;
    
    // DAOs
    public abstract UserDao userDao();
    public abstract CustomerDao customerDao();
    public abstract WorkOrderDao workOrderDao();
    
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
} 