package com.extrap.co.bizhub.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestingViewModel extends AndroidViewModel {
    
    private MutableLiveData<String> testResults;
    private MutableLiveData<String> errorMessage;
    private StringBuilder resultsBuilder;
    
    public TestingViewModel(Application application) {
        super(application);
        testResults = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        resultsBuilder = new StringBuilder();
    }
    
    public LiveData<String> getTestResults() {
        return testResults;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public void addTestResult(String result) {
        String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        resultsBuilder.append("[").append(timestamp).append("] ").append(result).append("\n");
        testResults.setValue(resultsBuilder.toString());
    }
    
    public void addErrorResult(String error) {
        String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        resultsBuilder.append("[").append(timestamp).append("] ERROR: ").append(error).append("\n");
        testResults.setValue(resultsBuilder.toString());
        errorMessage.setValue(error);
    }
    
    public void clearTestResults() {
        resultsBuilder = new StringBuilder();
        testResults.setValue("");
    }
    
    public String getTestResultsText() {
        return resultsBuilder.toString();
    }
} 