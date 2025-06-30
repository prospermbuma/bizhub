package com.extrap.co.bizhub.utils;

import android.content.Context;
import android.util.Log;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.AppDatabase;
import com.extrap.co.bizhub.data.dao.CustomerDao;
import com.extrap.co.bizhub.data.dao.UserDao;
import com.extrap.co.bizhub.data.dao.WorkOrderDao;
import com.extrap.co.bizhub.data.entities.Customer;
import com.extrap.co.bizhub.data.entities.User;
import com.extrap.co.bizhub.data.entities.WorkOrder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestUtils {
    
    private static final String TAG = "TestUtils";
    private static final boolean ENABLE_TEST_MODE = true; // Set to false for production
    
    private Context context;
    private AppDatabase database;
    private ExecutorService executorService;
    
    public TestUtils(Context context) {
        this.context = context;
        this.database = FieldServiceApp.getInstance().getDatabase();
        this.executorService = Executors.newFixedThreadPool(2);
    }
    
    public static boolean isTestModeEnabled() {
        return ENABLE_TEST_MODE;
    }
    
    public void populateTestData() {
        if (!ENABLE_TEST_MODE) {
            Log.w(TAG, "Test mode is disabled");
            return;
        }
        
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Populating test data...");
                
                // Clear existing data
                clearAllData();
                
                // Add test users
                addTestUsers();
                
                // Add test customers
                addTestCustomers();
                
                // Add test work orders
                addTestWorkOrders();
                
                Log.d(TAG, "Test data populated successfully");
                
            } catch (Exception e) {
                Log.e(TAG, "Error populating test data", e);
            }
        });
    }
    
    private void clearAllData() {
        try {
            database.clearAllTables();
            Log.d(TAG, "All tables cleared");
        } catch (Exception e) {
            Log.e(TAG, "Error clearing tables", e);
        }
    }
    
    private void addTestUsers() {
        try {
            UserDao userDao = database.userDao();
            
            User user1 = new User();
            user1.setEmail("admin@bizhub.com");
            user1.setPassword("admin123");
            user1.setName("Admin User");
            user1.setRole("admin");
            user1.setPhone("+1234567890");
            userDao.insertUser(user1);
            
            User user2 = new User();
            user2.setEmail("tech@bizhub.com");
            user2.setPassword("tech123");
            user2.setName("John Technician");
            user2.setRole("technician");
            user2.setPhone("+1234567891");
            userDao.insertUser(user2);
            
            User user3 = new User();
            user3.setEmail("manager@bizhub.com");
            user3.setPassword("manager123");
            user3.setName("Sarah Manager");
            user3.setRole("manager");
            user3.setPhone("+1234567892");
            userDao.insertUser(user3);
            
            Log.d(TAG, "Test users added");
            
        } catch (Exception e) {
            Log.e(TAG, "Error adding test users", e);
        }
    }
    
    private void addTestCustomers() {
        try {
            CustomerDao customerDao = database.customerDao();
            
            Customer customer1 = new Customer();
            customer1.setName("ABC Manufacturing");
            customer1.setEmail("contact@abcmanufacturing.com");
            customer1.setPhone("+1987654321");
            customer1.setAddress("123 Industrial Blvd, City, State 12345");
            customer1.setContactPerson("Mike Johnson");
            customer1.setCompanySize("Large");
            customer1.setIndustry("Manufacturing");
            customerDao.insertCustomer(customer1);
            
            Customer customer2 = new Customer();
            customer2.setName("XYZ Services");
            customer2.setEmail("info@xyzservices.com");
            customer2.setPhone("+1987654322");
            customer2.setAddress("456 Business Ave, City, State 12345");
            customer2.setContactPerson("Lisa Smith");
            customer2.setCompanySize("Medium");
            customer2.setIndustry("Services");
            customerDao.insertCustomer(customer2);
            
            Customer customer3 = new Customer();
            customer3.setName("Tech Solutions Inc");
            customer3.setEmail("support@techsolutions.com");
            customer3.setPhone("+1987654323");
            customer3.setAddress("789 Tech Park, City, State 12345");
            customer3.setContactPerson("David Wilson");
            customer3.setCompanySize("Small");
            customer3.setIndustry("Technology");
            customerDao.insertCustomer(customer3);
            
            Log.d(TAG, "Test customers added");
            
        } catch (Exception e) {
            Log.e(TAG, "Error adding test customers", e);
        }
    }
    
    private void addTestWorkOrders() {
        try {
            WorkOrderDao workOrderDao = database.workOrderDao();
            
            // Get customer IDs
            Customer customer1 = database.customerDao().getCustomerByName("ABC Manufacturing");
            Customer customer2 = database.customerDao().getCustomerByName("XYZ Services");
            Customer customer3 = database.customerDao().getCustomerByName("Tech Solutions Inc");
            
            long currentTime = System.currentTimeMillis();
            long dayInMillis = 24 * 60 * 60 * 1000L;
            
            // Work Order 1 - Pending
            WorkOrder workOrder1 = new WorkOrder();
            workOrder1.setWorkOrderNumber("WO-001");
            workOrder1.setCustomerId(customer1 != null ? customer1.getId() : 1);
            workOrder1.setServiceType("repair");
            workOrder1.setPriority("high");
            workOrder1.setStatus("pending");
            workOrder1.setDescription("Equipment malfunction - urgent repair needed");
            workOrder1.setLocation("Production Line A");
            workOrder1.setScheduledDate(currentTime + dayInMillis);
            workOrder1.setDueDate(currentTime + 2 * dayInMillis);
            workOrder1.setEstimatedDuration(4.0);
            workOrder1.setEstimatedCost(500.0);
            workOrderDao.insertWorkOrder(workOrder1);
            
            // Work Order 2 - In Progress
            WorkOrder workOrder2 = new WorkOrder();
            workOrder2.setWorkOrderNumber("WO-002");
            workOrder2.setCustomerId(customer2 != null ? customer2.getId() : 2);
            workOrder2.setServiceType("maintenance");
            workOrder2.setPriority("medium");
            workOrder2.setStatus("in_progress");
            workOrder2.setDescription("Regular maintenance check");
            workOrder2.setLocation("Main Office");
            workOrder2.setScheduledDate(currentTime - dayInMillis);
            workOrder2.setStartTime(currentTime - 2 * 60 * 60 * 1000L); // Started 2 hours ago
            workOrder2.setEstimatedDuration(2.0);
            workOrder2.setEstimatedCost(200.0);
            workOrderDao.insertWorkOrder(workOrder2);
            
            // Work Order 3 - Completed
            WorkOrder workOrder3 = new WorkOrder();
            workOrder3.setWorkOrderNumber("WO-003");
            workOrder3.setCustomerId(customer3 != null ? customer3.getId() : 3);
            workOrder3.setServiceType("installation");
            workOrder3.setPriority("low");
            workOrder3.setStatus("completed");
            workOrder3.setDescription("New equipment installation");
            workOrder3.setLocation("Server Room");
            workOrder3.setScheduledDate(currentTime - 3 * dayInMillis);
            workOrder3.setStartTime(currentTime - 3 * dayInMillis);
            workOrder3.setCompletionTime(currentTime - 2 * dayInMillis);
            workOrder3.setActualDuration(3.5);
            workOrder3.setActualCost(350.0);
            workOrderDao.insertWorkOrder(workOrder3);
            
            // Work Order 4 - High Priority
            WorkOrder workOrder4 = new WorkOrder();
            workOrder4.setWorkOrderNumber("WO-004");
            workOrder4.setCustomerId(customer1 != null ? customer1.getId() : 1);
            workOrder4.setServiceType("repair");
            workOrder4.setPriority("urgent");
            workOrder4.setStatus("pending");
            workOrder4.setDescription("Critical system failure - immediate attention required");
            workOrder4.setLocation("Control Room");
            workOrder4.setScheduledDate(currentTime);
            workOrder4.setDueDate(currentTime + 4 * 60 * 60 * 1000L); // 4 hours from now
            workOrder4.setEstimatedDuration(6.0);
            workOrder4.setEstimatedCost(800.0);
            workOrder4.setUrgent(true);
            workOrderDao.insertWorkOrder(workOrder4);
            
            Log.d(TAG, "Test work orders added");
            
        } catch (Exception e) {
            Log.e(TAG, "Error adding test work orders", e);
        }
    }
    
    public void runDatabaseTests() {
        if (!ENABLE_TEST_MODE) {
            return;
        }
        
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Running database tests...");
                
                // Test user queries
                testUserQueries();
                
                // Test customer queries
                testCustomerQueries();
                
                // Test work order queries
                testWorkOrderQueries();
                
                Log.d(TAG, "Database tests completed");
                
            } catch (Exception e) {
                Log.e(TAG, "Error running database tests", e);
            }
        });
    }
    
    private void testUserQueries() {
        try {
            UserDao userDao = database.userDao();
            
            // Test user count
            int userCount = userDao.getUserCount();
            Log.d(TAG, "User count: " + userCount);
            
            // Test user by email
            User user = userDao.getUserByEmail("admin@bizhub.com");
            if (user != null) {
                Log.d(TAG, "Found user: " + user.getName());
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error testing user queries", e);
        }
    }
    
    private void testCustomerQueries() {
        try {
            CustomerDao customerDao = database.customerDao();
            
            // Test customer count
            int customerCount = customerDao.getCustomerCountSync();
            Log.d(TAG, "Customer count: " + customerCount);
            
            // Test customer by name
            Customer customer = customerDao.getCustomerByName("ABC Manufacturing");
            if (customer != null) {
                Log.d(TAG, "Found customer: " + customer.getName());
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error testing customer queries", e);
        }
    }
    
    private void testWorkOrderQueries() {
        try {
            WorkOrderDao workOrderDao = database.workOrderDao();
            
            // Test work order count
            int workOrderCount = workOrderDao.getWorkOrderCount();
            Log.d(TAG, "Work order count: " + workOrderCount);
            
            // Test pending work orders
            int pendingCount = workOrderDao.getPendingWorkOrderCount();
            Log.d(TAG, "Pending work orders: " + pendingCount);
            
            // Test high priority work orders
            int highPriorityCount = workOrderDao.getHighPriorityWorkOrderCount();
            Log.d(TAG, "High priority work orders: " + highPriorityCount);
            
        } catch (Exception e) {
            Log.e(TAG, "Error testing work order queries", e);
        }
    }
    
    public void cleanup() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
} 