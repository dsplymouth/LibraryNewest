package com.example.libraryapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import API.LibraryService;
import Classes.Member;
import Classes.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class EditMember extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextContact;
    private TextView textViewMemberEndDate;
    private Button buttonSaveMember, buttonCancelMember, buttonDeleteMember;
    private String mode;
    private String memberUsername;
    private String originalMembershipEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_member);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");
        memberUsername = intent.getStringExtra("member_username");

        if ("edit".equals(mode)) {
            loadMemberData(intent);
        }

        setupButtonListeners();
    }

    private void initializeViews() {
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContact = findViewById(R.id.editTextContact);
        textViewMemberEndDate = findViewById(R.id.textViewMemberEndDate);
        buttonSaveMember = findViewById(R.id.buttonSaveMember);
        buttonCancelMember = findViewById(R.id.buttonCancelMember);
        buttonDeleteMember = findViewById(R.id.buttonDeleteMember);
    }

    private void loadMemberData(Intent intent) {
        String firstname = intent.getStringExtra("member_firstname");
        String lastname = intent.getStringExtra("member_lastname");
        String email = intent.getStringExtra("member_email");
        String contact = intent.getStringExtra("member_contact");
        originalMembershipEndDate = intent.getStringExtra("member_membership_end_date");

        editTextFirstName.setText(firstname);
        editTextLastName.setText(lastname);
        editTextEmail.setText(email);
        editTextContact.setText(contact);
        textViewMemberEndDate.setText("Membership End Date: " + originalMembershipEndDate);
    }

    private void setupButtonListeners() {
        buttonSaveMember.setOnClickListener(v -> saveMember());
        buttonCancelMember.setOnClickListener(v -> finish());
        buttonDeleteMember.setOnClickListener(v -> deleteMember());
    }

    private void saveMember() {
        if (!validateInputs()) {
            return;
        }

        Member member = new Member();
        member.setFirstname(editTextFirstName.getText().toString().trim());
        member.setLastname(editTextLastName.getText().toString().trim());
        member.setEmail(editTextEmail.getText().toString().trim());
        member.setContact(editTextContact.getText().toString().trim());

        if ("edit".equals(mode)) {
            // convert the original date to the correct format for API
            String formattedDate = convertDateToApiFormat(originalMembershipEndDate);
            member.setMembershipEndDate(formattedDate);
            LibraryService.updateMember(this, memberUsername, member);
            Toast.makeText(this, "Member updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // generate username for new members
            String generatedUsername = generateUsername(editTextFirstName.getText().toString().trim(),
                    editTextLastName.getText().toString().trim());
            String membershipEndDate = calculateOneYearFromToday();

            member.setUsername(generatedUsername);
            member.setMembershipEndDate(membershipEndDate);
            LibraryService.addMember(this, member);
            Toast.makeText(this, "Member added successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private String convertDateToApiFormat(String originalDate) {
        // if the date is already in yyyy-MM-dd format, return it
        if (originalDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return originalDate;
        }

        // long format check
        try {
            Date date = DateUtils.convertDate(originalDate);
            if (date != null) {
                return DateUtils.apiDate(date);
            }
        } catch (Exception e) {
            return calculateOneYearFromToday();
        }
        return calculateOneYearFromToday();
    }

    private String generateUsername(String firstname, String lastname) {
        // create username from firstname + lastname + rand num
        String base = (firstname + lastname).toLowerCase().replaceAll("\\s+", "");
        int randomNum = (int)(Math.random() * 1000);
        return base + randomNum;
    }

    private String calculateOneYearFromToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        Date oneYearFromNow = calendar.getTime();
        return DateUtils.apiDate(oneYearFromNow);
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

    private void deleteMember() {
        if ("edit".equals(mode) && memberUsername != null) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Delete Member")
                    .setMessage("Are you sure you want to delete this member? This action cannot be undone.")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        LibraryService.deleteMember(this, memberUsername);
                        Toast.makeText(this, "Member deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            Toast.makeText(this, "Cannot delete member in add mode", Toast.LENGTH_SHORT).show();
        }
    }
}