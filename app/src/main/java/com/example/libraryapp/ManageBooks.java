package com.example.libraryapp;
import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.os.Handler;
import android.os.Looper;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import Classes.BookDatabaseHelper;
import Classes.BookModel;
import Classes.BookAdapter;

public class ManageBooks extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<BookModel> bookList;
    private List<BookModel> allBooksList;
    private BookDatabaseHelper dbHelper;
    private EditText searchEditText;
    private Button buttonAddBook;
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_books);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());


        dbHelper = new BookDatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookList = new ArrayList<>();
        allBooksList = new ArrayList<>(); // initialize all books list
        loadBooks();
        adapter = new BookAdapter(this, bookList, true, null); // true = staff mode (edit)
        recyclerView.setAdapter(adapter);

        searchEditText = findViewById(R.id.searchViewBooksMember);
        buttonAddBook = findViewById(R.id.buttonAddBook);

        setupSearch();

        buttonAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditBook.class);
            intent.putExtra("mode", "add");
            startActivity(intent);
        });
    }

    private void loadBooks() {
        allBooksList.clear();
        allBooksList.addAll(dbHelper.getAllBooks());

        bookList.clear();
        bookList.addAll(allBooksList); // initially show all books

        Log.d("ManageBooks", "Loaded " + bookList.size() + " books from database");
        }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                // delay search
                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        filterBooks(s.toString());
                    }
                };
                searchHandler.postDelayed(searchRunnable, 300); // 300ms delay
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void filterBooks(String query) {
        new Thread(() -> {
            List<BookModel> filteredList = new ArrayList<>();

            if (query.isEmpty()) {
                // show all books if search is empty
                filteredList.addAll(allBooksList);
            } else {
                // filter books by title
                for (BookModel book : allBooksList) {
                    if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(book);
                    }
                }
            }

            // update ui
            runOnUiThread(() -> {
                bookList.clear();
                bookList.addAll(filteredList);
                adapter.notifyDataSetChanged();


                if (!query.isEmpty()) {
                    Toast.makeText(ManageBooks.this, "Found " + bookList.size() + " books", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // reload books when returning
        loadBooks();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchHandler != null) {
            searchHandler.removeCallbacksAndMessages(null);
        }
    }
}