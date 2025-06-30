package com.extrap.co.bizhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.extrap.co.bizhub.R;
import com.google.android.material.button.MaterialButton;

public class ReceiptActivity extends AppCompatActivity {
    
    private static final String TAG = "ReceiptActivity";
    
    private Toolbar toolbar;
    private TextView receiptNumberText;
    private TextView customerNameText;
    private TextView serviceTypeText;
    private TextView amountText;
    private TextView dateText;
    private TextView technicianText;
    private MaterialButton printButton;
    private MaterialButton shareButton;
    private MaterialButton emailButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "ReceiptActivity onCreate started");
        
        try {
            setContentView(R.layout.activity_receipt);
            Log.d(TAG, "ReceiptActivity layout set successfully");
            
            // Initialize views
            initializeViews();
            
            // Setup toolbar
            setupToolbar();
            
            // Setup click listeners
            setupClickListeners();
            
            // Load receipt data
            loadReceiptData();
            
            Log.d(TAG, "ReceiptActivity onCreate completed successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in ReceiptActivity onCreate", e);
        }
    }
    
    private void initializeViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            receiptNumberText = findViewById(R.id.receipt_number_text);
            customerNameText = findViewById(R.id.customer_name_text);
            serviceTypeText = findViewById(R.id.service_type_text);
            amountText = findViewById(R.id.amount_text);
            dateText = findViewById(R.id.date_text);
            technicianText = findViewById(R.id.technician_text);
            printButton = findViewById(R.id.print_button);
            shareButton = findViewById(R.id.share_button);
            emailButton = findViewById(R.id.email_button);
            
            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
        }
    }
    
    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Receipt");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up toolbar", e);
        }
    }
    
    private void setupClickListeners() {
        try {
            // Print receipt
            if (printButton != null) {
                printButton.setOnClickListener(v -> {
                    printReceipt();
                });
            }
            
            // Share receipt
            if (shareButton != null) {
                shareButton.setOnClickListener(v -> {
                    shareReceipt();
                });
            }
            
            // Email receipt
            if (emailButton != null) {
                emailButton.setOnClickListener(v -> {
                    emailReceipt();
                });
            }
            
            Log.d(TAG, "Click listeners setup successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up click listeners", e);
        }
    }
    
    private void loadReceiptData() {
        try {
            // Get receipt data from intent
            Intent intent = getIntent();
            if (intent != null) {
                String receiptNumber = intent.getStringExtra("receipt_number");
                String customerName = intent.getStringExtra("customer_name");
                String serviceType = intent.getStringExtra("service_type");
                String amount = intent.getStringExtra("amount");
                String date = intent.getStringExtra("date");
                String technician = intent.getStringExtra("technician");
                
                // Update UI
                if (receiptNumberText != null) receiptNumberText.setText(receiptNumber);
                if (customerNameText != null) customerNameText.setText(customerName);
                if (serviceTypeText != null) serviceTypeText.setText(serviceType);
                if (amountText != null) amountText.setText(amount);
                if (dateText != null) dateText.setText(date);
                if (technicianText != null) technicianText.setText(technician);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading receipt data", e);
        }
    }
    
    private void printReceipt() {
        try {
            // TODO: Implement receipt printing functionality
            Log.d(TAG, "Printing receipt");
        } catch (Exception e) {
            Log.e(TAG, "Error printing receipt", e);
        }
    }
    
    private void shareReceipt() {
        try {
            // TODO: Implement receipt sharing functionality
            Log.d(TAG, "Sharing receipt");
        } catch (Exception e) {
            Log.e(TAG, "Error sharing receipt", e);
        }
    }
    
    private void emailReceipt() {
        try {
            // TODO: Implement receipt email functionality
            Log.d(TAG, "Emailing receipt");
        } catch (Exception e) {
            Log.e(TAG, "Error emailing receipt", e);
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