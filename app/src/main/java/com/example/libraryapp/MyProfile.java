package com.example.libraryapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import API.LibraryService;
import Classes.Member;

public class MyProfile extends AppCompatActivity {

    private TextView textViewFirstNameProfile;
    private TextView textViewLastNameProfile;
    private TextView textViewEmailProfile;
    private TextView textViewContactProfile;
    private TextView textViewMemberEndDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewFirstNameProfile = findViewById(R.id.textViewFirstNameProfile);
        textViewLastNameProfile = findViewById(R.id.textViewLastNameProfile);
        textViewEmailProfile = findViewById(R.id.textViewEmailProfile);
        textViewContactProfile = findViewById(R.id.textViewContactProfile);
        textViewMemberEndDate = findViewById(R.id.textViewMemberEndDate);
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            loadMemberProfile(username);
        }
    }

    private void loadMemberProfile(String username) {
        LibraryService.getMember(this, username, new LibraryService.MemberCallback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Member member) {
                runOnUiThread(() -> {
                    // display member data
                    textViewFirstNameProfile.setText(getString(R.string.first_name) + " " + member.getFirstname());
                    textViewLastNameProfile.setText(getString(R.string.last_name) + " " + member.getLastname());
                    textViewEmailProfile.setText(getString(R.string.email) + " " + member.getEmail());
                    textViewContactProfile.setText(getString(R.string.contact) + " " + member.getContact());
                    textViewMemberEndDate.setText(getString(R.string.membership_end_date) + " " + member.getMembershipEndDate());
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    // show error in text views
                    textViewFirstNameProfile.setText(R.string.error_loading_profile);
                    textViewLastNameProfile.setText("");
                    textViewEmailProfile.setText("");
                    textViewContactProfile.setText("");
                    textViewMemberEndDate.setText("");
                });
            }
        });
    }
}