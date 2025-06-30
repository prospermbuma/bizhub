package com.extrap.co.bizhub.viewmodels;

import android.app.Application;
import android.net.Uri;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.data.entities.Document;
import java.util.List;

public class CameraViewModel extends AndroidViewModel {
    private final FieldServiceApp app;
    private final MutableLiveData<List<Document>> documents;
    private final MutableLiveData<String> errorMessage;

    public CameraViewModel(Application application) {
        super(application);
        app = (FieldServiceApp) application;
        documents = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        loadDocuments();
    }

    public LiveData<List<Document>> getDocuments() {
        return documents;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadDocuments() {
        // Load documents from database
        // This is a placeholder implementation
        // In a real app, you would load from DocumentDao
    }

    public void saveDocument(String filePath, String source, String mimeType) {
        // Save document to database
        // This is a placeholder implementation
        // In a real app, you would save to DocumentDao
        loadDocuments(); // Reload documents after saving
    }

    public void saveDocumentFromUri(Uri uri, String source, String mimeType) {
        // Save document from URI to database
        // This is a placeholder implementation
        // In a real app, you would save to DocumentDao
        loadDocuments(); // Reload documents after saving
    }

    public void deleteDocument(Document document) {
        // Delete document from database
        // This is a placeholder implementation
        // In a real app, you would delete from DocumentDao
        loadDocuments(); // Reload documents after deleting
    }
} 