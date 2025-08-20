package com.example.libraryapp;
import Classes.Request;
import Classes.RequestAdapter;
import Classes.BookDatabaseHelper;
import Classes.RequestDatabaseHelper;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Requests extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private List<Request> requestsList;
    private RequestDatabaseHelper requestDbHelper;
    private BookDatabaseHelper bookDbHelper; // might need this later

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
        requestAdapter = new RequestAdapter(this, requestsList, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(requestAdapter);
    }

    private void loadAllRequests() {
        Toast.makeText(this, "Loading all requests...", Toast.LENGTH_SHORT).show();

        requestsList.clear();
        requestsList.addAll(requestDbHelper.getAllRequests());
        requestAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Loaded " + requestsList.size() + " requests", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllRequests();
    }
}