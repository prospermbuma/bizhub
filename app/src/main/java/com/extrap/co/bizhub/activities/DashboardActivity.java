package com.extrap.co.bizhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.fragments.ComplaintsFragment;
import com.extrap.co.bizhub.fragments.CustomersFragment;
import com.extrap.co.bizhub.fragments.DashboardFragment;
import com.extrap.co.bizhub.fragments.FeedbackFragment;
import com.extrap.co.bizhub.fragments.HelpFragment;
import com.extrap.co.bizhub.fragments.OfflineFragment;
import com.extrap.co.bizhub.fragments.PrivacyPolicyFragment;
import com.extrap.co.bizhub.fragments.ProfileFragment;
import com.extrap.co.bizhub.fragments.ReportsFragment;
import com.extrap.co.bizhub.fragments.SettingsFragment;
import com.extrap.co.bizhub.fragments.WorkOrdersFragment;
import com.extrap.co.bizhub.utils.PreferenceManager;
import com.extrap.co.bizhub.viewmodels.DashboardViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    
    private static final String TAG = "DashboardActivity";
    
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private DashboardViewModel viewModel;
    private PreferenceManager preferenceManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "DashboardActivity onCreate started");
        
        try {
            setContentView(R.layout.activity_dashboard);
            Log.d(TAG, "DashboardActivity layout set successfully");
            
            // Initialize managers
            preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
            
            // Initialize views
            initializeViews();
            
            // Setup toolbar and navigation
            setupToolbar();
            setupNavigationDrawer();
            setupBottomNavigation();
            
            // Initialize ViewModel
            viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
            
            // Load default fragment
            loadFragment(new DashboardFragment());
            
            Log.d(TAG, "DashboardActivity onCreate completed successfully");
            
        } catch (Exception e) {
            Log.e(TAG, "Error in DashboardActivity onCreate", e);
            // Emergency fallback - show error message
            Snackbar.make(findViewById(android.R.id.content), "Error loading dashboard", Snackbar.LENGTH_LONG).show();
        }
    }
    
    private void initializeViews() {
        try {
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.navigation_view);
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            toolbar = findViewById(R.id.toolbar);
            
            Log.d(TAG, "Views initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
        }
    }
    
    private void setupToolbar() {
        try {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.dashboard_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up toolbar", e);
        }
    }
    
    private void setupNavigationDrawer() {
        try {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_dashboard, R.string.nav_dashboard);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            
            navigationView.setNavigationItemSelectedListener(this);
            
            // Set user info in navigation header
            setupNavigationHeader();
        } catch (Exception e) {
            Log.e(TAG, "Error setting up navigation drawer", e);
        }
    }
    
    private void setupBottomNavigation() {
        try {
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                
                if (id == R.id.navigation_dashboard) {
                    loadFragment(new DashboardFragment());
                    getSupportActionBar().setTitle(R.string.dashboard_title);
                    return true;
                } else if (id == R.id.navigation_work_orders) {
                    loadFragment(new WorkOrdersFragment());
                    getSupportActionBar().setTitle(R.string.work_orders);
                    return true;
                } else if (id == R.id.navigation_add) {
                    // TODO: Show add work order dialog or navigate to add screen
                    Snackbar.make(drawerLayout, "Add new work order", Snackbar.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.navigation_map) {
                    // TODO: Show map view
                    Snackbar.make(drawerLayout, "Map view", Snackbar.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.navigation_profile) {
                    loadFragment(new ProfileFragment());
                    getSupportActionBar().setTitle(R.string.account);
                    return true;
                }
                
                return false;
            });
        } catch (Exception e) {
            Log.e(TAG, "Error setting up bottom navigation", e);
        }
    }
    
    private void setupNavigationHeader() {
        try {
            View headerView = navigationView.getHeaderView(0);
            TextView userNameText = headerView.findViewById(R.id.nav_user_name);
            TextView userEmailText = headerView.findViewById(R.id.nav_user_email);
            
            userNameText.setText(preferenceManager.getUserName());
            userEmailText.setText(preferenceManager.getUserEmail());
        } catch (Exception e) {
            Log.e(TAG, "Error setting up navigation header", e);
        }
    }
    
    public void loadFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "Error loading fragment", e);
        }
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {
            int id = item.getItemId();
            
            if (id == R.id.nav_dashboard) {
                loadFragment(new DashboardFragment());
                getSupportActionBar().setTitle(R.string.dashboard_title);
            } else if (id == R.id.nav_work_orders) {
                loadFragment(new WorkOrdersFragment());
                getSupportActionBar().setTitle(R.string.work_orders);
            } else if (id == R.id.nav_customers) {
                loadFragment(new CustomersFragment());
                getSupportActionBar().setTitle(R.string.customers);
            } else if (id == R.id.nav_reports) {
                loadFragment(new ReportsFragment());
                getSupportActionBar().setTitle(R.string.reports);
            } else if (id == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                getSupportActionBar().setTitle(R.string.account);
            } else if (id == R.id.nav_settings) {
                loadFragment(new SettingsFragment());
                getSupportActionBar().setTitle(R.string.settings);
            } else if (id == R.id.nav_offline) {
                loadFragment(new OfflineFragment());
                getSupportActionBar().setTitle("Offline Mode");
            } else if (id == R.id.nav_feedback) {
                loadFragment(new FeedbackFragment());
                getSupportActionBar().setTitle(R.string.feedback);
            } else if (id == R.id.nav_complaints) {
                loadFragment(new ComplaintsFragment());
                getSupportActionBar().setTitle(R.string.complaints);
            } else if (id == R.id.nav_help) {
                loadFragment(new HelpFragment());
                getSupportActionBar().setTitle(R.string.help);
            } else if (id == R.id.nav_privacy) {
                loadFragment(new PrivacyPolicyFragment());
                getSupportActionBar().setTitle(R.string.privacy_policy);
            } else if (id == R.id.nav_logout) {
                logout();
            }
            
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error in navigation item selected", e);
            return false;
        }
    }
    
    private void logout() {
        try {
            preferenceManager.clearUserSession();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error during logout", e);
        }
    }
    
    @Override
    public void onBackPressed() {
        try {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onBackPressed", e);
            super.onBackPressed();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            Log.e(TAG, "Error in onOptionsItemSelected", e);
            return super.onOptionsItemSelected(item);
        }
    }
} 