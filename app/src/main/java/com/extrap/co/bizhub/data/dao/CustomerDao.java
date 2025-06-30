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
    int updateCustomer(Customer customer);
    
    @Delete
    int deleteCustomer(Customer customer);
    
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
    
    @Query("SELECT * FROM customers WHERE name LIKE '%' || :searchQuery || '%' OR email LIKE '%' || :searchQuery || '%' OR phoneNumber LIKE '%' || :searchQuery || '%'")
    List<Customer> searchCustomersSync(String searchQuery);
    
    @Query("SELECT * FROM customers WHERE name = :name LIMIT 1")
    Customer getCustomerByName(String name);
    
    @Query("SELECT * FROM customers WHERE customerType = :customerType")
    LiveData<List<Customer>> getCustomersByType(String customerType);
    
    @Query("SELECT * FROM customers WHERE isPremium = :isPremium")
    LiveData<List<Customer>> getCustomersByType(boolean isPremium);
    
    @Query("SELECT * FROM customers WHERE isPremium = :isPremium")
    List<Customer> getCustomersByTypeSync(boolean isPremium);
    
    @Query("SELECT COUNT(*) FROM customers WHERE isActive = 1")
    int getActiveCustomerCount();
    
    @Query("SELECT * FROM customers WHERE city = :city")
    LiveData<List<Customer>> getCustomersByCity(String city);
    
    @Query("UPDATE customers SET isActive = :isActive WHERE id = :customerId")
    void updateCustomerStatus(int customerId, boolean isActive);
    
    @Query("SELECT * FROM customers ORDER BY name ASC")
    LiveData<List<Customer>> getAllCustomersSorted();
    
    @Query("SELECT * FROM customers WHERE email = :email")
    Customer getCustomerByEmail(String email);
    
    @Query("SELECT * FROM customers WHERE phone = :phone")
    Customer getCustomerByPhone(String phone);
    
    @Query("DELETE FROM customers WHERE id = :id")
    void deleteCustomerById(long id);
    
    // Additional methods for CustomerViewModel
    @Query("SELECT * FROM customers WHERE isActive = :isActive")
    List<Customer> getCustomersByStatus(boolean isActive);
    
    @Query("SELECT * FROM customers WHERE isPremium = :isPremium")
    List<Customer> getPremiumCustomers(boolean isPremium);
    
    @Query("SELECT * FROM customers ORDER BY name ASC")
    List<Customer> getCustomersSortedByName();
    
    @Query("SELECT * FROM customers ORDER BY company ASC")
    List<Customer> getCustomersSortedByCompany();
    
    @Query("SELECT * FROM customers ORDER BY dateAdded DESC")
    List<Customer> getCustomersSortedByDate();
    
    @Query("SELECT * FROM customers")
    List<Customer> getAllCustomersSync();
    
    @Query("SELECT COUNT(*) FROM customers")
    LiveData<Integer> getCustomerCount();
    
    @Query("SELECT COUNT(*) FROM customers")
    int getCustomerCountSync();
    
    @Query("SELECT COUNT(*) FROM customers WHERE isPremium = 1")
    LiveData<Integer> getPremiumCustomerCount();
    
    @Query("SELECT * FROM customers WHERE id = :id")
    LiveData<Customer> getCustomerById(long id);
    
    @Query("UPDATE customers SET isActive = :isActive WHERE id = :customerId")
    int updateCustomerStatus(long customerId, boolean isActive);
    
    @Query("SELECT * FROM customers WHERE syncStatus = 'pending'")
    List<Customer> getPendingSyncCustomers();
    
    @Query("SELECT COUNT(*) FROM customers WHERE syncStatus = 'pending'")
    int getPendingSyncCustomerCount();
    
    @Query("DELETE FROM customers WHERE dateAdded < :timestamp")
    void deleteOldCustomers(long timestamp);
} 