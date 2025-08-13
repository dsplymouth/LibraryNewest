package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import API.LibraryService;

public class MemberDashboard extends AppCompatActivity {
    private Button buttonViewBooks;
    private Button buttonMyRequests;
    private Button buttonMyProfile;
    private Button buttonNotifications;
    private Button buttonMyBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_member_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonViewBooks = findViewById(R.id.buttonBrowseBooks);
        buttonMyRequests = findViewById(R.id.buttonMyRequests);
        buttonMyProfile = findViewById(R.id.buttonMyProfile);
        buttonNotifications = findViewById(R.id.buttonNotifications);
        buttonMyBooks = findViewById(R.id.buttonMyBooksTitle);

        buttonViewBooks.setOnClickListener(v -> {
            Intent intent = new Intent(MemberDashboard.this, ViewBooks.class);
            startActivity(intent);
        });

        buttonMyRequests.setOnClickListener(v -> {
            Intent intent = new Intent(MemberDashboard.this, MyRequests.class);
            startActivity(intent);
        });

        buttonMyProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MemberDashboard.this, MyProfile.class);
            startActivity(intent);
        });

        buttonNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(MemberDashboard.this, ViewBooks.class);
            startActivity(intent);
        });

        buttonMyBooks.setOnClickListener(v -> {
            Intent intent = new Intent(MemberDashboard.this, ViewOwnedBooks.class);
            startActivity(intent);
        });
    }
}