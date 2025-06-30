package com.extrap.co.bizhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.data.entities.User;
import com.extrap.co.bizhub.utils.PasswordUtils;
import com.extrap.co.bizhub.utils.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    
    private static final String TAG = "LoginActivity";
    
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView forgotPasswordText;
    private Button signUpButton;
    
    private PreferenceManager preferenceManager;
    private ExecutorService executorService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "LoginActivity onCreate started");
        
        try {
            setContentView(R.layout.activity_login);
            Log.d(TAG, "LoginActivity layout set successfully");
            
            // Initialize managers
            preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
            executorService = Executors.newSingleThreadExecutor();
            
            // Initialize views
            initializeViews();
            
            // Setup click listeners
            setupClickListeners();
            
            // Check if email was passed from sign up
            String email = getIntent().getStringExtra("email");
            if (email != null) {
                emailEditText.setText(email);
            }
            
            Log.d(TAG, "LoginActivity onCreate completed successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in LoginActivity onCreate", e);
            // Emergency fallback - go directly to MainActivity
            try {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e2) {
                Log.e(TAG, "Emergency fallback failed", e2);
            }
        }
    }
    
    private void initializeViews() {
        try {
            emailLayout = findViewById(R.id.email_layout);
            passwordLayout = findViewById(R.id.password_layout);
            emailEditText = findViewById(R.id.email_edit_text);
            passwordEditText = findViewById(R.id.password_edit_text);
            loginButton = findViewById(R.id.login_button);
            progressBar = findViewById(R.id.progress_bar);
            forgotPasswordText = findViewById(R.id.forgot_password_text);
            signUpButton = findViewById(R.id.sign_up_button);
            
            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
        }
    }
    
    private void setupClickListeners() {
        try {
            if (loginButton != null) {
                loginButton.setOnClickListener(v -> attemptLogin());
            }
            
            if (forgotPasswordText != null) {
                forgotPasswordText.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error navigating to ResetPasswordActivity", e);
                    }
                });
            }
            
            if (signUpButton != null) {
                signUpButton.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error navigating to SignUpActivity", e);
                    }
                });
            }
            
            Log.d(TAG, "Click listeners setup successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners", e);
        }
    }
    
    private void attemptLogin() {
        try {
            // Reset errors
            if (emailLayout != null) emailLayout.setError(null);
            if (passwordLayout != null) passwordLayout.setError(null);
            
            // Get values
            String email = emailEditText != null ? emailEditText.getText().toString().trim() : "";
            String password = passwordEditText != null ? passwordEditText.getText().toString().trim() : "";
            
            // Validate input
            if (TextUtils.isEmpty(email)) {
                if (emailLayout != null) emailLayout.setError(getString(R.string.error_field_required));
                if (emailEditText != null) emailEditText.requestFocus();
                return;
            }
            
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (emailLayout != null) emailLayout.setError(getString(R.string.error_invalid_email));
                if (emailEditText != null) emailEditText.requestFocus();
                return;
            }
            
            if (TextUtils.isEmpty(password)) {
                if (passwordLayout != null) passwordLayout.setError(getString(R.string.error_field_required));
                if (passwordEditText != null) passwordEditText.requestFocus();
                return;
            }
            
            // Show progress
            showProgress(true);
            
            // Authenticate user
            authenticateUser(email, password);
            
        } catch (Exception e) {
            Log.e(TAG, "Error in attemptLogin", e);
            showProgress(false);
        }
    }
    
    private void authenticateUser(String email, String password) {
        executorService.execute(() -> {
            try {
                User user = FieldServiceApp.getInstance().getDatabase().userDao().getUserByEmail(email);
                if (user != null) {
                    // If the stored password is not hashed (legacy), hash and update it
                    if (user.getPassword().length() < 64) { // SHA-256 hash is 64 hex chars
                        String hashed = PasswordUtils.hashPassword(user.getPassword());
                        user.setPassword(hashed);
                        FieldServiceApp.getInstance().getDatabase().userDao().updateUser(user);
                    }
                }
                
                runOnUiThread(() -> {
                    try {
                        if (user != null && PasswordUtils.verifyPassword(password, user.getPassword())) {
                            // Login successful
                            saveUserSession(user);
                            navigateToDashboard();
                        } else {
                            // Login failed
                            showProgress(false);
                            if (passwordLayout != null) passwordLayout.setError("Invalid email or password");
                            if (passwordEditText != null) passwordEditText.requestFocus();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error in UI thread after authentication", e);
                        showProgress(false);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error in authenticateUser", e);
                runOnUiThread(() -> {
                    showProgress(false);
                    if (passwordLayout != null) passwordLayout.setError("Login failed. Please try again.");
                });
            }
        });
    }
    
    private void saveUserSession(User user) {
        try {
            preferenceManager.setLoggedIn(true);
            preferenceManager.setUserId(user.getId());
            preferenceManager.setUserEmail(user.getEmail());
            preferenceManager.setUserName(user.getFirstName() + " " + user.getLastName());
            preferenceManager.setUserRole(user.getRole());
        } catch (Exception e) {
            Log.e(TAG, "Error saving user session", e);
        }
    }
    
    private void navigateToDashboard() {
        try {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to DashboardActivity", e);
            // Fallback to MainActivity
            try {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e2) {
                Log.e(TAG, "Fallback to MainActivity also failed", e2);
            }
        }
    }
    
    private void showProgress(boolean show) {
        try {
            if (progressBar != null) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
            if (loginButton != null) {
                loginButton.setEnabled(!show);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error showing/hiding progress", e);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onDestroy", e);
        }
    }
} 