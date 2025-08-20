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
import Classes.NotificationDatabaseHelper;
import Classes.RequestDatabaseHelper;
import Classes.Request;
import java.util.List;

public class ViewBook extends AppCompatActivity {
    private TextView textView; //will rename these better
    private TextView textView2;
    private Button buttonRequestBook;
    private Button buttonBack;
    private BookDatabaseHelper bookDbHelper;
    private RequestDatabaseHelper requestDbHelper;
    private int bookId;
    private String username;
    private boolean requestInProgress = false;
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

        bookDbHelper = new BookDatabaseHelper(this);
        requestDbHelper = new RequestDatabaseHelper(this);
        bookId = getIntent().getIntExtra("book_id", -1);
        username = getIntent().getStringExtra("username");
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        buttonRequestBook = findViewById(R.id.buttonRequestBook);
        buttonBack = findViewById(R.id.buttonBack);

        if (bookId != -1) {
            loadBookDetails();
        }

        buttonRequestBook.setOnClickListener(v -> {
            if (username != null) {
                buttonRequestBook.setEnabled(false);//  disable button
                requestBook();
            } else {
                Toast.makeText(this, "Please log in to request books", Toast.LENGTH_SHORT).show();
            }
        });

        buttonBack.setOnClickListener(v -> finish());
    }


    private void loadBookDetails() {
        List<BookModel> allBooks = bookDbHelper.getAllBooks();
        BookModel book = null;
        // find the book by id
        for (BookModel b : allBooks) {
            if (b.getId() == bookId) {
                book = b;
                break;
            }
        }

        if (book != null) {
            textView.setText(book.getTitle());
            textView2.setText("Quantity: " + book.getQuantity());
        }
    }

    private void requestBook() {
        if (requestInProgress) {
            return;// prevent multiple  requests
        }
        
        requestInProgress = true;
        
        List<BookModel> allBooks = bookDbHelper.getAllBooks();
        BookModel book = null;

        for (BookModel b : allBooks) {
            if (b.getId() == bookId) {
                book = b;
                break;
            }
        }

        if (book != null) {
            if (book.getQuantity() > 0) {
                Request request = new Request();
                request.setBookId(bookId);
                request.setUsername(username);
                request.setBookTitle(book.getTitle());
                request.setMemberName(username);

                long requestId = requestDbHelper.addRequest(request);

                if (requestId != -1) {
                    NotificationDatabaseHelper.createRequestNotification(this, username, book.getTitle());
                    Toast.makeText(this, "Book request sent successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to create request", Toast.LENGTH_SHORT).show();
                    requestInProgress = false;
                    buttonRequestBook.setEnabled(true);
                }
            } else {
                Toast.makeText(this, "Book is not available", Toast.LENGTH_SHORT).show();
                requestInProgress = false;
                buttonRequestBook.setEnabled(true);
            }
        } else {
            requestInProgress = false;
            buttonRequestBook.setEnabled(true);
        }
    }
}