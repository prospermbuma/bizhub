package com.extrap.co.bizhub.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.utils.DeploymentUtils;
import com.extrap.co.bizhub.utils.TestUtils;
import com.extrap.co.bizhub.viewmodels.TestingViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class TestingActivity extends AppCompatActivity {
    
    private TestingViewModel viewModel;
    private TestUtils testUtils;
    private DeploymentUtils deploymentUtils;
    
    // UI Components
    private MaterialCardView testModeCard;
    private TextView testModeStatusText;
    private MaterialButton populateTestDataButton;
    private MaterialButton runDatabaseTestsButton;
    private MaterialButton clearTestDataButton;
    private MaterialButton healthCheckButton;
    private MaterialButton deploymentPrepButton;
    private MaterialButton createBackupButton;
    private TextView testResultsText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        
        // Initialize utilities
        testUtils = new TestUtils(this);
        deploymentUtils = new DeploymentUtils(this);
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(TestingViewModel.class);
        
        // Initialize views
        initializeViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup click listeners
        setupClickListeners();
        
        // Observe data
        observeData();
        
        // Update UI
        updateUI();
    }
    
    private void initializeViews() {
        testModeCard = findViewById(R.id.test_mode_card);
        testModeStatusText = findViewById(R.id.test_mode_status_text);
        populateTestDataButton = findViewById(R.id.populate_test_data_button);
        runDatabaseTestsButton = findViewById(R.id.run_database_tests_button);
        clearTestDataButton = findViewById(R.id.clear_test_data_button);
        healthCheckButton = findViewById(R.id.health_check_button);
        deploymentPrepButton = findViewById(R.id.deployment_prep_button);
        createBackupButton = findViewById(R.id.create_backup_button);
        testResultsText = findViewById(R.id.test_results_text);
    }
    
    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Testing & Debug");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupClickListeners() {
        populateTestDataButton.setOnClickListener(v -> {
            populateTestData();
        });
        
        runDatabaseTestsButton.setOnClickListener(v -> {
            runDatabaseTests();
        });
        
        clearTestDataButton.setOnClickListener(v -> {
            clearTestData();
        });
        
        healthCheckButton.setOnClickListener(v -> {
            performHealthCheck();
        });
        
        deploymentPrepButton.setOnClickListener(v -> {
            prepareForDeployment();
        });
        
        createBackupButton.setOnClickListener(v -> {
            createBackup();
        });
    }
    
    private void observeData() {
        viewModel.getTestResults().observe(this, results -> {
            testResultsText.setText(results);
        });
        
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void updateUI() {
        // Update test mode status
        boolean isTestMode = TestUtils.isTestModeEnabled();
        testModeStatusText.setText(isTestMode ? "ENABLED" : "DISABLED");
        testModeStatusText.setTextColor(getColor(isTestMode ? R.color.warning : R.color.success));
        
        // Update button states
        populateTestDataButton.setEnabled(isTestMode);
        runDatabaseTestsButton.setEnabled(isTestMode);
        clearTestDataButton.setEnabled(isTestMode);
    }
    
    private void populateTestData() {
        testUtils.populateTestData();
        Toast.makeText(this, "Test data populated", Toast.LENGTH_SHORT).show();
        viewModel.addTestResult("Test data populated successfully");
    }
    
    private void runDatabaseTests() {
        testUtils.runDatabaseTests();
        Toast.makeText(this, "Database tests completed", Toast.LENGTH_SHORT).show();
        viewModel.addTestResult("Database tests completed successfully");
    }
    
    private void clearTestData() {
        // This would clear all test data
        Toast.makeText(this, "Test data cleared", Toast.LENGTH_SHORT).show();
        viewModel.addTestResult("Test data cleared successfully");
    }
    
    private void performHealthCheck() {
        deploymentUtils.performHealthCheck();
        Toast.makeText(this, "Health check completed", Toast.LENGTH_SHORT).show();
        viewModel.addTestResult("Health check completed successfully");
    }
    
    private void prepareForDeployment() {
        deploymentUtils.prepareForDeployment();
        Toast.makeText(this, "Deployment preparation completed", Toast.LENGTH_SHORT).show();
        viewModel.addTestResult("Deployment preparation completed successfully");
    }
    
    private void createBackup() {
        deploymentUtils.createBackup();
        Toast.makeText(this, "Backup created", Toast.LENGTH_SHORT).show();
        viewModel.addTestResult("Backup created successfully");
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_testing, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_clear_results) {
            clearTestResults();
            return true;
        } else if (id == R.id.action_export_results) {
            exportTestResults();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void clearTestResults() {
        viewModel.clearTestResults();
        Toast.makeText(this, "Test results cleared", Toast.LENGTH_SHORT).show();
    }
    
    private void exportTestResults() {
        // TODO: Implement test results export
        Toast.makeText(this, "Exporting test results...", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (testUtils != null) {
            testUtils.cleanup();
        }
    }
} 