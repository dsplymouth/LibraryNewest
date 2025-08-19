package com.example.libraryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Settings extends AppCompatActivity {
    private SwitchCompat switchNotifications;
    private Button buttonBack;
    private Button buttonSignOut;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("LibraryAppSettings", MODE_PRIVATE);
        initializeViews();
        loadSettings();
        setupButtonListeners();
    }

    private void initializeViews() {
        switchNotifications = findViewById(R.id.switchNotif);
        buttonBack = findViewById(R.id.buttonBack);
        buttonSignOut = findViewById(R.id.buttonSignOut);
    }

    private void loadSettings() {
        // load their preferences
        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);
        switchNotifications.setChecked(notificationsEnabled);
    }

    private void setupButtonListeners() {
        // notification on or off
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("notifications_enabled", isChecked).apply();
            Toast.makeText(this, "Notifications " + (isChecked ? "enabled" : "disabled"), Toast.LENGTH_SHORT).show();
        });
        buttonBack.setOnClickListener(v -> finish()); //back
        buttonSignOut.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}