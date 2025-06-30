package com.extrap.co.bizhub.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.extrap.co.bizhub.R;
import com.google.android.material.card.MaterialCardView;

public class ContactActivity extends AppCompatActivity {
    
    private static final String TAG = "ContactActivity";
    
    private Toolbar toolbar;
    private MaterialCardView callSupportCard;
    private MaterialCardView emailSupportCard;
    private MaterialCardView visitOfficeCard;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ContactActivity onCreate started");
        
        try {
            setContentView(R.layout.activity_contact);
            Log.d(TAG, "ContactActivity layout set successfully");
            
            // Initialize views
            initializeViews();
            
            // Setup toolbar
            setupToolbar();
            
            // Setup click listeners
            setupClickListeners();
            
            Log.d(TAG, "ContactActivity onCreate completed successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in ContactActivity onCreate", e);
        }
    }
    
    private void initializeViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            callSupportCard = findViewById(R.id.call_support_card);
            emailSupportCard = findViewById(R.id.email_support_card);
            visitOfficeCard = findViewById(R.id.visit_office_card);
            
            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
        }
    }
    
    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.contact);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up toolbar", e);
        }
    }
    
    private void setupClickListeners() {
        try {
            // Call support
            if (callSupportCard != null) {
                callSupportCard.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + getString(R.string.support_phone)));
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error making phone call", e);
                    }
                });
            }
            
            // Email support
            if (emailSupportCard != null) {
                emailSupportCard.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + getString(R.string.support_email)));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "FSP Support Request");
                        startActivity(Intent.createChooser(intent, "Send Email"));
                    } catch (Exception e) {
                        Log.e(TAG, "Error sending email", e);
                    }
                });
            }
            
            // Visit office
            if (visitOfficeCard != null) {
                visitOfficeCard.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("geo:0,0?q=" + getString(R.string.office_address)));
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error opening map", e);
                    }
                });
            }
            
            Log.d(TAG, "Click listeners setup successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners", e);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
                return true;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            Log.e(TAG, "Error in onOptionsItemSelected", e);
            return super.onOptionsItemSelected(item);
        }
    }
} 