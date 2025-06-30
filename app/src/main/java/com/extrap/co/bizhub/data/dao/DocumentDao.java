package com.extrap.co.bizhub.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.extrap.co.bizhub.data.entities.Document;

import java.util.List;

@Dao
public interface DocumentDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDocument(Document document);
    
    @Update
    void updateDocument(Document document);
    
    @Delete
    void deleteDocument(Document document);
    
    @Query("SELECT * FROM documents WHERE id = :documentId")
    LiveData<Document> getDocumentById(int documentId);
    
    @Query("SELECT * FROM documents WHERE id = :documentId")
    Document getDocumentByIdSync(int documentId);
    
    @Query("SELECT * FROM documents ORDER BY createdAt DESC")
    LiveData<List<Document>> getAllDocuments();
    
    @Query("SELECT * FROM documents ORDER BY createdAt DESC")
    List<Document> getAllDocumentsSync();
    
    @Query("SELECT * FROM documents WHERE workOrderId = :workOrderId ORDER BY createdAt DESC")
    LiveData<List<Document>> getDocumentsByWorkOrder(int workOrderId);
    
    @Query("SELECT * FROM documents WHERE customerId = :customerId ORDER BY createdAt DESC")
    LiveData<List<Document>> getDocumentsByCustomer(int customerId);
    
    @Query("SELECT * FROM documents WHERE source = :source ORDER BY createdAt DESC")
    LiveData<List<Document>> getDocumentsBySource(String source);
    
    @Query("SELECT * FROM documents WHERE mimeType LIKE '%image%' ORDER BY createdAt DESC")
    LiveData<List<Document>> getImageDocuments();
    
    @Query("SELECT * FROM documents WHERE mimeType LIKE '%pdf%' ORDER BY createdAt DESC")
    LiveData<List<Document>> getPdfDocuments();
    
    @Query("SELECT * FROM documents WHERE mimeType LIKE '%video%' ORDER BY createdAt DESC")
    LiveData<List<Document>> getVideoDocuments();
    
    @Query("SELECT COUNT(*) FROM documents")
    int getDocumentCount();
    
    @Query("SELECT COUNT(*) FROM documents WHERE workOrderId = :workOrderId")
    int getDocumentCountByWorkOrder(int workOrderId);
    
    @Query("SELECT COUNT(*) FROM documents WHERE customerId = :customerId")
    int getDocumentCountByCustomer(int customerId);
    
    @Query("SELECT * FROM documents WHERE syncStatus = 'pending'")
    List<Document> getPendingSyncDocuments();
    
    @Query("SELECT COUNT(*) FROM documents WHERE syncStatus = 'pending'")
    int getPendingSyncDocumentCount();
    
    @Query("DELETE FROM documents WHERE id = :documentId")
    void deleteDocumentById(int documentId);
    
    @Query("DELETE FROM documents WHERE workOrderId = :workOrderId")
    void deleteDocumentsByWorkOrder(int workOrderId);
    
    @Query("DELETE FROM documents WHERE customerId = :customerId")
    void deleteDocumentsByCustomer(int customerId);
    
    @Query("DELETE FROM documents WHERE createdAt < :timestamp")
    void deleteOldDocuments(long timestamp);
} 