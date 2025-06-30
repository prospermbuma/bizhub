package com.extrap.co.bizhub.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.adapters.BluetoothDeviceAdapter;
import com.extrap.co.bizhub.services.BluetoothService;
import com.extrap.co.bizhub.viewmodels.HardwareViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HardwareActivity extends AppCompatActivity {
    
    private static final int REQUEST_ENABLE_BT = 1001;
    private static final int PERMISSION_REQUEST_CODE = 1002;
    
    private HardwareViewModel viewModel;
    private BluetoothService bluetoothService;
    private BluetoothDeviceAdapter deviceAdapter;
    
    // UI Components
    private SwitchMaterial bluetoothSwitch;
    private MaterialCardView connectionStatusCard;
    private TextView connectionStatusText;
    private TextView connectedDeviceText;
    private RecyclerView deviceRecyclerView;
    private MaterialButton scanButton;
    private MaterialButton printButton;
    private MaterialButton scanBarcodeButton;
    private MaterialButton readRfidButton;
    
    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> discoveredDevices;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware);
        
        // Initialize Bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothService = new BluetoothService(this);
        discoveredDevices = new ArrayList<>();
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(HardwareViewModel.class);
        
        // Initialize views
        initializeViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup click listeners
        setupClickListeners();
        
        // Setup Bluetooth service
        setupBluetoothService();
        
        // Register Bluetooth receiver
        registerBluetoothReceiver();
        
        // Check permissions
        checkPermissions();
    }
    
    private void initializeViews() {
        bluetoothSwitch = findViewById(R.id.bluetooth_switch);
        connectionStatusCard = findViewById(R.id.connection_status_card);
        connectionStatusText = findViewById(R.id.connection_status_text);
        connectedDeviceText = findViewById(R.id.connected_device_text);
        deviceRecyclerView = findViewById(R.id.device_recycler_view);
        scanButton = findViewById(R.id.scan_button);
        printButton = findViewById(R.id.print_button);
        scanBarcodeButton = findViewById(R.id.scan_barcode_button);
        readRfidButton = findViewById(R.id.read_rfid_button);
    }
    
    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Hardware & Bluetooth");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupRecyclerView() {
        deviceAdapter = new BluetoothDeviceAdapter(discoveredDevices);
        deviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deviceRecyclerView.setAdapter(deviceAdapter);
        
        deviceAdapter.setOnItemClickListener(device -> {
            connectToDevice(device);
        });
    }
    
    private void setupClickListeners() {
        bluetoothSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                enableBluetooth();
            } else {
                disableBluetooth();
            }
        });
        
        scanButton.setOnClickListener(v -> {
            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                startDeviceDiscovery();
            } else {
                Toast.makeText(this, "Please enable Bluetooth first", Toast.LENGTH_SHORT).show();
            }
        });
        
        printButton.setOnClickListener(v -> {
            if (bluetoothService.isConnected()) {
                printTestReceipt();
            } else {
                Toast.makeText(this, "Please connect to a printer first", Toast.LENGTH_SHORT).show();
            }
        });
        
        scanBarcodeButton.setOnClickListener(v -> {
            if (bluetoothService.isConnected()) {
                bluetoothService.scanBarcode();
            } else {
                Toast.makeText(this, "Please connect to a scanner first", Toast.LENGTH_SHORT).show();
            }
        });
        
        readRfidButton.setOnClickListener(v -> {
            if (bluetoothService.isConnected()) {
                bluetoothService.readRFID();
            } else {
                Toast.makeText(this, "Please connect to an RFID reader first", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setupBluetoothService() {
        bluetoothService.setEventListener(new BluetoothService.OnBluetoothEventListener() {
            @Override
            public void onDeviceConnected(BluetoothDevice device) {
                runOnUiThread(() -> {
                    connectionStatusText.setText("Connected");
                    connectionStatusText.setTextColor(getColor(R.color.success));
                    connectedDeviceText.setText("Device: " + device.getName());
                    Toast.makeText(HardwareActivity.this, "Connected to " + device.getName(), Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onDeviceDisconnected() {
                runOnUiThread(() -> {
                    connectionStatusText.setText("Disconnected");
                    connectionStatusText.setTextColor(getColor(R.color.error));
                    connectedDeviceText.setText("No device connected");
                    Toast.makeText(HardwareActivity.this, "Device disconnected", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onDataReceived(String data) {
                runOnUiThread(() -> {
                    Toast.makeText(HardwareActivity.this, "Data received: " + data, Toast.LENGTH_SHORT).show();
                    // Handle received data based on type
                    handleReceivedData(data);
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(HardwareActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    private void handleReceivedData(String data) {
        if (data.startsWith("BARCODE:")) {
            String barcode = data.substring(8);
            // Handle barcode data
            Toast.makeText(this, "Barcode scanned: " + barcode, Toast.LENGTH_SHORT).show();
        } else if (data.startsWith("RFID:")) {
            String rfid = data.substring(5);
            // Handle RFID data
            Toast.makeText(this, "RFID read: " + rfid, Toast.LENGTH_SHORT).show();
        }
    }
    
    private void enableBluetooth() {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
    
    private void disableBluetooth() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothService.disconnect();
            bluetoothAdapter.disable();
        }
    }
    
    private void startDeviceDiscovery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            discoveredDevices.clear();
            deviceAdapter.notifyDataSetChanged();
            bluetoothAdapter.startDiscovery();
            scanButton.setText("Scanning...");
            scanButton.setEnabled(false);
        }
    }
    
    private void connectToDevice(BluetoothDevice device) {
        bluetoothService.connectToDevice(device);
    }
    
    private void printTestReceipt() {
        String testReceipt = "Test Receipt\n" +
                "Work Order: WO-001\n" +
                "Customer: John Doe\n" +
                "Service: Equipment Repair\n" +
                "Amount: $150.00\n" +
                "Date: " + java.time.LocalDate.now();
        
        bluetoothService.printReceipt(testReceipt);
    }
    
    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        
        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }
        
        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }
    
    private void registerBluetoothReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothReceiver, filter);
    }
    
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && !discoveredDevices.contains(device)) {
                    discoveredDevices.add(device);
                    deviceAdapter.notifyDataSetChanged();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                scanButton.setText("Scan for Devices");
                scanButton.setEnabled(true);
            }
        }
    };
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
            } else {
                bluetoothSwitch.setChecked(false);
                Toast.makeText(this, "Bluetooth is required for hardware features", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            
            if (!allGranted) {
                Toast.makeText(this, "Bluetooth permissions are required", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hardware, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_connect_printer) {
            // TODO: Show printer connection dialog
            Toast.makeText(this, "Printer connection coming soon", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_connect_scanner) {
            // TODO: Show scanner connection dialog
            Toast.makeText(this, "Scanner connection coming soon", Toast.LENGTH_SHORT).show();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
        bluetoothService.disconnect();
    }
} 