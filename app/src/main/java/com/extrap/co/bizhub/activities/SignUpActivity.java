package com.extrap.co.bizhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.data.entities.User;
import com.extrap.co.bizhub.utils.PasswordUtils;
import com.extrap.co.bizhub.utils.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {
    
    private TextInputLayout firstNameLayout;
    private TextInputLayout lastNameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout phoneLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;
    private TextInputLayout securityQuestionLayout;
    private TextInputLayout securityAnswerLayout;
    
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText phoneEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private TextInputEditText securityQuestionEditText;
    private TextInputEditText securityAnswerEditText;
    
    private Button signUpButton;
    private ProgressBar progressBar;
    
    private PreferenceManager preferenceManager;
    private ExecutorService executorService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        // Initialize views
        initializeViews();
        
        // Initialize managers
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        executorService = Executors.newSingleThreadExecutor();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup click listeners
        setupClickListeners();
    }
    
    private void initializeViews() {
        firstNameLayout = findViewById(R.id.first_name_layout);
        lastNameLayout = findViewById(R.id.last_name_layout);
        emailLayout = findViewById(R.id.email_layout);
        phoneLayout = findViewById(R.id.phone_layout);
        passwordLayout = findViewById(R.id.password_layout);
        confirmPasswordLayout = findViewById(R.id.confirm_password_layout);
        securityQuestionLayout = findViewById(R.id.security_question_layout);
        securityAnswerLayout = findViewById(R.id.security_answer_layout);
        
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        securityQuestionEditText = findViewById(R.id.security_question_edit_text);
        securityAnswerEditText = findViewById(R.id.security_answer_edit_text);
        
        signUpButton = findViewById(R.id.sign_up_button);
        progressBar = findViewById(R.id.progress_bar);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void setupClickListeners() {
        signUpButton.setOnClickListener(v -> attemptSignUp());
    }
    
    private void attemptSignUp() {
        // Reset errors
        firstNameLayout.setError(null);
        lastNameLayout.setError(null);
        emailLayout.setError(null);
        phoneLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);
        securityQuestionLayout.setError(null);
        securityAnswerLayout.setError(null);
        
        // Get values
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String securityQuestion = securityQuestionEditText.getText().toString().trim();
        String securityAnswer = securityAnswerEditText.getText().toString().trim();
        
        // Validate input
        if (TextUtils.isEmpty(firstName)) {
            firstNameLayout.setError(getString(R.string.error_field_required));
            firstNameEditText.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(lastName)) {
            lastNameLayout.setError(getString(R.string.error_field_required));
            lastNameEditText.requestFocus();
            return;
        }
        
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
        
        if (TextUtils.isEmpty(phone)) {
            phoneLayout.setError(getString(R.string.error_field_required));
            phoneEditText.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.error_field_required));
            passwordEditText.requestFocus();
            return;
        }
        
        if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(securityQuestion)) {
            securityQuestionLayout.setError("Security question is required");
            securityQuestionEditText.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(securityAnswer)) {
            securityAnswerLayout.setError("Security answer is required");
            securityAnswerEditText.requestFocus();
            return;
        }
        
        // Show progress
        showProgress(true);
        
        // Check if email already exists
        executorService.execute(() -> {
            User existingUser = FieldServiceApp.getInstance().getDatabase().userDao().getUserByEmail(email);
            
            runOnUiThread(() -> {
                if (existingUser != null) {
                    showProgress(false);
                    emailLayout.setError("Email already registered");
                    emailEditText.requestFocus();
                } else {
                    // Create new user
                    createUser(firstName, lastName, email, phone, password, securityQuestion, securityAnswer);
                }
            });
        });
    }
    
    private void createUser(String firstName, String lastName, String email, String phone, String password, String securityQuestion, String securityAnswer) {
        executorService.execute(() -> {
            String hashedPassword = PasswordUtils.hashPassword(password);
            User newUser = new User(email, hashedPassword, firstName, lastName, phone, "technician");
            newUser.setSecurityQuestion(securityQuestion);
            newUser.setSecurityAnswer(securityAnswer);
            long userId = FieldServiceApp.getInstance().getDatabase().userDao().insertUser(newUser);
            
            runOnUiThread(() -> {
                showProgress(false);
                
                if (userId > 0) {
                    // Sign up successful
                    Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_LONG).show();
                    
                    // Navigate to login
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    // Sign up failed
                    Toast.makeText(SignUpActivity.this, "Failed to create account", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
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