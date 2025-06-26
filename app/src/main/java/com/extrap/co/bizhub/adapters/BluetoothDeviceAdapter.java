package com.extrap.co.bizhub.adapters;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;

import java.util.List;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceViewHolder> {
    
    private List<BluetoothDevice> devices;
    private OnItemClickListener onItemClickListener;
    
    public interface OnItemClickListener {
        void onItemClick(BluetoothDevice device);
    }
    
    public BluetoothDeviceAdapter(List<BluetoothDevice> devices) {
        this.devices = devices;
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bluetooth_device, parent, false);
        return new DeviceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        BluetoothDevice device = devices.get(position);
        holder.bind(device);
    }
    
    @Override
    public int getItemCount() {
        return devices.size();
    }
    
    class DeviceViewHolder extends RecyclerView.ViewHolder {
        
        private TextView deviceName;
        private TextView deviceAddress;
        private TextView deviceType;
        
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            
            deviceName = itemView.findViewById(R.id.device_name);
            deviceAddress = itemView.findViewById(R.id.device_address);
            deviceType = itemView.findViewById(R.id.device_type);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(devices.get(position));
                }
            });
        }
        
        public void bind(BluetoothDevice device) {
            String name = device.getName();
            if (name == null || name.isEmpty()) {
                name = "Unknown Device";
            }
            deviceName.setText(name);
            
            deviceAddress.setText(device.getAddress());
            
            String type = getDeviceTypeString(device.getType());
            deviceType.setText(type);
        }
        
        private String getDeviceTypeString(int type) {
            switch (type) {
                case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                    return "Classic";
                case BluetoothDevice.DEVICE_TYPE_LE:
                    return "Low Energy";
                case BluetoothDevice.DEVICE_TYPE_DUAL:
                    return "Dual";
                case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                default:
                    return "Unknown";
            }
        }
    }
} 