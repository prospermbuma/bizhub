package com.extrap.co.bizhub.activities;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.adapters.RecentActivityAdapter;
import com.extrap.co.bizhub.data.entities.WorkOrder;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private DashboardViewModel viewModel;
    private PreferenceManager preferenceManager;
    
    // Dashboard cards
    private MaterialCardView todayTasksCard;
    private MaterialCardView pendingOrdersCard;
    private MaterialCardView completedTodayCard;
    private MaterialCardView totalCustomersCard;
    
    // Dashboard stats
    private TextView todayTasksCount;
    private TextView pendingOrdersCount;
    private TextView completedTodayCount;
    private TextView totalCustomersCount;
    
    // Recent activities
    private RecyclerView recentActivitiesRecyclerView;
    private RecentActivityAdapter recentActivityAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        
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
        
        // Setup dashboard content
        setupDashboardContent();
        
        // Observe data
        observeData();
        
        // Load default fragment
        loadFragment(new DashboardFragment());
    }
    
    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar);
        
        // Dashboard cards
        todayTasksCard = findViewById(R.id.today_tasks_card);
        pendingOrdersCard = findViewById(R.id.pending_orders_card);
        completedTodayCard = findViewById(R.id.completed_today_card);
        totalCustomersCard = findViewById(R.id.total_customers_card);
        
        // Dashboard stats
        todayTasksCount = findViewById(R.id.today_tasks_count);
        pendingOrdersCount = findViewById(R.id.pending_orders_count);
        completedTodayCount = findViewById(R.id.completed_today_count);
        totalCustomersCount = findViewById(R.id.total_customers_count);
        
        // Recent activities
        recentActivitiesRecyclerView = findViewById(R.id.recent_activities_recycler_view);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.dashboard_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
    }
    
    private void setupNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.nav_dashboard, R.string.nav_dashboard);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        
        navigationView.setNavigationItemSelectedListener(this);
        
        // Set user info in navigation header
        setupNavigationHeader();
    }
    
    private void setupBottomNavigation() {
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
    }
    
    private void setupNavigationHeader() {
        View headerView = navigationView.getHeaderView(0);
        TextView userNameText = headerView.findViewById(R.id.nav_user_name);
        TextView userEmailText = headerView.findViewById(R.id.nav_user_email);
        
        userNameText.setText(preferenceManager.getUserName());
        userEmailText.setText(preferenceManager.getUserEmail());
    }
    
    private void setupDashboardContent() {
        // Setup recent activities recycler view
        recentActivityAdapter = new RecentActivityAdapter(new ArrayList<>());
        recentActivitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentActivitiesRecyclerView.setAdapter(recentActivityAdapter);
        
        // Setup card click listeners
        setupCardClickListeners();
    }
    
    private void setupCardClickListeners() {
        todayTasksCard.setOnClickListener(v -> {
            loadFragment(new WorkOrdersFragment());
            getSupportActionBar().setTitle("Today's Work Orders");
            bottomNavigationView.setSelectedItemId(R.id.navigation_work_orders);
        });
        
        pendingOrdersCard.setOnClickListener(v -> {
            loadFragment(new WorkOrdersFragment());
            getSupportActionBar().setTitle("Pending Work Orders");
            bottomNavigationView.setSelectedItemId(R.id.navigation_work_orders);
        });
        
        completedTodayCard.setOnClickListener(v -> {
            loadFragment(new WorkOrdersFragment());
            getSupportActionBar().setTitle("Completed Today");
            bottomNavigationView.setSelectedItemId(R.id.navigation_work_orders);
        });
        
        totalCustomersCard.setOnClickListener(v -> {
            loadFragment(new CustomersFragment());
            getSupportActionBar().setTitle(R.string.customers);
        });
    }
    
    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    
    private void observeData() {
        // Observe dashboard stats
        viewModel.getTodayTasksCount().observe(this, count -> {
            todayTasksCount.setText(String.valueOf(count));
        });
        
        viewModel.getPendingOrdersCount().observe(this, count -> {
            pendingOrdersCount.setText(String.valueOf(count));
        });
        
        viewModel.getCompletedTodayCount().observe(this, count -> {
            completedTodayCount.setText(String.valueOf(count));
        });
        
        viewModel.getTotalCustomersCount().observe(this, count -> {
            totalCustomersCount.setText(String.valueOf(count));
        });
        
        // Observe recent activities
        viewModel.getRecentActivities().observe(this, activities -> {
            recentActivityAdapter.updateActivities(activities);
        });
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.nav_dashboard) {
            loadFragment(new DashboardFragment());
            getSupportActionBar().setTitle(R.string.dashboard_title);
            bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        } else if (id == R.id.nav_work_orders) {
            loadFragment(new WorkOrdersFragment());
            getSupportActionBar().setTitle(R.string.work_orders);
            bottomNavigationView.setSelectedItemId(R.id.navigation_work_orders);
        } else if (id == R.id.nav_customers) {
            loadFragment(new CustomersFragment());
            getSupportActionBar().setTitle(R.string.customers);
        } else if (id == R.id.nav_reports) {
            loadFragment(new ReportsFragment());
            getSupportActionBar().setTitle(R.string.reports);
        } else if (id == R.id.nav_profile) {
            loadFragment(new ProfileFragment());
            getSupportActionBar().setTitle(R.string.account);
            bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        } else if (id == R.id.nav_settings) {
            loadFragment(new SettingsFragment());
            getSupportActionBar().setTitle(R.string.settings);
        } else if (id == R.id.nav_offline) {
            loadFragment(new OfflineFragment());
            getSupportActionBar().setTitle(R.string.offline_mode);
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
    }
    
    private void logout() {
        // Clear user session
        preferenceManager.clearUserSession();
        
        // Navigate to login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 