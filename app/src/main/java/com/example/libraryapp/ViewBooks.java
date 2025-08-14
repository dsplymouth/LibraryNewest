package com.example.libraryapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import Classes.BookModel;
import Classes.BookAdapter;
import Classes.BookDatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ViewBooks extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private EditText searchEditText;
    private List<BookModel> allBooksList;
    private String currentUsername;
    private Button buttonBack;
    private BookDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_books);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchViewBooksMember);
        buttonBack = findViewById(R.id.buttonBack);
        // get username from username passed from previous activity
        currentUsername = getIntent().getStringExtra("username");

        dbHelper = new BookDatabaseHelper(this);
        setupBackButton();
        setupRecyclerView();
        setupSearch();
        loadBooksFromDatabase();
    }

    private void setupRecyclerView() {
        allBooksList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, allBooksList, false, currentUsername); // false = member mode (view)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bookAdapter);
    }
    private void setupBackButton() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> finish());
        }
    }
    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadBooksFromDatabase() {
        Toast.makeText(this, "Loading books...", Toast.LENGTH_SHORT).show();

        // load books from local database
        allBooksList.clear();
        allBooksList.addAll(dbHelper.getAllBooks());
        bookAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Loaded " + allBooksList.size() + " books", Toast.LENGTH_SHORT).show();
    }

    private void filterBooks(String query) {
        List<BookModel> filteredList = new ArrayList<>();
        for (BookModel book : allBooksList) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(book);
            }
        }
        bookAdapter.updateBooks(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooksFromDatabase(); // refresh
    }
}