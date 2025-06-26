package com.extrap.co.bizhub.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.data.entities.Customer;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    
    private List<Customer> customers;
    private final OnCustomerClickListener onCustomerClickListener;
    private final OnCustomerEditListener onCustomerEditListener;
    private final OnCustomerDeleteListener onCustomerDeleteListener;
    
    public interface OnCustomerClickListener {
        void onCustomerClick(Customer customer);
    }
    
    public interface OnCustomerEditListener {
        void onCustomerEdit(Customer customer);
    }
    
    public interface OnCustomerDeleteListener {
        void onCustomerDelete(Customer customer);
    }
    
    public CustomerAdapter(List<Customer> customers, 
                          OnCustomerClickListener onCustomerClickListener,
                          OnCustomerEditListener onCustomerEditListener,
                          OnCustomerDeleteListener onCustomerDeleteListener) {
        this.customers = customers;
        this.onCustomerClickListener = onCustomerClickListener;
        this.onCustomerEditListener = onCustomerEditListener;
        this.onCustomerDeleteListener = onCustomerDeleteListener;
    }
    
    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customers.get(position);
        holder.bind(customer);
    }
    
    @Override
    public int getItemCount() {
        return customers.size();
    }
    
    public void updateCustomers(List<Customer> newCustomers) {
        this.customers = newCustomers;
        notifyDataSetChanged();
    }
    
    class CustomerViewHolder extends RecyclerView.ViewHolder {
        
        private MaterialCardView cardView;
        private TextView nameTextView;
        private TextView companyTextView;
        private TextView emailTextView;
        private TextView phoneTextView;
        private TextView addressTextView;
        private TextView dateAddedTextView;
        private Chip statusChip;
        private Chip typeChip;
        private ImageButton editButton;
        private ImageButton deleteButton;
        
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardView = itemView.findViewById(R.id.customer_card);
            nameTextView = itemView.findViewById(R.id.customer_name);
            companyTextView = itemView.findViewById(R.id.customer_company);
            emailTextView = itemView.findViewById(R.id.customer_email);
            phoneTextView = itemView.findViewById(R.id.customer_phone);
            addressTextView = itemView.findViewById(R.id.customer_address);
            dateAddedTextView = itemView.findViewById(R.id.customer_date_added);
            statusChip = itemView.findViewById(R.id.customer_status_chip);
            typeChip = itemView.findViewById(R.id.customer_type_chip);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
        
        public void bind(Customer customer) {
            nameTextView.setText(customer.getName());
            companyTextView.setText(customer.getCompany());
            emailTextView.setText(customer.getEmail());
            phoneTextView.setText(customer.getPhone());
            addressTextView.setText(customer.getAddress());
            
            // Format date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            dateAddedTextView.setText("Added: " + dateFormat.format(customer.getDateAdded()));
            
            // Set status chip
            if (customer.isActive()) {
                statusChip.setText("Active");
                statusChip.setChipBackgroundColorResource(R.color.success_light);
                statusChip.setTextColor(itemView.getContext().getColor(R.color.success));
            } else {
                statusChip.setText("Inactive");
                statusChip.setChipBackgroundColorResource(R.color.error_light);
                statusChip.setTextColor(itemView.getContext().getColor(R.color.error));
            }
            
            // Set type chip
            if (customer.isPremium()) {
                typeChip.setText("Premium");
                typeChip.setChipBackgroundColorResource(R.color.warning_light);
                typeChip.setTextColor(itemView.getContext().getColor(R.color.warning));
            } else {
                typeChip.setText("Regular");
                typeChip.setChipBackgroundColorResource(R.color.info_light);
                typeChip.setTextColor(itemView.getContext().getColor(R.color.info));
            }
            
            // Set click listeners
            cardView.setOnClickListener(v -> {
                if (onCustomerClickListener != null) {
                    onCustomerClickListener.onCustomerClick(customer);
                }
            });
            
            editButton.setOnClickListener(v -> {
                if (onCustomerEditListener != null) {
                    onCustomerEditListener.onCustomerEdit(customer);
                }
            });
            
            deleteButton.setOnClickListener(v -> {
                if (onCustomerDeleteListener != null) {
                    onCustomerDeleteListener.onCustomerDelete(customer);
                }
            });
        }
    }
} 