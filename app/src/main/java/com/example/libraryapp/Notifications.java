package com.example.libraryapp;
import Classes.NotifAdapter;
import Classes.NotifItem;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import java.util.ArrayList;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import Classes.NotificationDatabaseHelper;



public class Notifications extends AppCompatActivity {
    private RecyclerView recyclerViewNotifications;
    private Button buttonBack;
    private Button buttonMarkAllRead;
    private NotifAdapter notificationAdapter;
    private List<NotifItem> notificationsList;
    private String username;
    private boolean isStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username = getIntent().getStringExtra("username");
        isStaff = getIntent().getBooleanExtra("isStaff", false);

        initialiseViews();
        setupRecyclerView();
        setupButtonListeners();
    }

    private void initialiseViews() {
        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        buttonBack = findViewById(R.id.buttonBack);
        buttonMarkAllRead = findViewById(R.id.buttonMarkAllRead);
    }

    private void setupRecyclerView() {
        notificationsList = new ArrayList<>();
        notificationAdapter = new NotifAdapter(this, notificationsList);

        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotifications.setAdapter(notificationAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications();
    }

    private void setupButtonListeners() {
        buttonBack.setOnClickListener(v -> finish());
        buttonMarkAllRead.setOnClickListener(v -> {
            if (isStaff) {
                NotificationDatabaseHelper.markAllStaffNotificationsAsRead(this);
            } else {
                NotificationDatabaseHelper.markAllMemberNotificationsAsRead(this, username);
            }
            loadNotifications();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadNotifications() {
        Log.d("DEBUG", "Loading notifications for " + (isStaff ? "staff" : "member: " + username));

        if (isStaff) {
            notificationsList = NotificationDatabaseHelper.getStaffNotifications(this);
        } else {
            notificationsList = NotificationDatabaseHelper.getMemberNotifications(this, username);
        }

        Log.d("NotificationDebug", "Loaded " + notificationsList.size() + " notifications");
        // System.out.println("notifications loaded"); // old debug line
        notificationAdapter.notifyDataSetChanged();
    }
}