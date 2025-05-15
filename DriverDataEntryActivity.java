package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unimasmove.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DriverDataEntryActivity extends AppCompatActivity {
    private Button btnGenerateMap;
    private ImageButton btnHome, btnSchedule, btnChats, btnNotifications, btnAccount;
    private TextView tvDriverName, tvBusInfo, tvPlateNumber, tvCurrentDate, tvCurrentTime;
    private Handler timeHandler;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverdataentry);

        SessionManager.init(this);
        session = SessionManager.getInstance();

        // Initialize views
        initializeViews();

        // Set driver info
        setDriverInfo();

        // Set up date and time updates
        setupDateTimeUpdates();

        // Set up button click listeners
        setupButtonListeners();
    }

    private void initializeViews() {
        btnGenerateMap = findViewById(R.id.btnGenerateMap);
        btnHome = findViewById(R.id.btnHome);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnChats = findViewById(R.id.btnChats);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnAccount = findViewById(R.id.btnAccount);
        tvDriverName = findViewById(R.id.tvDriverName);
        tvBusInfo = findViewById(R.id.tvBusInfo);
        tvPlateNumber = findViewById(R.id.tvPlateNumber);
        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
    }

    private void setDriverInfo() {
        tvDriverName.setText(session.getUsername());
        tvBusInfo.setText("UNIMAS Route 1");
        tvPlateNumber.setText("Plate: QAB1234");
    }

    private void setupDateTimeUpdates() {
        updateDateTime();
        timeHandler = new Handler(Looper.getMainLooper()); // Updated to use main looper
        timeHandler.postDelayed(timeRunnable, 1000);
    }

    private void updateDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());

        String currentDate = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());

        tvCurrentDate.setText(currentDate);
        tvCurrentTime.setText(currentTime);
    }

    private void setupButtonListeners() {
        // Generate Map button
        btnGenerateMap.setOnClickListener(v -> {
            Intent intent = new Intent(DriverDataEntryActivity.this, LiveMapActivity.class);
            intent.putExtra("route_id", 1);
            startActivity(intent);
        });

        // Bottom navigation buttons
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        btnSchedule.setOnClickListener(v -> startActivity(new Intent(this, ScheduleActivity.class)));
        btnChats.setOnClickListener(v -> startActivity(new Intent(this, ChatActivity.class)));
        btnNotifications.setOnClickListener(v -> startActivity(new Intent(this, NotificationActivity.class)));
        btnAccount.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            updateDateTime();
            timeHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeHandler.removeCallbacks(timeRunnable);
    }
}