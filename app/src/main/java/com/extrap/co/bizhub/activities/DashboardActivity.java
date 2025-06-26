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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.adapters.RecentActivityAdapter;
import com.extrap.co.bizhub.data.entities.WorkOrder;
import com.extrap.co.bizhub.utils.PreferenceManager;
import com.extrap.co.bizhub.viewmodels.DashboardViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
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
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        
        // Setup dashboard content
        setupDashboardContent();
        
        // Observe data
        observeData();
    }
    
    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
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
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("filter", "today");
            startActivity(intent);
        });
        
        pendingOrdersCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("filter", "pending");
            startActivity(intent);
        });
        
        completedTodayCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("filter", "completed_today");
            startActivity(intent);
        });
        
        totalCustomersCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerActivity.class);
            startActivity(intent);
        });
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
            // Already on dashboard
        } else if (id == R.id.nav_work_orders) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_customers) {
            startActivity(new Intent(this, CustomerActivity.class));
        } else if (id == R.id.nav_inventory) {
            startActivity(new Intent(this, InventoryActivity.class));
        } else if (id == R.id.nav_schedule) {
            startActivity(new Intent(this, ScheduleActivity.class));
        } else if (id == R.id.nav_reports) {
            startActivity(new Intent(this, ReportActivity.class));
        } else if (id == R.id.nav_map) {
            startActivity(new Intent(this, MapActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_account) {
            startActivity(new Intent(this, AccountActivity.class));
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(this, HelpActivity.class));
        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(this, ContactActivity.class));
        } else if (id == R.id.nav_logout) {
            logout();
        }
        
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    
    private void logout() {
        // Clear user data
        preferenceManager.clearUserData();
        
        // Navigate to login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        
        Snackbar.make(drawerLayout, R.string.logout_success, Snackbar.LENGTH_SHORT).show();
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
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 