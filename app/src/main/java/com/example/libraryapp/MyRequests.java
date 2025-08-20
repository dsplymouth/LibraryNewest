package com.example.libraryapp;
import Classes.Request;
import Classes.RequestAdapter;
import Classes.RequestDatabaseHelper;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyRequests extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private List<Request> requestsList;
    private RequestDatabaseHelper requestDbHelper;
    private String username;
    private Button buttonBack;

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

        buttonBack = findViewById(R.id.buttonBack);
        username = getIntent().getStringExtra("username");
        recyclerView = findViewById(R.id.recyclerView);
        requestDbHelper = new RequestDatabaseHelper(this);

        setupRecyclerView();
        setupButtonListeners();
        loadMyRequests();
    }

    private void setupRecyclerView() {
        requestsList = new ArrayList<>();
        requestAdapter = new RequestAdapter(this, requestsList, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(requestAdapter);
    }

    private void setupButtonListeners() {
        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadMyRequests() {
        Toast.makeText(this, "Loading your requests...", Toast.LENGTH_SHORT).show();
    // load only user's requests
        requestsList.clear();
        requestsList.addAll(requestDbHelper.getRequestsByUsername(username));
        requestAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Loaded " + requestsList.size() + " requests", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyRequests(); // refresh when returning to reload requests and if approve/changed itll update
    }
}