package com.example.libraryapp;
import Classes.NotificationDatabaseHelper;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MemberDashboard extends AppCompatActivity {
    private Button buttonViewBooks;
    private Button buttonMyRequests;
    private Button buttonMyProfile;
    private Button buttonNotifications;


    private String username;

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
        // assing the buttons to their ids on the activity
        buttonViewBooks = findViewById(R.id.buttonBrowseBooks);
        buttonMyRequests = findViewById(R.id.buttonMyRequests);
        buttonMyProfile = findViewById(R.id.buttonMyProfile);
        buttonNotifications = findViewById(R.id.buttonNotifications);

        username = getIntent().getStringExtra("username");

        buttonViewBooks.setOnClickListener(v -> {
            Intent intent = new Intent(MemberDashboard.this, ViewBooks.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
        buttonMyRequests.setOnClickListener(v -> {
            Intent intent = new Intent(MemberDashboard.this, MyRequests.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });


        buttonMyProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MemberDashboard.this, MyProfile.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        buttonNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(MemberDashboard.this, Notifications.class);
            intent.putExtra("username", username);
            intent.putExtra("isStaff", false);
            startActivity(intent);
        });


        
        updateNotificationCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNotificationCount(); // refresh count when returning displays on
    }

    private void updateNotificationCount() {
        int count = NotificationDatabaseHelper.getMemberUnreadNotificationCount(this, username);
        if (count > 0) {
            buttonNotifications.setText("Notifications (" + count + ")");
        } else {
            buttonNotifications.setText("Notifications");
        }
    }
}