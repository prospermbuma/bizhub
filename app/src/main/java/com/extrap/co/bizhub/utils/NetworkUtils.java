package com.extrap.co.bizhub.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

public class NetworkUtils {
    
    private static NetworkUtils instance;
    private Context context;
    private ConnectivityManager connectivityManager;
    private boolean isConnected = false;
    private NetworkCallback networkCallback;
    
    public NetworkUtils(Context context) {
        this.context = context.getApplicationContext();
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        setupNetworkCallback();
    }
    
    public static void init(Context context) {
        if (instance == null) {
            instance = new NetworkUtils(context);
        }
    }
    
    public static NetworkUtils getInstance() {
        return instance;
    }
    
    public boolean isNetworkAvailable() {
        if (connectivityManager == null) return false;
        
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;
        
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        return capabilities != null && (
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        );
    }
    
    public boolean isWifiConnected() {
        if (connectivityManager == null) return false;
        
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;
        
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
    }
    
    public boolean isMobileDataConnected() {
        if (connectivityManager == null) return false;
        
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) return false;
        
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    private void setupNetworkCallback() {
        networkCallback = new NetworkCallback();
        NetworkRequest networkRequest = new NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }
    
    public void setNetworkCallbackListener(NetworkCallbackListener listener) {
        if (networkCallback != null) {
            networkCallback.setListener(listener);
        }
    }
    
    public interface NetworkCallbackListener {
        void onNetworkAvailable();
        void onNetworkLost();
    }
    
    private class NetworkCallback extends ConnectivityManager.NetworkCallback {
        private NetworkCallbackListener listener;
        
        public void setListener(NetworkCallbackListener listener) {
            this.listener = listener;
        }
        
        @Override
        public void onAvailable(Network network) {
            isConnected = true;
            if (listener != null) {
                listener.onNetworkAvailable();
            }
        }
        
        @Override
        public void onLost(Network network) {
            isConnected = false;
            if (listener != null) {
                listener.onNetworkLost();
            }
        }
    }
} 