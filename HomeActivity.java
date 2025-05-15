package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unimasmove.utils.SessionManager;

public class HomeActivity extends AppCompatActivity {
    private Button btnUnimasBus, btnPanBorneoBus;
    private ImageButton btnHome, btnSchedule, btnChats, btnNotifications, btnAccount;
    private TextView profileName, profileEmail;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize SessionManager
        SessionManager.init(this); // Make sure it's initialized
        session = SessionManager.getInstance();

        // Initialize views
        btnUnimasBus = findViewById(R.id.btnUnimasBus);
        btnPanBorneoBus = findViewById(R.id.btnPanBorneoBus);
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
        btnUnimasBus.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, BusRouteActivity.class)));
        btnPanBorneoBus.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, BusRouteActivity.class)));

        // Bottom navigation
        btnHome.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, HomeActivity.class)));
        btnSchedule.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ScheduleActivity.class)));
        btnChats.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ChatActivity.class)));
        btnNotifications.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, NotificationActivity.class)));
        btnAccount.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, SettingsActivity.class)));
    }
}
