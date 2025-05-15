package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import com.google.android.material.snackbar.Snackbar;

public abstract class BaseActivity extends AppCompatActivity {

    // Navigation button IDs
    private static final int[] NAV_BUTTON_IDS = {
            R.id.btnHome,
            R.id.btnSchedule,
            R.id.btnChats,
            R.id.btnNotifications,
            R.id.btnAccount
    };

    // Corresponding activities for each button
    private static final Class<?>[] NAV_ACTIVITIES = {
            HomeActivity.class,
            ScheduleActivity.class,
            ChatActivity.class,
            NotificationActivity.class,
            SettingsActivity.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        // Create the main layout with bottom navigation
        LinearLayout fullLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_base, null);

        // Add the activity content with proper layout params
        View activityContent = getLayoutInflater().inflate(layoutResID, fullLayout, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1.0f // Weight to take remaining space
        );
        fullLayout.addView(activityContent, 0, params);

        // Set the combined layout
        super.setContentView(fullLayout);

        // Setup navigation
        setupBottomNavigation();
        updateActiveButton();
    }

    private void setupBottomNavigation() {
        for (int i = 0; i < NAV_BUTTON_IDS.length; i++) {
            ImageButton button = findViewById(NAV_BUTTON_IDS[i]);
            final Class<?> activityClass = NAV_ACTIVITIES[i];

            button.setOnClickListener(v -> {
                if (!isCurrentActivity(activityClass)) {
                    navigateTo(activityClass);
                }
            });
        }
    }

    private void updateActiveButton() {
        for (int i = 0; i < NAV_ACTIVITIES.length; i++) {
            ImageButton button = findViewById(NAV_BUTTON_IDS[i]);
            if (button != null) {
                button.setSelected(isCurrentActivity(NAV_ACTIVITIES[i]));
            }
        }
    }

    private boolean isCurrentActivity(Class<?> activityClass) {
        return this.getClass().equals(activityClass);
    }

    protected void showToast(String message) {
        // Modern alternative to Toast
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    protected void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);

        // Clear back stack if navigating to home
        if (activityClass.equals(HomeActivity.class)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }

        // Modern activity transition
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                android.R.anim.fade_in,  // enter animation
                android.R.anim.fade_out  // exit animation
        );
        startActivity(intent, options.toBundle());
    }

    protected void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
        startActivity(intent, options.toBundle());
    }

    protected void startNewActivityClearStack(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
        );
        startActivity(intent, options.toBundle());
        finish();
    }
}