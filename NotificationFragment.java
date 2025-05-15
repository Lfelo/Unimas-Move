package com.example.unimasmove;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    private static final String ARG_FILTER = "filter";
    private String filter;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String filter) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILTER, filter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filter = getArguments().getString(ARG_FILTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_notification, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.notificationRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // In a real app, you would filter notifications based on the filter parameter
        List<Notification> notifications = getSampleNotifications();
        NotificationAdapter adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<Notification> getSampleNotifications() {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification("Bus Delay", "Route 1 is delayed by 15 minutes", "10:30 AM", false));
        notifications.add(new Notification("System Update", "New features available in version 2.0", "Yesterday", true));
        notifications.add(new Notification("Maintenance", "App will be down for maintenance tonight", "2 days ago", true));
        return notifications;
    }
}