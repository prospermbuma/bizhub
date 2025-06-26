package com.extrap.co.bizhub.services;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {
    
    private static final String TAG = "BluetoothService";
    private static final UUID SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler handler;
    
    private boolean isConnected = false;
    private OnBluetoothEventListener eventListener;
    
    public interface OnBluetoothEventListener {
        void onDeviceConnected(BluetoothDevice device);
        void onDeviceDisconnected();
        void onDataReceived(String data);
        void onError(String error);
    }
    
    public BluetoothService(Context context) {
        this.context = context;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.handler = new Handler(Looper.getMainLooper());
    }
    
    public void setEventListener(OnBluetoothEventListener listener) {
        this.eventListener = listener;
    }
    
    public boolean isBluetoothAvailable() {
        return bluetoothAdapter != null;
    }
    
    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public void connectToDevice(BluetoothDevice device) {
        new Thread(() -> {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(SERVICE_UUID);
                bluetoothSocket.connect();
                
                inputStream = bluetoothSocket.getInputStream();
                outputStream = bluetoothSocket.getOutputStream();
                
                isConnected = true;
                
                handler.post(() -> {
                    if (eventListener != null) {
                        eventListener.onDeviceConnected(device);
                    }
                });
                
                // Start listening for data
                startListening();
                
            } catch (IOException e) {
                Log.e(TAG, "Error connecting to device", e);
                handler.post(() -> {
                    if (eventListener != null) {
                        eventListener.onError("Connection failed: " + e.getMessage());
                    }
                });
            }
        }).start();
    }
    
    public void disconnect() {
        new Thread(() -> {
            try {
                if (bluetoothSocket != null) {
                    bluetoothSocket.close();
                }
                isConnected = false;
                
                handler.post(() -> {
                    if (eventListener != null) {
                        eventListener.onDeviceDisconnected();
                    }
                });
                
            } catch (IOException e) {
                Log.e(TAG, "Error disconnecting", e);
            }
        }).start();
    }
    
    public void sendData(String data) {
        if (!isConnected || outputStream == null) {
            if (eventListener != null) {
                eventListener.onError("Not connected to device");
            }
            return;
        }
        
        new Thread(() -> {
            try {
                outputStream.write(data.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Error sending data", e);
                handler.post(() -> {
                    if (eventListener != null) {
                        eventListener.onError("Failed to send data: " + e.getMessage());
                    }
                });
            }
        }).start();
    }
    
    private void startListening() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            int bytes;
            
            while (isConnected) {
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes > 0) {
                        String data = new String(buffer, 0, bytes);
                        handler.post(() -> {
                            if (eventListener != null) {
                                eventListener.onDataReceived(data);
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error reading data", e);
                    break;
                }
            }
        }).start();
    }
    
    // Hardware-specific methods
    public void printReceipt(String receiptData) {
        // Format receipt data for thermal printer
        String formattedData = formatReceiptData(receiptData);
        sendData(formattedData);
    }
    
    public void scanBarcode() {
        // Send scan command to barcode scanner
        sendData("SCAN");
    }
    
    public void readRFID() {
        // Send read command to RFID reader
        sendData("READ_RFID");
    }
    
    public void connectToPrinter() {
        // Connect to thermal printer
        // This would typically involve discovering and connecting to a specific device
    }
    
    public void connectToScanner() {
        // Connect to barcode scanner
        // This would typically involve discovering and connecting to a specific device
    }
    
    private String formatReceiptData(String data) {
        // Format data for thermal printer
        StringBuilder formatted = new StringBuilder();
        formatted.append("\n");
        formatted.append("=== FIELD SERVICE RECEIPT ===\n");
        formatted.append(data);
        formatted.append("\n");
        formatted.append("=============================\n");
        formatted.append("\n\n\n"); // Cut paper
        return formatted.toString();
    }
} 