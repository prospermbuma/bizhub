package com.extrap.co.bizhub.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.extrap.co.bizhub.data.entities.Customer;

import java.util.List;

@Dao
public interface CustomerDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCustomer(Customer customer);
    
    @Update
    void updateCustomer(Customer customer);
    
    @Delete
    void deleteCustomer(Customer customer);
    
    @Query("SELECT * FROM customers WHERE id = :customerId")
    LiveData<Customer> getCustomerById(int customerId);
    
    @Query("SELECT * FROM customers WHERE id = :customerId")
    Customer getCustomerByIdSync(int customerId);
    
    @Query("SELECT * FROM customers WHERE isActive = 1")
    LiveData<List<Customer>> getAllActiveCustomers();
    
    @Query("SELECT * FROM customers")
    LiveData<List<Customer>> getAllCustomers();
    
    @Query("SELECT * FROM customers WHERE name LIKE '%' || :searchQuery || '%' OR email LIKE '%' || :searchQuery || '%' OR phoneNumber LIKE '%' || :searchQuery || '%'")
    LiveData<List<Customer>> searchCustomers(String searchQuery);
    
    @Query("SELECT * FROM customers WHERE customerType = :customerType")
    LiveData<List<Customer>> getCustomersByType(String customerType);
    
    @Query("SELECT COUNT(*) FROM customers WHERE isActive = 1")
    int getActiveCustomerCount();
    
    @Query("SELECT * FROM customers WHERE city = :city")
    LiveData<List<Customer>> getCustomersByCity(String city);
    
    @Query("UPDATE customers SET isActive = :isActive WHERE id = :customerId")
    void updateCustomerStatus(int customerId, boolean isActive);
    
    @Query("SELECT * FROM customers ORDER BY name ASC")
    LiveData<List<Customer>> getAllCustomersSorted();
} 