package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class BusRouteActivity extends BaseActivity {
    private Button btnRoute1, btnRoute2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busroute);

        // Initialize route-specific buttons
        btnRoute1 = findViewById(R.id.btnUnimasBus);
        btnRoute2 = findViewById(R.id.btnPanBorneoBus);

        // Set click listeners for route buttons
        btnRoute1.setOnClickListener(v -> {
            Intent intent = new Intent(BusRouteActivity.this, Schedule1Activity.class);
            intent.putExtra("route_id", 1);
            startActivity(intent);
        });

        btnRoute2.setOnClickListener(v -> {
            Intent intent = new Intent(BusRouteActivity.this, Schedule2Activity.class);
            intent.putExtra("route_id", 2);
            startActivity(intent);
        });
    }
}