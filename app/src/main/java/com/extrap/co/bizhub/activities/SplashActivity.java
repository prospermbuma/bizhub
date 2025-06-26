package com.extrap.co.bizhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.utils.NetworkUtils;
import com.extrap.co.bizhub.utils.PreferenceManager;

public class SplashActivity extends AppCompatActivity {
    
    private static final int SPLASH_DURATION = 2000; // 2 seconds
    private ImageView logoImageView;
    private TextView appNameTextView;
    private TextView statusTextView;
    private PreferenceManager preferenceManager;
    private NetworkUtils networkUtils;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Initialize managers
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        networkUtils = FieldServiceApp.getInstance().getNetworkUtils();
        
        // Initialize views
        initializeViews();
        
        // Start animations
        startAnimations();
        
        // Check authentication and navigate
        checkAuthenticationAndNavigate();
    }
    
    private void initializeViews() {
        logoImageView = findViewById(R.id.logo_image_view);
        appNameTextView = findViewById(R.id.app_name_text_view);
        statusTextView = findViewById(R.id.status_text_view);
    }
    
    private void startAnimations() {
        // Logo animation
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImageView.startAnimation(logoAnimation);
        
        // App name animation
        Animation nameAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        appNameTextView.startAnimation(nameAnimation);
        
        // Status animation
        Animation statusAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
        statusTextView.startAnimation(statusAnimation);
    }
    
    private void checkAuthenticationAndNavigate() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Check if user is logged in
            if (preferenceManager.isLoggedIn()) {
                // Check network connectivity
                if (networkUtils.isNetworkAvailable()) {
                    statusTextView.setText("Connecting to server...");
                    // TODO: Validate session with server
                    navigateToDashboard();
                } else {
                    statusTextView.setText("Offline mode");
                    // Allow offline access if previously logged in
                    navigateToDashboard();
                }
            } else {
                statusTextView.setText("Welcome to BizHub");
                navigateToLogin();
            }
        }, SPLASH_DURATION);
    }
    
    private void navigateToDashboard() {
        Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
    
    private void navigateToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop animations to prevent memory leaks
        if (logoImageView != null) {
            logoImageView.clearAnimation();
        }
        if (appNameTextView != null) {
            appNameTextView.clearAnimation();
        }
        if (statusTextView != null) {
            statusTextView.clearAnimation();
        }
    }
} 