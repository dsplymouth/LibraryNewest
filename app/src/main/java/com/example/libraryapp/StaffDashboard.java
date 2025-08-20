package com.example.libraryapp;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import android.content.Intent;
import Classes.NotificationDatabaseHelper;
import androidx.activity.EdgeToEdge;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StaffDashboard extends AppCompatActivity {
    private Button ButtonViewMembers;
    private Button buttonManageBooks;
    private Button buttonApproveRequests;
    private Button btnNotifications;
    private Button buttonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_staff_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getIntent().getStringExtra("username");

        ButtonViewMembers = findViewById(R.id.buttonViewMembers);
        buttonManageBooks = findViewById(R.id.buttonManageBooks);
        buttonApproveRequests = findViewById(R.id.buttonApproveRequests);
        btnNotifications = findViewById(R.id.buttonNotifications);
        buttonSettings = findViewById(R.id.buttonSettings);

        setupButtonListeners();
        updateNotificationCount();
        NotificationDatabaseHelper.removeDuplicateNotifications(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNotificationCount();
    }

    @SuppressLint("SetTextI18n")
    private void updateNotificationCount() {
        int count = NotificationDatabaseHelper.getStaffUnreadNotificationCount(this);
        if (count > 0) {
            btnNotifications.setText("Notifications (" + count + ")");
        } else {
            btnNotifications.setText("Notifications");
        }

    }

    private void setupButtonListeners() {
        // setup navigation buttons
        ButtonViewMembers.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboard.this, ManageMembers.class);
            startActivity(intent);
        });

        buttonManageBooks.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboard.this, ManageBooks.class);
            startActivity(intent);
        });


        buttonApproveRequests.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboard.this, Requests.class);
            startActivity(intent);
        });


        btnNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboard.this, Notifications.class);
            intent.putExtra("isStaff", true);
            startActivity(intent);
        });

        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboard.this, Settings.class);
            startActivity(intent);
        });
    }
}