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
    
    private static final int SPLASH_DURATION = 3000; // 3 seconds
    private ImageView logoImageView;
    private TextView appNameTextView;
    private TextView loadingTextView;
    private PreferenceManager preferenceManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Initialize views
        logoImageView = findViewById(R.id.logo_image_view);
        appNameTextView = findViewById(R.id.app_name_text_view);
        loadingTextView = findViewById(R.id.loading_text_view);
        
        // Initialize preference manager
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
        
        // Start animations
        startAnimations();
        
        // Check network connectivity
        checkNetworkAndProceed();
    }
    
    private void startAnimations() {
        // Logo animation
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logoImageView.startAnimation(logoAnimation);
        
        // App name animation
        Animation appNameAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        appNameTextView.startAnimation(appNameAnimation);
        
        // Loading text animation
        Animation loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
        loadingTextView.startAnimation(loadingAnimation);
    }
    
    private void checkNetworkAndProceed() {
        // Check if network is available
        if (NetworkUtils.getInstance().isNetworkAvailable()) {
            proceedToNextScreen();
        } else {
            // Show offline message and proceed anyway
            loadingTextView.setText(R.string.no_internet);
            new Handler(Looper.getMainLooper()).postDelayed(this::proceedToNextScreen, SPLASH_DURATION);
        }
    }
    
    private void proceedToNextScreen() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            
            // Check if user is logged in
            if (preferenceManager.isLoggedIn()) {
                // User is logged in, go to dashboard
                intent = new Intent(SplashActivity.this, DashboardActivity.class);
            } else {
                // User is not logged in, go to login
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
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
        if (loadingTextView != null) {
            loadingTextView.clearAnimation();
        }
    }
} 