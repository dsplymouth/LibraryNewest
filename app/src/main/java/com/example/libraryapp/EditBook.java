package com.example.libraryapp;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import Classes.BookDatabaseHelper;
import Classes.BookModel;

public class EditBook extends AppCompatActivity {
    private EditText editTextTitle, editTextQuantity;
    private Button buttonSave, buttonDelete, buttonCancel;
    private TextView textViewTitle;
    private BookDatabaseHelper dbHelper;
    private boolean isEditMode = false;
    private BookModel currentBook = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextTitle = findViewById(R.id.editTextFirstName);
        editTextQuantity = findViewById(R.id.editTextLastName);
        buttonSave = findViewById(R.id.buttonSaveMember);
        buttonDelete = findViewById(R.id.buttonDeleteMember);
        buttonCancel = findViewById(R.id.buttonCancelMember);
        textViewTitle = findViewById(R.id.textViewEditBook);
        dbHelper = new BookDatabaseHelper(this);
        checkMode();
        setupButtonListeners();
    }

    private void checkMode() {
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");

        if ("edit".equals(mode)) {
            // edit mode
            isEditMode = true;
            textViewTitle.setText("Edit Book");
            buttonSave.setText("Update");

            String bookTitle = intent.getStringExtra("book_title");
            int quantity = intent.getIntExtra("book_quantity", 0);

            currentBook = new BookModel(0, bookTitle, quantity);

            editTextTitle.setText(bookTitle);
            editTextQuantity.setText(String.valueOf(quantity));

            buttonDelete.setVisibility(android.view.View.VISIBLE);

        } else {
            // add mode
            isEditMode = false;
            textViewTitle.setText("Add New Book");
            buttonSave.setText("Add Book");

            buttonDelete.setVisibility(android.view.View.GONE);
        }
    }

    private void setupButtonListeners() {
        buttonSave.setOnClickListener(v -> {
            if (validateInput()) {
                if (isEditMode) {
                    updateBook();
                } else {
                    addBook();
                }
            }
        });

        buttonDelete.setOnClickListener(v -> {
            if (currentBook != null) {
                deleteBook();
            }
        });

        buttonCancel.setOnClickListener(v -> finish());
    }

    private boolean validateInput() {
        String title = editTextTitle.getText().toString().trim();
        String quantityStr = editTextQuantity.getText().toString().trim();

        if (title.isEmpty()) {
            editTextTitle.setError("Book title is required");
            return false;
        }

        if (quantityStr.isEmpty()) {
            editTextQuantity.setError("Quantity is required");
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                editTextQuantity.setError("Quantity must be greater than 0");
                return false;
            }
        } catch (NumberFormatException e) {
            editTextQuantity.setError("Please enter a valid number");
            return false;
        }

        return true;
    }

    private void addBook() {
        String title = editTextTitle.getText().toString().trim();
        int quantity = Integer.parseInt(editTextQuantity.getText().toString().trim());

        // check if book already exists
        if (dbHelper.bookExists(title)) {
            Toast.makeText(this, "Book already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        // add new book
        boolean success = dbHelper.addBook(title, quantity);

        if (success) {
            Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBook() {
        String newTitle = editTextTitle.getText().toString().trim();
        int newQuantity = Integer.parseInt(editTextQuantity.getText().toString().trim());

        // check if new title same with existing book
        if (!newTitle.equals(currentBook.getTitle()) && dbHelper.bookExists(newTitle)) {
            Toast.makeText(this, "Book title already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        // update book
        boolean success = dbHelper.updateBook(currentBook.getTitle(), newTitle, newQuantity);

        if (success) {
            Toast.makeText(this, "Book updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update book", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBook() {
        // delete book
        boolean success = dbHelper.deleteBook(currentBook.getTitle());

        if (success) {
            Toast.makeText(this, "Book deleted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete book", Toast.LENGTH_SHORT).show();
        }
    }
}