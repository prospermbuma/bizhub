package com.extrap.co.bizhub.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.extrap.co.bizhub.R;
import com.extrap.co.bizhub.data.entities.WorkOrder;
import com.extrap.co.bizhub.viewmodels.MapViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final float DEFAULT_ZOOM = 12f;
    
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private MapViewModel viewModel;
    private Map<Marker, WorkOrder> markerWorkOrderMap;
    
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MapViewModel.class);
        
        // Initialize marker map
        markerWorkOrderMap = new HashMap<>();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup map
        setupMap();
        
        // Observe data
        observeData();
        
        // Get location permission
        getLocationPermission();
    }
    
    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Work Orders Map");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    
    private void observeData() {
        viewModel.getWorkOrders().observe(this, workOrders -> {
            if (mMap != null) {
                updateMapMarkers(workOrders);
            }
        });
        
        viewModel.getCurrentLocation().observe(this, location -> {
            if (location != null && mMap != null) {
                updateCurrentLocation(location);
            }
        });
    }
    
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        
        // Enable location if permission granted
        if (locationPermissionGranted) {
            enableMyLocation();
        }
        
        // Setup map settings
        setupMapSettings();
        
        // Load work orders
        viewModel.loadWorkOrders();
        
        // Get current location
        getDeviceLocation();
    }
    
    private void setupMapSettings() {
        if (mMap != null) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            
            // Setup marker click listener
            mMap.setOnMarkerClickListener(marker -> {
                WorkOrder workOrder = markerWorkOrderMap.get(marker);
                if (workOrder != null) {
                    showWorkOrderDetails(workOrder);
                }
                return true;
            });
        }
    }
    
    private void updateMapMarkers(List<WorkOrder> workOrders) {
        // Clear existing markers
        mMap.clear();
        markerWorkOrderMap.clear();
        
        // Add new markers
        for (WorkOrder workOrder : workOrders) {
            if (workOrder.getLatitude() != 0 && workOrder.getLongitude() != 0) {
                LatLng position = new LatLng(workOrder.getLatitude(), workOrder.getLongitude());
                
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(position)
                        .title(workOrder.getWorkOrderNumber())
                        .snippet(workOrder.getDescription())
                        .icon(getMarkerIcon(workOrder.getPriority(), workOrder.getStatus()));
                
                Marker marker = mMap.addMarker(markerOptions);
                markerWorkOrderMap.put(marker, workOrder);
            }
        }
    }
    
    private com.google.android.gms.maps.model.BitmapDescriptor getMarkerIcon(String priority, String status) {
        if ("completed".equals(status)) {
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        } else if ("urgent".equals(priority) || "high".equals(priority)) {
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        } else if ("medium".equals(priority)) {
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        } else {
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        }
    }
    
    private void updateCurrentLocation(Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        
        // Add current location marker
        MarkerOptions currentLocationMarker = new MarkerOptions()
                .position(currentLatLng)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        
        mMap.addMarker(currentLocationMarker);
        
        // Move camera to current location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
    }
    
    private void showWorkOrderDetails(WorkOrder workOrder) {
        // TODO: Show work order details dialog or navigate to details activity
        Toast.makeText(this, "Work Order: " + workOrder.getWorkOrderNumber(), Toast.LENGTH_SHORT).show();
    }
    
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    
    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }
    
    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            lastKnownLocation = location;
                            viewModel.setCurrentLocation(location);
                        }
                    });
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                if (mMap != null) {
                    enableMyLocation();
                    getDeviceLocation();
                }
            } else {
                Toast.makeText(this, "Location permission is required to show your location on the map", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_my_location) {
            if (lastKnownLocation != null) {
                LatLng currentLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
            }
            return true;
        } else if (id == R.id.action_map_type_normal) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            return true;
        } else if (id == R.id.action_map_type_satellite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        } else if (id == R.id.action_map_type_hybrid) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
} 