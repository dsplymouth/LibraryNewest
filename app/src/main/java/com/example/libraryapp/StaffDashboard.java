package com.example.libraryapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Classes.BookDatabaseHelper;
import Classes.BookModel;
import java.util.List;

public class StaffDashboard extends AppCompatActivity {
    private Button buttonManageMembers;
    private Button buttonManageBooks;
    private Button buttonRequests;
    private Button buttonNotifications;
    private Button buttonSettings;
    private String username;

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

        username = getIntent().getStringExtra("username");

        buttonManageMembers = findViewById(R.id.buttonViewMembers);
        buttonManageBooks = findViewById(R.id.buttonManageBooks);
        buttonRequests = findViewById(R.id.buttonApproveRequests);
        buttonNotifications = findViewById(R.id.buttonNotifications);
        buttonSettings = findViewById(R.id.buttonSettings);

        // set up buttons
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        buttonManageMembers.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboard.this, ManageMembers.class);
            startActivity(intent);
        });

        buttonManageBooks.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboard.this, ManageBooks.class);
            startActivity(intent);
        });

        buttonRequests.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboard.this, Requests.class);
            startActivity(intent);
        });

        buttonNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboard.this, Notifications.class);
            startActivity(intent);
        });

        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(StaffDashboard.this, Settings.class);
            startActivity(intent);
        });
    }
}