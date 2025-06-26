package com.extrap.co.bizhub.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.adapters.CustomerAdapter;
import com.extrap.co.bizhub.data.entities.Customer;
import com.extrap.co.bizhub.viewmodels.CustomerViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CustomersFragment extends Fragment {
    
    private CustomerViewModel viewModel;
    private CustomerAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ChipGroup filterChipGroup;
    private FloatingActionButton fabAddCustomer;
    
    private List<Customer> allCustomers = new ArrayList<>();
    private String currentSearchQuery = "";
    private String currentFilter = "all";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_customers, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        setupViewModel();
        setupRecyclerView();
        setupSearchAndFilters();
        setupClickListeners();
        observeData();
    }
    
    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.customers_recycler_view);
        searchView = view.findViewById(R.id.search_view);
        filterChipGroup = view.findViewById(R.id.filter_chip_group);
        fabAddCustomer = view.findViewById(R.id.fab_add_customer);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
    }
    
    private void setupRecyclerView() {
        adapter = new CustomerAdapter(new ArrayList<>(), customer -> {
            // Handle customer click - navigate to customer details
            navigateToCustomerDetails(customer);
        }, customer -> {
            // Handle customer edit
            editCustomer(customer);
        }, customer -> {
            // Handle customer delete
            showDeleteConfirmation(customer);
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupSearchAndFilters() {
        // Setup search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                filterCustomers();
                return true;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                filterCustomers();
                return true;
            }
        });
        
        // Setup filter chips
        setupFilterChips();
    }
    
    private void setupFilterChips() {
        // Add filter chips
        addFilterChip("All", "all");
        addFilterChip("Active", "active");
        addFilterChip("Inactive", "inactive");
        addFilterChip("Premium", "premium");
        addFilterChip("Regular", "regular");
        
        filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                currentFilter = (String) chip.getTag();
                filterCustomers();
            }
        });
    }
    
    private void addFilterChip(String text, String tag) {
        Chip chip = new Chip(requireContext());
        chip.setText(text);
        chip.setTag(tag);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(true);
        filterChipGroup.addView(chip);
        
        // Set first chip as checked by default
        if (tag.equals("all")) {
            chip.setChecked(true);
        }
    }
    
    private void setupClickListeners() {
        fabAddCustomer.setOnClickListener(v -> {
            // Navigate to add customer activity
            addNewCustomer();
        });
    }
    
    private void observeData() {
        viewModel.getAllCustomers().observe(getViewLifecycleOwner(), customers -> {
            allCustomers = customers;
            filterCustomers();
        });
        
        viewModel.getSearchResults().observe(getViewLifecycleOwner(), customers -> {
            adapter.updateCustomers(customers);
        });
        
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Snackbar.make(requireView(), error, Snackbar.LENGTH_LONG).show();
            }
        });
        
        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
    
    private void filterCustomers() {
        List<Customer> filteredCustomers = new ArrayList<>();
        
        for (Customer customer : allCustomers) {
            boolean matchesSearch = currentSearchQuery.isEmpty() ||
                    customer.getName().toLowerCase().contains(currentSearchQuery.toLowerCase()) ||
                    customer.getEmail().toLowerCase().contains(currentSearchQuery.toLowerCase()) ||
                    customer.getPhone().contains(currentSearchQuery) ||
                    customer.getCompany().toLowerCase().contains(currentSearchQuery.toLowerCase());
            
            boolean matchesFilter = matchesFilterCriteria(customer);
            
            if (matchesSearch && matchesFilter) {
                filteredCustomers.add(customer);
            }
        }
        
        adapter.updateCustomers(filteredCustomers);
    }
    
    private boolean matchesFilterCriteria(Customer customer) {
        switch (currentFilter) {
            case "active":
                return customer.isActive();
            case "inactive":
                return !customer.isActive();
            case "premium":
                return customer.isPremium();
            case "regular":
                return !customer.isPremium();
            default:
                return true; // "all" filter
        }
    }
    
    private void navigateToCustomerDetails(Customer customer) {
        // TODO: Navigate to customer details activity
        Toast.makeText(getContext(), "Viewing details for " + customer.getName(), Toast.LENGTH_SHORT).show();
    }
    
    private void editCustomer(Customer customer) {
        // TODO: Navigate to edit customer activity
        Toast.makeText(getContext(), "Editing " + customer.getName(), Toast.LENGTH_SHORT).show();
    }
    
    private void addNewCustomer() {
        // TODO: Navigate to add customer activity
        Toast.makeText(getContext(), "Add new customer", Toast.LENGTH_SHORT).show();
    }
    
    private void showDeleteConfirmation(Customer customer) {
        new AlertDialog.Builder(requireContext())
            .setTitle("Delete Customer")
            .setMessage("Are you sure you want to delete " + customer.getName() + "? This action cannot be undone.")
            .setPositiveButton("Delete", (dialog, which) -> {
                viewModel.deleteCustomer(customer);
            })
            .setNegativeButton("Cancel", null)
            .show();
    }
    
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_customers, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_export_customers) {
            exportCustomers();
            return true;
        } else if (id == R.id.action_import_customers) {
            importCustomers();
            return true;
        } else if (id == R.id.action_sort_by_name) {
            sortCustomersByName();
            return true;
        } else if (id == R.id.action_sort_by_company) {
            sortCustomersByCompany();
            return true;
        } else if (id == R.id.action_sort_by_date) {
            sortCustomersByDate();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void exportCustomers() {
        viewModel.exportCustomers();
    }
    
    private void importCustomers() {
        viewModel.importCustomers();
    }
    
    private void sortCustomersByName() {
        viewModel.sortCustomersByName();
    }
    
    private void sortCustomersByCompany() {
        viewModel.sortCustomersByCompany();
    }
    
    private void sortCustomersByDate() {
        viewModel.sortCustomersByDate();
    }
} 