package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class NotificationActivity extends AppCompatActivity {
    private ImageButton btnHome, btnSchedule, btnChats, btnNotifications, btnAccount;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private NotificationPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Initialize views
        btnHome = findViewById(R.id.btnHome);
        btnSchedule = findViewById(R.id.btnSchedule);
        btnChats = findViewById(R.id.btnChats);
        btnNotifications = findViewById(R.id.btnNotifications);
        btnAccount = findViewById(R.id.btnAccount);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Setup ViewPager and TabLayout
        pagerAdapter = new NotificationPagerAdapter(this);
        pagerAdapter.addFragment(NotificationFragment.newInstance("All"), "All");
        pagerAdapter.addFragment(NotificationFragment.newInstance("Read"), "Read");
        pagerAdapter.addFragment(NotificationFragment.newInstance("Unread"), "Unread");

        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(pagerAdapter.getTitle(position))
        ).attach();

        // Bottom navigation
        btnHome.setOnClickListener(v -> startActivity(new Intent(NotificationActivity.this, HomeActivity.class)));
        btnSchedule.setOnClickListener(v -> startActivity(new Intent(NotificationActivity.this, ScheduleActivity.class)));
        btnChats.setOnClickListener(v -> startActivity(new Intent(NotificationActivity.this, ChatActivity.class)));
        btnNotifications.setOnClickListener(v -> startActivity(new Intent(NotificationActivity.this, NotificationActivity.class)));
        btnAccount.setOnClickListener(v -> startActivity(new Intent(NotificationActivity.this, SettingsActivity.class)));
    }
}