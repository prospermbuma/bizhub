package com.extrap.co.bizhub.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.adapters.DocumentAdapter;
import com.extrap.co.bizhub.data.entities.Document;
import com.extrap.co.bizhub.viewmodels.CameraViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {
    
    private static final int REQUEST_IMAGE_CAPTURE = 1001;
    private static final int REQUEST_PICK_IMAGE = 1002;
    private static final int PERMISSION_REQUEST_CODE = 1003;
    
    private CameraViewModel viewModel;
    private RecyclerView documentRecyclerView;
    private DocumentAdapter documentAdapter;
    private FloatingActionButton fabCamera;
    private FloatingActionButton fabGallery;
    
    private String currentPhotoPath;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        
        // Initialize views
        initializeViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup click listeners
        setupClickListeners();
        
        // Observe data
        observeData();
        
        // Check permissions
        checkPermissions();
    }
    
    private void initializeViews() {
        documentRecyclerView = findViewById(R.id.document_recycler_view);
        fabCamera = findViewById(R.id.fab_camera);
        fabGallery = findViewById(R.id.fab_gallery);
    }
    
    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Camera & Documents");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupRecyclerView() {
        documentAdapter = new DocumentAdapter();
        documentRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        documentRecyclerView.setAdapter(documentAdapter);
    }
    
    private void setupClickListeners() {
        fabCamera.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                dispatchTakePictureIntent();
            } else {
                requestCameraPermission();
            }
        });
        
        fabGallery.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                openGallery();
            } else {
                requestStoragePermission();
            }
        });
        
        documentAdapter.setOnItemClickListener(document -> {
            // TODO: Open document viewer
            Toast.makeText(this, "Opening: " + document.getFileName(), Toast.LENGTH_SHORT).show();
        });
        
        documentAdapter.setOnItemLongClickListener(document -> {
            showDocumentOptionsDialog(document);
            return true;
        });
    }
    
    private void observeData() {
        viewModel.getDocuments().observe(this, documents -> {
            documentAdapter.setDocuments(documents);
        });
        
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void checkPermissions() {
        if (!checkCameraPermission()) {
            requestCameraPermission();
        }
        if (!checkStoragePermission()) {
            requestStoragePermission();
        }
    }
    
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    
    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }
    
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
    
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show();
            }
            
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.extrap.co.bizhub.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }
    
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir("Images");
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle captured image
                if (currentPhotoPath != null) {
                    viewModel.saveDocument(currentPhotoPath, "Camera", "image/jpeg");
                }
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                // Handle selected image from gallery
                if (data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    viewModel.saveDocumentFromUri(selectedImageUri, "Gallery", "image/jpeg");
                }
            }
        }
    }
    
    private void showDocumentOptionsDialog(Document document) {
        String[] options = {"View", "Share", "Delete"};
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Document Options")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            viewDocument(document);
                            break;
                        case 1:
                            shareDocument(document);
                            break;
                        case 2:
                            deleteDocument(document);
                            break;
                    }
                })
                .show();
    }
    
    private void viewDocument(Document document) {
        // TODO: Implement document viewer
        Toast.makeText(this, "Viewing: " + document.getFileName(), Toast.LENGTH_SHORT).show();
    }
    
    private void shareDocument(Document document) {
        // TODO: Implement document sharing
        Toast.makeText(this, "Sharing: " + document.getFileName(), Toast.LENGTH_SHORT).show();
    }
    
    private void deleteDocument(Document document) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Document")
                .setMessage("Are you sure you want to delete this document?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteDocument(document);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_scan_document) {
            // TODO: Implement document scanning
            Toast.makeText(this, "Document scanning coming soon", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_import_pdf) {
            // TODO: Implement PDF import
            Toast.makeText(this, "PDF import coming soon", Toast.LENGTH_SHORT).show();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
} 