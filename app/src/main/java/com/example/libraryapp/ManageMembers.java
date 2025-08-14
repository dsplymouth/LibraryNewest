package com.example.libraryapp;

import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.lang.reflect.Type;
import Classes.MemberAdapter;
import Classes.MemberModel;
import Classes.Member;
import API.LibraryService;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

public class ManageMembers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MemberAdapter adapter;
    private List<MemberModel> memberList;
    private List<MemberModel> allMembersList;
    private EditText searchEditText;
    private Button buttonAddMember;
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_members);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewMembers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        memberList = new ArrayList<>();
        allMembersList = new ArrayList<>();

        adapter = new MemberAdapter(memberList, this);
        recyclerView.setAdapter(adapter);

        searchEditText = findViewById(R.id.searchViewManageMembers);
        buttonAddMember = findViewById(R.id.buttonAddMember);

        setupSearch();

        buttonAddMember.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditMember.class);
            intent.putExtra("mode", "add");
            startActivity(intent);
        });


        loadMembersFromAPI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMembersFromAPI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchHandler != null) {
            searchHandler.removeCallbacksAndMessages(null);
        }
    }

    private void loadMembersFromAPI() {
        Toast.makeText(this, "Loading members...", Toast.LENGTH_SHORT).show();
        
        LibraryService.getAllMembers(this, new API.LibraryService.MembersListCallback() {
            @Override
            public void onSuccess(List<Member> members) {
                runOnUiThread(() -> {
                    allMembersList.clear();
                    
                    for (Member member : members) {
                        MemberModel memberModel = new MemberModel(
                            member.getUsername(),
                            member.getFirstname(),
                            member.getLastname(),
                            member.getEmail(),
                            member.getContact(),
                            member.getMembershipEndDate()
                        );
                        allMembersList.add(memberModel);
                    }
                    
                    memberList.clear();
                    memberList.addAll(allMembersList);
                    adapter.notifyDataSetChanged();
                    
                    Toast.makeText(ManageMembers.this, 
                        "Loaded " + memberList.size() + " members", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ManageMembers.this, 
                        "Error loading members: " + error, Toast.LENGTH_SHORT).show();
                    loadSampleMembers();
                });
            }
        });
    }

    private void loadSampleMembers() {
        allMembersList.clear();
        allMembersList.add(new MemberModel("john123", "John", "Doe", "john@example.com", "1234567890", "2025-12-31"));
        allMembersList.add(new MemberModel("jane456", "Jane", "Smith", "jane@example.com", "0987654321", "2025-11-30"));

        memberList.clear();
        memberList.addAll(allMembersList);
        adapter.notifyDataSetChanged();
    }

    // ... existing code ...

    private void setupSearch() {
        searchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = new Runnable() {
                    @Override
                    public void run() {
                        filterMembers(s.toString());
                    }
                };
                searchHandler.postDelayed(searchRunnable, 300);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void filterMembers(String query) {
        new Thread(() -> {
            List<MemberModel> filteredList = new ArrayList<>();

            if (query.isEmpty()) {
                filteredList.addAll(allMembersList);
            } else {
                for (MemberModel member : allMembersList) {
                    if (member.getFullName().toLowerCase().contains(query.toLowerCase()) ||
                            member.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                            member.getEmail().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(member);
                    }
                }
            }

            runOnUiThread(() -> {
                memberList.clear();
                memberList.addAll(filteredList);
                adapter.notifyDataSetChanged();

                if (!query.isEmpty()) {
                    Toast.makeText(ManageMembers.this, "Found " + memberList.size() + " members", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}