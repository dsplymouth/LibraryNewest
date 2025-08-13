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

import API.LibraryService;
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
        testDatabase();
        syncBooksFromAPI();


    }
    private void testDatabase() {
        BookDatabaseHelper dbHelper = new BookDatabaseHelper(this);

        // add a test book
        BookModel testBook = new BookModel("Test Book", 5);
        boolean added = dbHelper.addBook(testBook);
        Log.d("Database", "Test book added: " + added);

        // get all books
        List<BookModel> books = dbHelper.getAllBooks();
        Log.d("Database", "Total books in database: " + books.size());

        for (BookModel book : books) {
            Log.d("Database", "Book: " + book.getTitle() + ", Quantity: " + book.getQuantity());
        }

        Toast.makeText(this, "Database test: " + books.size() + " books found", Toast.LENGTH_SHORT).show();
    }

    // sync books from API - UPDATED VERSION
    private void syncBooksFromAPI() {
        BookDatabaseHelper dbHelper = new BookDatabaseHelper(this);
        Toast.makeText(this, "Starting API sync...", Toast.LENGTH_SHORT).show();
        LibraryService.syncBooksFromAPI(this, dbHelper);
    }
}