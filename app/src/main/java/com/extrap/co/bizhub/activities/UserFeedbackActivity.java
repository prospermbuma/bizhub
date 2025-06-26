package com.extrap.co.bizhub.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.extrap.co.bizhub.R;
import com.google.android.material.textfield.TextInputEditText;

public class UserFeedbackActivity extends AppCompatActivity {
    
    private RatingBar ratingBar;
    private TextInputEditText feedbackEditText;
    private Button submitButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        
        initializeViews();
        setupToolbar();
        setupClickListeners();
    }
    
    private void initializeViews() {
        ratingBar = findViewById(R.id.rating_bar);
        feedbackEditText = findViewById(R.id.feedback_edit_text);
        submitButton = findViewById(R.id.submit_button);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void setupClickListeners() {
        submitButton.setOnClickListener(v -> submitFeedback());
    }
    
    private void submitFeedback() {
        float rating = ratingBar.getRating();
        String feedback = feedbackEditText.getText().toString().trim();
        
        if (rating == 0) {
            Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (TextUtils.isEmpty(feedback)) {
            Toast.makeText(this, "Please provide feedback", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // TODO: Save feedback to database/API
        Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_LONG).show();
        finish();
    }
} 