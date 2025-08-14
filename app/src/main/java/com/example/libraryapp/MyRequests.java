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
import Classes.RequestDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MyRequests extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private List<Request> requestsList;
    private String username;
    private RequestDatabaseHelper requestDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        username = getIntent().getStringExtra("username");
        requestDbHelper = new RequestDatabaseHelper(this);

        setupRecyclerView();
        loadMyRequests();
    }

    private void setupRecyclerView() {
        requestsList = new ArrayList<>();
        requestAdapter = new RequestAdapter(this, requestsList, null); // no action listener for member view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(requestAdapter);
    }

    private void loadMyRequests() {
        if (username == null) {
            Toast.makeText(this, "Error: Username not found", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Loading your requests...", Toast.LENGTH_SHORT).show();

        // load requests from local database
        requestsList.clear();
        requestsList.addAll(requestDbHelper.getRequestsByUsername(username));
        requestAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Loaded " + requestsList.size() + " requests", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyRequests();
    }
}