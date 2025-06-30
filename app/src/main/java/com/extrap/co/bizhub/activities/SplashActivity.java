package com.extrap.co.bizhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.extrap.co.bizhub.R;

public class SplashActivity extends AppCompatActivity {
    
    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DURATION = 2000; // 2 seconds
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "SplashActivity onCreate started");
        
        try {
            setContentView(R.layout.activity_splash);
            Log.d(TAG, "SplashActivity layout set successfully");
            
            // Initialize views
            ImageView logoImageView = findViewById(R.id.logo_image_view);
            TextView appNameTextView = findViewById(R.id.app_name_text_view);
            TextView statusTextView = findViewById(R.id.status_text_view);
            
            if (logoImageView != null) {
                Log.d(TAG, "Logo ImageView found");
            } else {
                Log.e(TAG, "Logo ImageView is null");
            }
            
            if (appNameTextView != null) {
                Log.d(TAG, "App name TextView found");
            } else {
                Log.e(TAG, "App name TextView is null");
            }
            
            if (statusTextView != null) {
                Log.d(TAG, "Status TextView found");
                statusTextView.setText("Loading FSP...");
            } else {
                Log.e(TAG, "Status TextView is null");
            }
            
            // Navigate after delay
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Log.d(TAG, "Splash delay completed, navigating to LoginActivity");
                try {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    // Add smooth transition
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } catch (Exception e) {
                    Log.e(TAG, "Error navigating to LoginActivity", e);
                    // Fallback to MainActivity
                    try {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } catch (Exception e2) {
                        Log.e(TAG, "Error navigating to MainActivity", e2);
                    }
                }
            }, SPLASH_DURATION);
            
        } catch (Exception e) {
            Log.e(TAG, "Error in SplashActivity onCreate", e);
            // Emergency fallback - try to go directly to LoginActivity
            try {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            } catch (Exception e2) {
                Log.e(TAG, "Emergency fallback to LoginActivity failed", e2);
                // Final fallback to MainActivity
                try {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } catch (Exception e3) {
                    Log.e(TAG, "Final emergency fallback failed", e3);
                }
            }
        }
    }
} 