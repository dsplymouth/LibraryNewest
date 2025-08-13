package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import API.LibraryService;
import Classes.Member;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmailAddress;
    private EditText editTextPassword;
    private Button buttonStaffLogin;
    private Button buttonMemberLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        buttonStaffLogin = findViewById(R.id.buttonStaffLogin);
        buttonMemberLogin = findViewById(R.id.buttonMemberLogin);


        buttonStaffLogin.setOnClickListener(v -> {
            String username = editTextEmailAddress.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(LoginActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(LoginActivity.this, StaffDashboard.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });


        buttonMemberLogin.setOnClickListener(v -> {
            String username = editTextEmailAddress.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(LoginActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                return;
            }

            validateMemberLogin(username);
        });
    }

    private void validateMemberLogin(String username) {
        Toast.makeText(this, "Checking username...", Toast.LENGTH_SHORT).show();
        LibraryService.getMember(this, username, new LibraryService.MemberCallback() {
            @Override
            public void onSuccess(Member member) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(LoginActivity.this, MemberDashboard.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                });
            }

            @Override
            public void onError(String error) {

                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show());
            }
        });
    }
}