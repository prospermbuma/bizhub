package com.extrap.co.bizhub.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.extrap.co.bizhub.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ResetPasswordActivity extends AppCompatActivity {
    
    private TextInputLayout emailLayout;
    private TextInputEditText emailEditText;
    private Button resetButton;
    private ProgressBar progressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        
        initializeViews();
        setupToolbar();
        setupClickListeners();
    }
    
    private void initializeViews() {
        emailLayout = findViewById(R.id.email_layout);
        emailEditText = findViewById(R.id.email_edit_text);
        resetButton = findViewById(R.id.reset_button);
        progressBar = findViewById(R.id.progress_bar);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void setupClickListeners() {
        resetButton.setOnClickListener(v -> attemptReset());
    }
    
    private void attemptReset() {
        emailLayout.setError(null);
        
        String email = emailEditText.getText().toString().trim();
        
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError(getString(R.string.error_field_required));
            emailEditText.requestFocus();
            return;
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(getString(R.string.error_invalid_email));
            emailEditText.requestFocus();
            return;
        }
        
        showProgress(true);
        
        // Simulate password reset
        resetButton.postDelayed(() -> {
            showProgress(false);
            Toast.makeText(this, "Password reset email sent", Toast.LENGTH_LONG).show();
            finish();
        }, 2000);
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        resetButton.setEnabled(!show);
    }
} 