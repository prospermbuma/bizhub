package com.extrap.co.bizhub.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.extrap.co.bizhub.FieldServiceApp;
import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.data.entities.User;
import com.extrap.co.bizhub.utils.PreferenceManager;
import com.extrap.co.bizhub.viewmodels.ProfileViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileFragment extends Fragment {
    
    private ProfileViewModel viewModel;
    private PreferenceManager preferenceManager;
    
    // Views
    private ImageView profileImageView;
    private TextView userNameText;
    private TextView userEmailText;
    private TextView userRoleText;
    private TextView joinDateText;
    private MaterialCardView editProfileCard;
    private MaterialCardView accountSettingsCard;
    private MaterialCardView securityCard;
    private MaterialButton logoutButton;
    
    // Edit Profile Views
    private TextInputLayout firstNameLayout;
    private TextInputLayout lastNameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout phoneLayout;
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText phoneEditText;
    private MaterialButton saveButton;
    private MaterialButton cancelButton;
    
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private boolean isEditMode = false;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupViewModel();
        setupImagePicker();
        setupClickListeners();
        loadUserProfile();
    }
    
    private void initializeViews(View view) {
        // Profile display views
        profileImageView = view.findViewById(R.id.profile_image_view);
        userNameText = view.findViewById(R.id.user_name_text);
        userEmailText = view.findViewById(R.id.user_email_text);
        userRoleText = view.findViewById(R.id.user_role_text);
        joinDateText = view.findViewById(R.id.join_date_text);
        editProfileCard = view.findViewById(R.id.edit_profile_card);
        accountSettingsCard = view.findViewById(R.id.account_settings_card);
        securityCard = view.findViewById(R.id.security_card);
        logoutButton = view.findViewById(R.id.logout_button);
        
        // Edit profile views
        firstNameLayout = view.findViewById(R.id.first_name_layout);
        lastNameLayout = view.findViewById(R.id.last_name_layout);
        emailLayout = view.findViewById(R.id.email_layout);
        phoneLayout = view.findViewById(R.id.phone_layout);
        firstNameEditText = view.findViewById(R.id.first_name_edit_text);
        lastNameEditText = view.findViewById(R.id.last_name_edit_text);
        emailEditText = view.findViewById(R.id.email_edit_text);
        phoneEditText = view.findViewById(R.id.phone_edit_text);
        saveButton = view.findViewById(R.id.save_button);
        cancelButton = view.findViewById(R.id.cancel_button);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        preferenceManager = FieldServiceApp.getInstance().getPreferenceManager();
    }
    
    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    if (selectedImage != null) {
                        profileImageView.setImageURI(selectedImage);
                        // TODO: Save image to storage and update profile
                        Toast.makeText(getContext(), "Profile picture updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        );
    }
    
    private void setupClickListeners() {
        // Profile image click
        profileImageView.setOnClickListener(v -> {
            if (isEditMode) {
                openImagePicker();
            }
        });
        
        // Edit profile card
        editProfileCard.setOnClickListener(v -> {
            if (!isEditMode) {
                enableEditMode();
            }
        });
        
        // Account settings card
        accountSettingsCard.setOnClickListener(v -> {
            // TODO: Navigate to account settings
            Toast.makeText(getContext(), "Account settings", Toast.LENGTH_SHORT).show();
        });
        
        // Security card
        securityCard.setOnClickListener(v -> {
            showSecurityOptions();
        });
        
        // Logout button
        logoutButton.setOnClickListener(v -> {
            showLogoutConfirmation();
        });
        
        // Save button
        saveButton.setOnClickListener(v -> {
            saveProfile();
        });
        
        // Cancel button
        cancelButton.setOnClickListener(v -> {
            disableEditMode();
        });
    }
    
    private void loadUserProfile() {
        // Load user data from preferences
        String userName = preferenceManager.getUserName();
        String userEmail = preferenceManager.getUserEmail();
        String userRole = preferenceManager.getUserRole();
        
        userNameText.setText(userName);
        userEmailText.setText(userEmail);
        userRoleText.setText(userRole.substring(0, 1).toUpperCase() + userRole.substring(1));
        joinDateText.setText("Member since December 2024"); // TODO: Get actual join date
        
        // Load user data from database
        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                firstNameEditText.setText(user.getFirstName());
                lastNameEditText.setText(user.getLastName());
                emailEditText.setText(user.getEmail());
                phoneEditText.setText(user.getPhone());
            }
        });
    }
    
    private void enableEditMode() {
        isEditMode = true;
        editProfileCard.setVisibility(View.GONE);
        firstNameLayout.setVisibility(View.VISIBLE);
        lastNameLayout.setVisibility(View.VISIBLE);
        emailLayout.setVisibility(View.VISIBLE);
        phoneLayout.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        
        // Make profile image clickable for editing
        profileImageView.setClickable(true);
        profileImageView.setFocusable(true);
    }
    
    private void disableEditMode() {
        isEditMode = false;
        editProfileCard.setVisibility(View.VISIBLE);
        firstNameLayout.setVisibility(View.GONE);
        lastNameLayout.setVisibility(View.GONE);
        emailLayout.setVisibility(View.GONE);
        phoneLayout.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        
        // Make profile image non-clickable
        profileImageView.setClickable(false);
        profileImageView.setFocusable(false);
        
        // Reset form data
        loadUserProfile();
    }
    
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }
    
    private void saveProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        
        // Validate input
        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Update profile
        viewModel.updateProfile(firstName, lastName, email, phone);
        
        // Update preferences
        preferenceManager.setUserName(firstName + " " + lastName);
        preferenceManager.setUserEmail(email);
        
        // Update display
        userNameText.setText(firstName + " " + lastName);
        userEmailText.setText(email);
        
        // Disable edit mode
        disableEditMode();
        
        Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }
    
    private void showSecurityOptions() {
        String[] options = {"Change Password", "Two-Factor Authentication", "Login History"};
        
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Security Settings")
            .setItems(options, (dialog, which) -> {
                switch (which) {
                    case 0:
                        // TODO: Navigate to change password
                        Toast.makeText(getContext(), "Change password", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // TODO: Navigate to 2FA settings
                        Toast.makeText(getContext(), "Two-factor authentication", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        // TODO: Show login history
                        Toast.makeText(getContext(), "Login history", Toast.LENGTH_SHORT).show();
                        break;
                }
            })
            .show();
    }
    
    private void showLogoutConfirmation() {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout", (dialog, which) -> {
                // Clear user session
                preferenceManager.clearUserSession();
                
                // Navigate to login
                if (getActivity() != null) {
                    getActivity().finish();
                }
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
} 