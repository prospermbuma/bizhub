package com.extrap.co.bizhub.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.utils.PreferenceManager;
import com.google.android.material.textfield.TextInputEditText;

public class AccountActivity extends AppCompatActivity {
    
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText phoneEditText;
    private Button saveButton;
    private PreferenceManager preferenceManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        
        initializeViews();
        setupToolbar();
        loadUserData();
        setupClickListeners();
    }
    
    private void initializeViews() {
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        saveButton = findViewById(R.id.save_button);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void loadUserData() {
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        
        // Load user data from preferences
        String[] nameParts = preferenceManager.getUserName().split(" ", 2);
        firstNameEditText.setText(nameParts.length > 0 ? nameParts[0] : "");
        lastNameEditText.setText(nameParts.length > 1 ? nameParts[1] : "");
        emailEditText.setText(preferenceManager.getUserEmail());
        phoneEditText.setText(""); // Phone not stored in preferences yet
    }
    
    private void setupClickListeners() {
        saveButton.setOnClickListener(v -> saveUserData());
    }
    
    private void saveUserData() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Save to preferences
        preferenceManager.setUserName(firstName + " " + lastName);
        preferenceManager.setUserEmail(email);
        
        // TODO: Update user in database
        
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
} 