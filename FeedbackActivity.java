package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {
    private ImageButton btnHome, btnSchedule, btnChats, btnNotifications, btnAccount;
    private RatingBar ratingBar;
    private TextInputEditText etFeedback;
    private Button btnSubmitFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize views
        btnHome = findViewById(R.id.btnHome);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnChats = findViewById(R.id.btnChats);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnAccount = findViewById(R.id.btnAccount);
        ratingBar = findViewById(R.id.ratingBar);
        etFeedback = findViewById(R.id.etFeedback);
        btnSubmitFeedback = findViewById(R.id.btnSubmitFeedback);

        // Submit feedback button
        btnSubmitFeedback.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String feedback = etFeedback.getText().toString().trim();

            if (feedback.isEmpty()) {
                Toast.makeText(this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
                return;
            }

            // Process feedback (in a real app, this would send to server)
            Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Bottom navigation
        btnHome.setOnClickListener(v -> startActivity(new Intent(FeedbackActivity.this, HomeActivity.class)));
        btnSchedule.setOnClickListener(v -> startActivity(new Intent(FeedbackActivity.this, ScheduleActivity.class)));
        btnChats.setOnClickListener(v -> startActivity(new Intent(FeedbackActivity.this, ChatActivity.class)));
        btnNotifications.setOnClickListener(v -> startActivity(new Intent(FeedbackActivity.this, NotificationActivity.class)));
        btnAccount.setOnClickListener(v -> startActivity(new Intent(FeedbackActivity.this, SettingsActivity.class)));
    }
}