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
import androidx.appcompat.widget.Toolbar;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.data.entities.User;
import com.extrap.co.bizhub.utils.NetworkUtils;
import com.extrap.co.bizhub.utils.PasswordUtils;
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
    private ProgressBar progressBar;
    private TextView forgotPasswordText;
    private TextView signUpText;
    private TextView connectionStatusText;
    
    private PreferenceManager preferenceManager;
    private ExecutorService executorService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize managers
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        executorService = Executors.newSingleThreadExecutor();
        
        // Initialize views
        initializeViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup click listeners
        setupClickListeners();
        
        // Check if email was passed from sign up
        String email = getIntent().getStringExtra("email");
        if (email != null) {
            emailEditText.setText(email);
        }
        
        // Check network connection
        checkNetworkConnection();
    }
    
    private void initializeViews() {
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        forgotPasswordText = findViewById(R.id.forgot_password_text);
        signUpText = findViewById(R.id.sign_up_text);
        connectionStatusText = findViewById(R.id.connection_status_text);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        
        forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });
        
        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
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
        
        // Validate input
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
        
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.error_field_required));
            passwordEditText.requestFocus();
            return;
        }
        
        // Show progress
        showProgress(true);
        
        // Authenticate user
        authenticateUser(email, password);
    }
    
    private void authenticateUser(String email, String password) {
        executorService.execute(() -> {
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
                if (user != null && PasswordUtils.verifyPassword(password, user.getPassword())) {
                    // Login successful
                    saveUserSession(user);
                    navigateToDashboard();
                } else {
                    // Login failed
                    showProgress(false);
                    passwordLayout.setError("Invalid email or password");
                    passwordEditText.requestFocus();
                }
            });
        });
    }
    
    private void saveUserSession(User user) {
        preferenceManager.setLoggedIn(true);
        preferenceManager.setUserId(user.getId());
        preferenceManager.setUserEmail(user.getEmail());
        preferenceManager.setUserName(user.getFirstName() + " " + user.getLastName());
        preferenceManager.setUserRole(user.getRole());
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
    }
    
    private void checkNetworkConnection() {
        if (NetworkUtils.getInstance().isNetworkAvailable()) {
            connectionStatusText.setVisibility(View.GONE);
        } else {
            connectionStatusText.setVisibility(View.VISIBLE);
            connectionStatusText.setText(R.string.no_internet);
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
} 