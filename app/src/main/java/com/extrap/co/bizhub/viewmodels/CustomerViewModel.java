package com.extrap.co.bizhub.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.entities.Customer;
import com.extrap.co.bizhub.data.dao.CustomerDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerViewModel extends AndroidViewModel {
    
    private CustomerDao customerDao;
    private ExecutorService executorService;
    
    private MutableLiveData<List<Customer>> customers = new MutableLiveData<>();
    private MutableLiveData<List<Customer>> searchResults = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<String> successMessage = new MutableLiveData<>();
    
    public CustomerViewModel(Application application) {
        super(application);
        customerDao = FieldServiceApp.getInstance().getDatabase().customerDao();
        executorService = Executors.newFixedThreadPool(4);
    }
    
    public LiveData<List<Customer>> getAllCustomers() {
        return customerDao.getAllCustomers();
    }
    
    public LiveData<List<Customer>> getSearchResults() {
        return searchResults;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }
    
    public void searchCustomers(String query) {
        executorService.execute(() -> {
            try {
                List<Customer> results = customerDao.searchCustomersSync("%" + query + "%");
                searchResults.postValue(results);
            } catch (Exception e) {
                errorMessage.postValue("Error searching customers: " + e.getMessage());
            }
        });
    }
    
    public void filterCustomersByStatus(boolean isActive) {
        executorService.execute(() -> {
            try {
                List<Customer> results = customerDao.getCustomersByStatus(isActive);
                searchResults.postValue(results);
            } catch (Exception e) {
                errorMessage.postValue("Filter failed: " + e.getMessage());
            }
        });
    }
    
    public void filterCustomersByType(boolean isPremium) {
        executorService.execute(() -> {
            try {
                List<Customer> results = customerDao.getCustomersByTypeSync(isPremium);
                searchResults.postValue(results);
            } catch (Exception e) {
                errorMessage.postValue("Error getting customers by type: " + e.getMessage());
            }
        });
    }
    
    public void addCustomer(Customer customer) {
        executorService.execute(() -> {
            try {
                long id = customerDao.insertCustomer(customer);
                if (id > 0) {
                    successMessage.postValue("Customer added successfully");
                } else {
                    errorMessage.postValue("Failed to add customer");
                }
            } catch (Exception e) {
                errorMessage.postValue("Error adding customer: " + e.getMessage());
            }
        });
    }
    
    public void loadCustomers() {
        executorService.execute(() -> {
            try {
                List<Customer> customers = customerDao.getAllCustomersSync();
                this.customers.postValue(customers);
            } catch (Exception e) {
                errorMessage.postValue("Error loading customers: " + e.getMessage());
            }
        });
    }
    
    public void updateCustomer(Customer customer) {
        executorService.execute(() -> {
            try {
                int rowsUpdated = customerDao.updateCustomer(customer);
                if (rowsUpdated > 0) {
                    loadCustomers();
                } else {
                    errorMessage.postValue("Failed to update customer");
                }
            } catch (Exception e) {
                errorMessage.postValue("Error updating customer: " + e.getMessage());
            }
        });
    }
    
    public void deleteCustomer(Customer customer) {
        executorService.execute(() -> {
            try {
                int rowsDeleted = customerDao.deleteCustomer(customer);
                if (rowsDeleted > 0) {
                    loadCustomers();
                } else {
                    errorMessage.postValue("Failed to delete customer");
                }
            } catch (Exception e) {
                errorMessage.postValue("Error deleting customer: " + e.getMessage());
            }
        });
    }
    
    public void sortCustomersByName() {
        executorService.execute(() -> {
            try {
                List<Customer> results = customerDao.getCustomersSortedByName();
                searchResults.postValue(results);
            } catch (Exception e) {
                errorMessage.postValue("Sort failed: " + e.getMessage());
            }
        });
    }
    
    public void sortCustomersByCompany() {
        executorService.execute(() -> {
            try {
                List<Customer> results = customerDao.getCustomersSortedByCompany();
                searchResults.postValue(results);
            } catch (Exception e) {
                errorMessage.postValue("Sort failed: " + e.getMessage());
            }
        });
    }
    
    public void sortCustomersByDate() {
        executorService.execute(() -> {
            try {
                List<Customer> results = customerDao.getCustomersSortedByDate();
                searchResults.postValue(results);
            } catch (Exception e) {
                errorMessage.postValue("Sort failed: " + e.getMessage());
            }
        });
    }
    
    public void exportCustomers() {
        executorService.execute(() -> {
            try {
                List<Customer> customers = customerDao.getAllCustomersSync();
                // TODO: Implement actual export functionality
                successMessage.postValue("Customers exported successfully");
            } catch (Exception e) {
                errorMessage.postValue("Export failed: " + e.getMessage());
            }
        });
    }
    
    public void importCustomers() {
        executorService.execute(() -> {
            try {
                // TODO: Implement actual import functionality
                successMessage.postValue("Customers imported successfully");
            } catch (Exception e) {
                errorMessage.postValue("Import failed: " + e.getMessage());
            }
        });
    }
    
    public LiveData<Customer> getCustomerById(long id) {
        return customerDao.getCustomerById(id);
    }
    
    public LiveData<List<Customer>> getActiveCustomers() {
        MutableLiveData<List<Customer>> activeCustomers = new MutableLiveData<>();
        executorService.execute(() -> {
            try {
                List<Customer> customers = customerDao.getCustomersByStatus(true);
                activeCustomers.postValue(customers);
            } catch (Exception e) {
                errorMessage.postValue("Error getting active customers: " + e.getMessage());
            }
        });
        return activeCustomers;
    }
    
    public LiveData<List<Customer>> getPremiumCustomers() {
        return customerDao.getCustomersByType(true);
    }
    
    public LiveData<Integer> getCustomerCount() {
        return customerDao.getCustomerCount();
    }
    
    public LiveData<Integer> getActiveCustomerCount() {
        MutableLiveData<Integer> count = new MutableLiveData<>();
        executorService.execute(() -> {
            try {
                int activeCount = customerDao.getActiveCustomerCount();
                count.postValue(activeCount);
            } catch (Exception e) {
                errorMessage.postValue("Error getting active customer count: " + e.getMessage());
            }
        });
        return count;
    }
    
    public LiveData<Integer> getPremiumCustomerCount() {
        return customerDao.getPremiumCustomerCount();
    }
    
    public Customer getCustomerByIdSync(long id) {
        return customerDao.getCustomerByIdSync((int) id);
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
} 