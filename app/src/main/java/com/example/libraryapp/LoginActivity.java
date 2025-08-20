package com.example.libraryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import java.util.List;

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
            String emailOrUsername = editTextEmailAddress.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(emailOrUsername)) {
                Toast.makeText(LoginActivity.this, "Please enter email or username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(LoginActivity.this, StaffDashboard.class);
            intent.putExtra("username", emailOrUsername);
            startActivity(intent);
        });

        buttonMemberLogin.setOnClickListener(v -> {
            String emailOrUsername = editTextEmailAddress.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(emailOrUsername)) {
                Toast.makeText(LoginActivity.this, "Please enter email or username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                return;
            }

            validateMemberLogin(emailOrUsername);
        });
    }

    private void validateMemberLogin(String emailOrUsername) {
        final String finalEmailOrUsername = emailOrUsername;
        Toast.makeText(this, "Checking credentials...", Toast.LENGTH_SHORT).show();

        if (isValidEmail(finalEmailOrUsername)) {
            LibraryService.getAllMembers(this, new LibraryService.MembersListCallback() {
                @Override
                public void onSuccess(List<Member> members) {
                    final Member foundMember = findMemberByEmail(members, finalEmailOrUsername);

                    if (foundMember != null) {
                        runOnUiThread(() -> {
                            Intent intent = new Intent(LoginActivity.this, MemberDashboard.class);
                            intent.putExtra("username", foundMember.getUsername());
                            startActivity(intent);
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show());
                    }
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login failed: " + error, Toast.LENGTH_LONG).show());
                }
            });
        } else {
            LibraryService.getMember(this, finalEmailOrUsername, new LibraryService.MemberCallback() {
                @Override
                public void onSuccess(Member member) {
                    runOnUiThread(() -> {
                        Intent intent = new Intent(LoginActivity.this, MemberDashboard.class);
                        intent.putExtra("username", finalEmailOrUsername);
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

    private Member findMemberByEmail(List<Member> members, String email) {
        for (Member member : members) {
            if (email.equals(member.getEmail())) {
                return member;
            }
        }
        return null;
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}