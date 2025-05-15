package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

public class RegistrationSuccessActivity extends AppCompatActivity {
    private static final int REDIRECT_DELAY = 3000; // 3 seconds delay
    private Handler handler;
    private Runnable redirectRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_success);

        // Get user type from intent extras (default to student if not specified)
        String userType = getIntent().getStringExtra("userType");
        boolean isDriver = "driver".equalsIgnoreCase(userType);

        // Initialize Handler with main looper
        handler = new Handler(Looper.getMainLooper());

        redirectRunnable = () -> {
            // Determine which login activity to navigate to
            Class<?> loginActivityClass = isDriver ?
                    DriverLoginActivity.class : StudentLoginActivity.class;

            // Create and start the intent with transition animation
            Intent loginIntent = new Intent(this, loginActivityClass);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                    this,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
            startActivity(loginIntent, options.toBundle());

            // Finish this activity so user can't go back to it
            finish();
        };

        handler.postDelayed(redirectRunnable, REDIRECT_DELAY);

        // Modern back press handling
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (handler != null && redirectRunnable != null) {
                    handler.removeCallbacks(redirectRunnable);
                }
                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                        RegistrationSuccessActivity.this,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                );
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up to prevent memory leaks
        if (handler != null && redirectRunnable != null) {
            handler.removeCallbacks(redirectRunnable);
        }
    }
}