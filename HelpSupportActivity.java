package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;


public class HelpSupportActivity extends AppCompatActivity {
    private ImageButton btnHome, btnSchedule, btnChats, btnNotifications, btnAccount;
    private Button btnSubmitTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        // Initialize views
        btnHome = findViewById(R.id.btnHome);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnChats = findViewById(R.id.btnChats);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnAccount = findViewById(R.id.btnAccount);
        btnSubmitTicket = findViewById(R.id.btnSubmitTicket);

        // Submit ticket button
        btnSubmitTicket.setOnClickListener(v -> {
            Toast.makeText(HelpSupportActivity.this, "Support ticket submitted", Toast.LENGTH_SHORT).show();
            finish();
        });


        // Bottom navigation
        btnHome.setOnClickListener(v -> startActivity(new Intent(HelpSupportActivity.this, HomeActivity.class)));
        btnSchedule.setOnClickListener(v -> startActivity(new Intent(HelpSupportActivity.this, ScheduleActivity.class)));
        btnChats.setOnClickListener(v -> startActivity(new Intent(HelpSupportActivity.this, ChatActivity.class)));
        btnNotifications.setOnClickListener(v -> startActivity(new Intent(HelpSupportActivity.this, NotificationActivity.class)));
        btnAccount.setOnClickListener(v -> startActivity(new Intent(HelpSupportActivity.this, SettingsActivity.class)));
    }
}