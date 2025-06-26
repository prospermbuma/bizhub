package com.extrap.co.bizhub.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.extrap.co.bizhub.R;
import com.google.android.material.textfield.TextInputEditText;

public class ComplaintsActivity extends AppCompatActivity {
    
    private TextInputEditText subjectEditText;
    private TextInputEditText descriptionEditText;
    private Button submitButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        
        initializeViews();
        setupToolbar();
        setupClickListeners();
    }
    
    private void initializeViews() {
        subjectEditText = findViewById(R.id.subject_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        submitButton = findViewById(R.id.submit_button);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.complaints);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void setupClickListeners() {
        submitButton.setOnClickListener(v -> submitComplaint());
    }
    
    private void submitComplaint() {
        String subject = subjectEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        
        if (TextUtils.isEmpty(subject)) {
            Toast.makeText(this, "Please enter a subject", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // TODO: Save complaint to database/API
        Toast.makeText(this, "Complaint submitted successfully", Toast.LENGTH_LONG).show();
        finish();
    }
} 