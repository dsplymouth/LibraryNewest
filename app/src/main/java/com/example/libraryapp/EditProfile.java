package com.example.libraryapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import java.util.Calendar;
import java.util.Date;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import API.LibraryService;
import Classes.Member;
import Classes.DateUtils;

public class EditProfile extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextContact;
    private TextView textViewMemberEndDate;
    private Button buttonSaveMember, buttonCancelMember;
    private String username;
    private Member currentMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialiseViews();
        setupButtonListeners();
        username = getIntent().getStringExtra("username");
        if (username != null) {
            loadMemberData(username);
        }
    }

    private void initialiseViews() {
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContact = findViewById(R.id.editTextContact);
        textViewMemberEndDate = findViewById(R.id.textViewMemberEndDate);


        buttonSaveMember = findViewById(R.id.buttonSaveMember);
        buttonCancelMember = findViewById(R.id.buttonCancelMember);
    }

    private void setupButtonListeners() {
        buttonSaveMember.setOnClickListener(v -> saveMember());
        buttonCancelMember.setOnClickListener(v -> finish());
    }

    private void loadMemberData(String username) {
        LibraryService.getMember(this, username, new LibraryService.MemberCallback() {
            @Override
            public void onSuccess(Member member) {
                runOnUiThread(() -> {
                    currentMember = member;
                    populateFields();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(EditProfile.this, "Error loading profile: " + error, Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void populateFields() {
        if (currentMember != null) {
            editTextFirstName.setText(currentMember.getFirstname());
            editTextLastName.setText(currentMember.getLastname());
            editTextEmail.setText(currentMember.getEmail());
            editTextContact.setText(currentMember.getContact());

            // membership end date is read-only, just display it for staff and members
            textViewMemberEndDate.setText(getString(R.string.membership_end_date) + " " + currentMember.getMembershipEndDate());
        }
    }

    private void saveMember() {
        if (!validateInputs()) {
            return;
        }

        Member updatedMember = new Member();
        updatedMember.setFirstname(editTextFirstName.getText().toString().trim());
        updatedMember.setLastname(editTextLastName.getText().toString().trim());
        updatedMember.setEmail(editTextEmail.getText().toString().trim());
        updatedMember.setContact(editTextContact.getText().toString().trim());

        // keep original membership end date unchanged
        String formattedDate = convertDateToApiFormat(currentMember.getMembershipEndDate());
        updatedMember.setMembershipEndDate(formattedDate);

        LibraryService.updateMember(this, username, updatedMember);
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean validateInputs() {
        String firstname = editTextFirstName.getText().toString().trim();
        String lastname = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();

        if (firstname.isEmpty()) {
            editTextFirstName.setError("First name is required");
            return false;
        }

        if (lastname.isEmpty()) {
            editTextLastName.setError("Last name is required");
            return false;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email address");
            return false;
        }

        if (contact.isEmpty()) {
            editTextContact.setError("Contact number is required");
            return false;
        }

        if (contact.length() < 10) {
            editTextContact.setError("Contact number must be at least 10 digits");
            return false;
        }

        return true;
    }
    // Check if date is already in the correct format (YYYY-MM-DD)
    private String convertDateToApiFormat(String originalDate) {
        if (originalDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return originalDate;  // Already in correct format so its fine and it gets returned
        }
        try { // try to convert it otherwise and if it doesnt work it goes to the fallback
            Date date = DateUtils.convertDate(originalDate);
            if (date != null) {
                return DateUtils.apiDate(date);
            }
        } catch (Exception e) {

            return calculateOneYearFromToday();
        }
        return calculateOneYearFromToday();
    }

    private String calculateOneYearFromToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date oneYearFromNow = calendar.getTime();
        return DateUtils.apiDate(oneYearFromNow);
        //simple using calendar to add one year to today's date
    }
}