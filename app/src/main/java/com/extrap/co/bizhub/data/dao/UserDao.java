package com.extrap.co.bizhub.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.extrap.co.bizhub.data.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(User user);
    
    @Update
    void updateUser(User user);
    
    @Delete
    void deleteUser(User user);
    
    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<User> getUserById(int userId);
    
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);
    
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User authenticateUser(String email, String password);
    
    @Query("SELECT * FROM users WHERE role = :role")
    LiveData<List<User>> getUsersByRole(String role);
    
    @Query("SELECT * FROM users WHERE isActive = 1")
    LiveData<List<User>> getAllActiveUsers();
    
    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();
    
    @Query("SELECT COUNT(*) FROM users WHERE role = 'technician' AND isActive = 1")
    int getActiveTechnicianCount();
    
    @Query("UPDATE users SET lastLoginAt = :timestamp WHERE id = :userId")
    void updateLastLogin(int userId, long timestamp);
    
    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserByIdSync(int userId);
    
    @Query("SELECT * FROM users WHERE email = :email")
    LiveData<User> getUserByEmailLive(String email);
    
    @Query("UPDATE users SET isActive = :isActive WHERE id = :userId")
    void updateUserStatus(int userId, boolean isActive);
    
    @Query("SELECT * FROM users WHERE role = 'technician' AND isActive = 1 ORDER BY firstName ASC")
    LiveData<List<User>> getActiveTechnicians();
    
    @Query("SELECT * FROM users ORDER BY first_name ASC")
    LiveData<List<User>> getAllUsers();
    
    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(long id);
    
    @Query("SELECT * FROM users WHERE first_name LIKE '%' || :searchQuery || '%' OR last_name LIKE '%' || :searchQuery || '%' OR email LIKE '%' || :searchQuery || '%'")
    List<User> searchUsers(String searchQuery);
    
    @Query("SELECT COUNT(*) FROM users WHERE role = :role")
    int getUsersCountByRole(String role);
    
    @Query("DELETE FROM users WHERE id = :id")
    void deleteUserById(long id);
} 