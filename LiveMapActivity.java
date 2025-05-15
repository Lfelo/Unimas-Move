package com.example.unimasmove;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.unimasmove.events.LocationUpdateEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.example.unimasmove.events.PredictionUpdateEvent;
import com.example.unimasmove.utils.SessionManager;
import com.example.unimasmove.network.RetrofitClient;
import com.example.unimasmove.network.model.RouteResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.ArrayList;

public class LiveMapActivity extends BaseActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private GoogleMap mMap;
    private int routeId;
    private TextView tvDelayPrediction;
    private TextView tvBusInfo;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livemap);

        session = SessionManager.getInstance();
        routeId = getIntent().getIntExtra("route_id", 1);

        tvDelayPrediction = findViewById(R.id.tvDelayPrediction);
        tvBusInfo = findViewById(R.id.tvBusInfo);

        updatePredictionDisplay();
        setupMap();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setupMap() {
        if (checkLocationPermission()) {
            initMap();
        } else {
            requestLocationPermission();
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap();
            } else {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Enable location layer - new recommended approach
            try {
                mMap.setLocationSource(null); // Clear any existing location source
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                // Location will be provided by your LocationUpdateService
            } catch (SecurityException e) {
                Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            }
        }

        updateBusInfo();
        drawRouteOnMap();
    }

    private void updateBusInfo() {
        String routeName = getRouteName(routeId);
        tvBusInfo.setText(routeName + " - Next stop: " + getNextStop(routeId));
    }

    private void updatePredictionDisplay() {
        int delay = session.getLastPredictedDelay();
        double confidence = session.getLastPredictionConfidence();

        if (delay > 0) {
            String predictionText = String.format(
                    "Predicted delay: %d minutes (%.0f%% confidence)",
                    delay, confidence * 100);
            tvDelayPrediction.setText(predictionText);
        } else {
            tvDelayPrediction.setText("No delay prediction available");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationUpdate(LocationUpdateEvent event) {
        if (mMap != null) {
            // Update map with new location
            LatLng position = new LatLng(event.getLatitude(), event.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(position).title("Your Location"));
            // TODO: Add bus location markers if available
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPredictionUpdate(PredictionUpdateEvent event) {
        updatePredictionDisplay();
    }

    private String getRouteName(int routeId) {
        switch (routeId) {
            case 1: return "UNIMAS Route 1";
            case 2: return "UNIMAS Route 2";
            case 3: return "PanBorneo";
            default: return "UNIMAS Route";
        }
    }

    private String getNextStop(int routeId) {
        // TODO: Implement actual next stop logic
        return "FCSIT";
    }

    private void drawRouteOnMap() {
        RetrofitClient.getApiService(this).getRouteDetails(session.getCurrentRoute())
                .enqueue(new Callback<RouteResponse>() {
                    @Override
                    public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Decode the polyline for the route and draw on the map
                            List<LatLng> points = decodePolyline(response.body().getEncodedPath());
                            mMap.addPolyline(new PolylineOptions()
                                    .addAll(points)
                                    .width(12)
                                    .color(ResourcesCompat.getColor(getResources(), android.R.color.holo_blue_light, getTheme())));

                            // Optionally, you can add bus stop markers as well
                            for (LatLng stop : getBusStops(routeId)) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(stop)
                                        .title("Bus Stop")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RouteResponse> call, Throwable t) {
                        // Handle failure to load route
                        Toast.makeText(LiveMapActivity.this, "Failed to load route", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Helper method to decode polyline
    private List<LatLng> decodePolyline(String encoded) {
        return PolyUtil.decode(encoded);
    }

    private List<LatLng> getBusStops(int routeId) {
        List<LatLng> stops = new ArrayList<>();
        // Example bus stop locations
        stops.add(new LatLng(1.4567, 110.4567));
        stops.add(new LatLng(1.4578, 110.4578));
        return stops;
    }
}