package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unimasmove.utils.SessionManager;

public class Schedule1Activity extends AppCompatActivity {
    private Button btnStartTracking;
    private ImageButton btnHome, btnSchedule, btnChats, btnNotifications, btnAccount;
    private TextView profileName, profileEmail;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule1);

        // Initialize SessionManager with context
        SessionManager.init(this);  // Initialize SessionManager
        session = SessionManager.getInstance();  // Now you can safely get the instance

        // Initialize views
        btnStartTracking = findViewById(R.id.btnStartTracking);
        btnHome = findViewById(R.id.btnHome);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnChats = findViewById(R.id.btnChats);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnAccount = findViewById(R.id.btnAccount);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);

        // Set user info
        profileName.setText(session.getUsername());
        profileEmail.setText(session.getEmail());

        // Set click listeners
        btnStartTracking.setOnClickListener(v -> {
            Intent intent = new Intent(Schedule1Activity.this, LiveMapActivity.class);
            intent.putExtra("route_id", 1);  // Pass route ID to LiveMapActivity
            startActivity(intent);
        });

        // Bottom navigation
        btnHome.setOnClickListener(v -> startActivity(new Intent(Schedule1Activity.this, HomeActivity.class)));
        btnSchedule.setOnClickListener(v -> startActivity(new Intent(Schedule1Activity.this, ScheduleActivity.class)));
        btnChats.setOnClickListener(v -> startActivity(new Intent(Schedule1Activity.this, ChatActivity.class)));
        btnNotifications.setOnClickListener(v -> startActivity(new Intent(Schedule1Activity.this, NotificationActivity.class)));
        btnAccount.setOnClickListener(v -> startActivity(new Intent(Schedule1Activity.this, SettingsActivity.class)));
    }
}
