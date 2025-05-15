package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.unimasmove.utils.SessionManager;

public class ScheduleActivity extends AppCompatActivity {
    private Button btnRoute1, btnRoute2, btnRoute3;
    private ImageButton btnHome, btnSchedule, btnChats, btnNotifications, btnAccount;
    private TextView profileName, profileEmail;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        session = SessionManager.getInstance();

        // Initialize views
        btnRoute1 = findViewById(R.id.btnUnimasBus);
        btnRoute2 = findViewById(R.id.btnPanBorneoBus);
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
        btnRoute1.setOnClickListener(v -> {
            Intent intent = new Intent(ScheduleActivity.this, Schedule1Activity.class);
            intent.putExtra("route_id", 1);
            startActivity(intent);
        });

        btnRoute2.setOnClickListener(v -> {
            Intent intent = new Intent(ScheduleActivity.this, Schedule2Activity.class);
            intent.putExtra("route_id", 2);
            startActivity(intent);
        });

        btnRoute3.setOnClickListener(v -> {
            Intent intent = new Intent(ScheduleActivity.this, Schedule3Activity.class);
            intent.putExtra("route_id", 3);
            startActivity(intent);
        });

        // Bottom navigation
        btnHome.setOnClickListener(v -> startActivity(new Intent(ScheduleActivity.this, HomeActivity.class)));
        btnSchedule.setOnClickListener(v -> startActivity(new Intent(ScheduleActivity.this, ScheduleActivity.class)));
        btnChats.setOnClickListener(v -> startActivity(new Intent(ScheduleActivity.this, ChatActivity.class)));
        btnNotifications.setOnClickListener(v -> startActivity(new Intent(ScheduleActivity.this, NotificationActivity.class)));
        btnAccount.setOnClickListener(v -> startActivity(new Intent(ScheduleActivity.this, SettingsActivity.class)));
    }
}