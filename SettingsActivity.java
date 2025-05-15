package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.unimasmove.utils.SessionManager;

public class SettingsActivity extends AppCompatActivity {
    private ImageButton btnHome, btnSchedule, btnChats, btnNotifications, btnAccount;
    private TextView userName, userRole;
    private SwitchCompat gpsSwitch, chatSwitch, notificationSwitch;
    private Button logoutButton;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        session = SessionManager.getInstance();

        // Initialize views
        btnHome = findViewById(R.id.btnHome);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnChats = findViewById(R.id.btnChats);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnAccount = findViewById(R.id.btnAccount);
        userName = findViewById(R.id.userName);
        userRole = findViewById(R.id.userRole);
        gpsSwitch = findViewById(R.id.gpsSwitch);
        chatSwitch = findViewById(R.id.chatSwitch);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        logoutButton = findViewById(R.id.logoutButton);

        // Set user info
        userName.setText(session.getUsername());
        userRole.setText(session.getUserType().equals("student") ? "Student" : "Bus Driver");

        // Set click listeners for settings options
        findViewById(R.id.editProfileLayout).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, EditStudentProfileActivity.class));
        });

        findViewById(R.id.helpSupportLayout).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, HelpSupportActivity.class));
        });

        findViewById(R.id.feedbackLayout).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, FeedbackActivity.class));
        });

        // Set switch listeners
        gpsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle GPS toggle
        });

        chatSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle chat toggle
        });

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle notification toggle
        });

        // Logout button
        logoutButton.setOnClickListener(v -> {
            session.logoutUser();
            startActivity(new Intent(SettingsActivity.this, StudentLoginActivity.class));
            finish();
        });

        // Bottom navigation
        btnHome.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, HomeActivity.class)));
        btnSchedule.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, ScheduleActivity.class)));
        btnChats.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, ChatActivity.class)));
        btnNotifications.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, NotificationActivity.class)));
        btnAccount.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, SettingsActivity.class)));
    }
}