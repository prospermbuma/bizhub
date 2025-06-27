package com.extrap.co.bizhub.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.entities.User;
import com.extrap.co.bizhub.utils.PreferenceManager;
import com.extrap.co.bizhub.utils.PasswordUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileViewModel extends ViewModel {
    
    private MutableLiveData<User> currentUser;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<Boolean> isProfileUpdated;
    
    private ExecutorService executorService;
    private PreferenceManager preferenceManager;
    
    public ProfileViewModel() {
        currentUser = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();
        isProfileUpdated = new MutableLiveData<>(false);
        
        executorService = FieldServiceApp.getInstance().getExecutorService();
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        
        // Load current user data
        loadCurrentUser();
    }
    
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Boolean> getIsProfileUpdated() {
        return isProfileUpdated;
    }
    
    private void loadCurrentUser() {
        isLoading.setValue(true);
        
        executorService.execute(() -> {
            try {
                long userId = preferenceManager.getUserId();
                if (userId > 0) {
                    User user = FieldServiceApp.getInstance().getDatabase()
                        .userDao().getUserById(userId);
                    currentUser.postValue(user);
                }
                isLoading.postValue(false);
            } catch (Exception e) {
                errorMessage.postValue("Error loading user data: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }
    
    public void updateProfile(String firstName, String lastName, String email, String phone) {
        isLoading.setValue(true);
        
        executorService.execute(() -> {
            try {
                User user = currentUser.getValue();
                if (user != null) {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setEmail(email);
                    user.setPhone(phone);
                    
                    // Update in database
                    FieldServiceApp.getInstance().getDatabase()
                        .userDao().updateUser(user);
                    
                    // Update current user
                    currentUser.postValue(user);
                    
                    // Update preferences
                    preferenceManager.setUserName(firstName + " " + lastName);
                    preferenceManager.setUserEmail(email);
                    
                    isProfileUpdated.postValue(true);
                }
                isLoading.postValue(false);
            } catch (Exception e) {
                errorMessage.postValue("Error updating profile: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }
    
    public void changePassword(String currentPassword, String newPassword) {
        isLoading.setValue(true);
        
        executorService.execute(() -> {
            try {
                User user = currentUser.getValue();
                if (user != null && PasswordUtils.verifyPassword(currentPassword, user.getPassword())) {
                    user.setPassword(PasswordUtils.hashPassword(newPassword));
                    
                    // Update in database
                    FieldServiceApp.getInstance().getDatabase()
                        .userDao().updateUser(user);
                    
                    // Update current user
                    currentUser.postValue(user);
                    
                    isProfileUpdated.postValue(true);
                } else {
                    errorMessage.postValue("Current password is incorrect");
                }
                isLoading.postValue(false);
            } catch (Exception e) {
                errorMessage.postValue("Error changing password: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }
    
    public void updateProfilePicture(String imagePath) {
        // TODO: Implement profile picture update
        // This would involve uploading the image to storage
        // and updating the user's profile picture URL
    }
    
    public void clearProfileUpdateFlag() {
        isProfileUpdated.setValue(false);
    }
    
    public void resetPasswordWithSecurityAnswer(String email, String securityAnswer, String newPassword) {
        isLoading.setValue(true);
        executorService.execute(() -> {
            try {
                User user = FieldServiceApp.getInstance().getDatabase().userDao().getUserByEmail(email);
                if (user != null && user.getSecurityAnswer() != null && user.getSecurityAnswer().equalsIgnoreCase(securityAnswer.trim())) {
                    user.setPassword(PasswordUtils.hashPassword(newPassword));
                    FieldServiceApp.getInstance().getDatabase().userDao().updateUser(user);
                    currentUser.postValue(user);
                    isProfileUpdated.postValue(true);
                } else {
                    errorMessage.postValue("Security answer is incorrect");
                }
                isLoading.postValue(false);
            } catch (Exception e) {
                errorMessage.postValue("Error resetting password: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }
} 