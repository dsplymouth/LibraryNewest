package com.example.libraryapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import Classes.Request;
import Classes.RequestAdapter;
import Classes.BookDatabaseHelper;
import Classes.RequestDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Requests extends AppCompatActivity implements RequestAdapter.OnRequestActionListener {
    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private List<Request> requestsList;
    private RequestDatabaseHelper requestDbHelper;
    private BookDatabaseHelper bookDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        requestDbHelper = new RequestDatabaseHelper(this);
        bookDbHelper = new BookDatabaseHelper(this);

        setupRecyclerView();
        loadAllRequests();
    }

    private void setupRecyclerView() {
        requestsList = new ArrayList<>();
        requestAdapter = new RequestAdapter(this, requestsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(requestAdapter);
    }
    private void loadAllRequests() {
        Toast.makeText(this, "Loading all requests...", Toast.LENGTH_SHORT).show();

        // load requests from local database
        requestsList.clear();
        requestsList.addAll(requestDbHelper.getAllRequests());
        requestAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Loaded " + requestsList.size() + " requests", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onApproveRequest(Request request) {
        List<Classes.BookModel> books = bookDbHelper.getAllBooks();
        for (Classes.BookModel book : books) {
            if (book.getId() == request.getBookId()) {
                book.setQuantity(book.getQuantity() - 1);
                bookDbHelper.updateBook(book.getTitle(), book.getTitle(), book.getQuantity());
                break;
            }
        }


        requestDbHelper.deleteRequest(request.getId());

        Toast.makeText(this, "Request approved for: " + request.getBookTitle(), Toast.LENGTH_SHORT).show();
        loadAllRequests(); // refresh the list
    }

    @Override
    public void onDenyRequest(Request request) {

        requestDbHelper.deleteRequest(request.getId());

        Toast.makeText(this, "Request denied for: " + request.getBookTitle(), Toast.LENGTH_SHORT).show();
        loadAllRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllRequests();
    }
}