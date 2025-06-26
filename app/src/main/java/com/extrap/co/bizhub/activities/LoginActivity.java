package com.extrap.co.bizhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.data.entities.User;
import com.extrap.co.bizhub.utils.NetworkUtils;
import com.extrap.co.bizhub.utils.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private Button signUpButton;
    private TextView forgotPasswordText;
    private ProgressBar progressBar;
    private TextView connectionStatusText;
    
    private PreferenceManager preferenceManager;
    private ExecutorService executorService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize views
        initializeViews();
        
        // Initialize managers
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        executorService = Executors.newSingleThreadExecutor();
        
        // Check network connection
        checkNetworkConnection();
        
        // Set up click listeners
        setupClickListeners();
    }
    
    private void initializeViews() {
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        signUpButton = findViewById(R.id.sign_up_button);
        forgotPasswordText = findViewById(R.id.forgot_password_text);
        progressBar = findViewById(R.id.progress_bar);
        connectionStatusText = findViewById(R.id.connection_status_text);
    }
    
    private void checkNetworkConnection() {
        if (NetworkUtils.getInstance().isNetworkAvailable()) {
            connectionStatusText.setVisibility(View.GONE);
        } else {
            connectionStatusText.setVisibility(View.VISIBLE);
            connectionStatusText.setText(R.string.no_internet);
        }
    }
    
    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        
        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        
        forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });
    }
    
    private void attemptLogin() {
        // Reset errors
        emailLayout.setError(null);
        passwordLayout.setError(null);
        
        // Get values
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // Check for valid input
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError(getString(R.string.error_field_required));
            emailEditText.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.error_field_required));
            passwordEditText.requestFocus();
            return;
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(getString(R.string.error_invalid_email));
            emailEditText.requestFocus();
            return;
        }
        
        // Show progress
        showProgress(true);
        
        // Perform authentication
        executorService.execute(() -> {
            User user = FieldServiceApp.getInstance().getDatabase().userDao().authenticateUser(email, password);
            
            runOnUiThread(() -> {
                showProgress(false);
                
                if (user != null) {
                    // Login successful
                    saveUserData(user);
                    navigateToDashboard();
                } else {
                    // Login failed
                    Toast.makeText(LoginActivity.this, R.string.error_invalid_credentials, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
    
    private void saveUserData(User user) {
        preferenceManager.setUserId(user.getId());
        preferenceManager.setUserEmail(user.getEmail());
        preferenceManager.setUserName(user.getFullName());
        preferenceManager.setUserRole(user.getRole());
        preferenceManager.setLoggedIn(true);
        
        // Update last login time
        executorService.execute(() -> {
            user.setLastLoginAt(System.currentTimeMillis());
            FieldServiceApp.getInstance().getDatabase().userDao().updateUser(user);
        });
    }
    
    private void navigateToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!show);
        signUpButton.setEnabled(!show);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
} 