package com.example.libraryapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import Classes.BookModel;
import Classes.BookDatabaseHelper;
import Classes.Request;
import Classes.RequestDatabaseHelper;

import java.util.List;

public class ViewBook extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewStatus;
    private Button buttonRequestBook;
    private Button buttonBack;
    private int bookId;
    private String username;
    private BookDatabaseHelper bookDbHelper;
    private RequestDatabaseHelper requestDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupButtonListeners();

        // get book id and username from prev
        bookId = getIntent().getIntExtra("book_id", -1);
        username = getIntent().getStringExtra("username");
        bookDbHelper = new BookDatabaseHelper(this);
        requestDbHelper = new RequestDatabaseHelper(this);

        if (bookId != -1) {
            loadBookDetails(bookId);
        } else {
            Toast.makeText(this, "Error: Book ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        textViewTitle = findViewById(R.id.textView);
        textViewStatus = findViewById(R.id.textView2);
        buttonRequestBook = findViewById(R.id.buttonRequestBook);
        buttonBack = findViewById(R.id.buttonBack);
    }

    private void setupButtonListeners() {
        buttonBack.setOnClickListener(v -> finish());

        buttonRequestBook.setOnClickListener(v -> {
            if (username != null) {
                requestBook();
            } else {
                Toast.makeText(this, "Error: Username not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBookDetails(int bookId) {
        // get book from local database
        List<BookModel> books = bookDbHelper.getAllBooks();
        BookModel book = null;

        for (BookModel b : books) {
            if (b.getId() == bookId) {
                book = b;
                break;
            }
        }

        if (book != null) {
            displayBookDetails(book);
        } else {
            Toast.makeText(this, "Error: Book not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayBookDetails(BookModel book) {
        textViewTitle.setText("Title: " + book.getTitle());
        String status = book.getQuantity() > 0 ? "Available" : "Not Available";
        textViewStatus.setText("Status: " + status);
        if (book.getQuantity() > 0) {
            buttonRequestBook.setEnabled(true);
        } else {
            buttonRequestBook.setEnabled(false);
            buttonRequestBook.setText("Not Available");
        }
    }

    private void requestBook() {

        List<BookModel> books = bookDbHelper.getAllBooks();
        BookModel book = null;

        for (BookModel b : books) {
            if (b.getId() == bookId) {
                book = b;
                break;
            }
        }

        if (book != null) {
            Request request = new Request(bookId, username, book.getTitle(), username);
            long result = requestDbHelper.addRequest(request);
            if (result != -1) {
                Toast.makeText(this, "Request sent successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error creating request", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
}