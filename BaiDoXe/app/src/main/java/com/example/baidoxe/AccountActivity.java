package com.example.baidoxe;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "AccountActivity";
    private RecyclerView rvAccounts;
    private TextView tvNoAccounts;
    private Button btnBack;
    private AccountAdapter accountAdapter;
    private List<Account> accountList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Khởi tạo Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Khởi tạo RecyclerView, TextView và Button
        rvAccounts = findViewById(R.id.rv_accounts);
        tvNoAccounts = findViewById(R.id.tv_no_accounts);
        btnBack = findViewById(R.id.btn_back);
        rvAccounts.setLayoutManager(new LinearLayoutManager(this));
        accountList = new ArrayList<>();
        accountAdapter = new AccountAdapter(accountList, this::toggleAccountLock);
        rvAccounts.setAdapter(accountAdapter);

        // Xử lý nút Quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        } else {
            Log.e(TAG, "Button btn_back is null");
        }

        // Tải danh sách tài khoản cố định
        loadFixedAccounts();
    }

    private void loadFixedAccounts() {
        // Danh sách tài khoản cố định
        List<String> fixedEmails = Arrays.asList(
                "user1@baidoxe.com",
                "user2@baidoxe.com",
                "user3@baidoxe.com",
                "user4@baidoxe.com"
        );

        accountList.clear();
        AtomicInteger loadedCount = new AtomicInteger(0);
        int totalAccounts = fixedEmails.size();

        for (String email : fixedEmails) {
            String userId = email.replace(".", ",");
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Boolean locked = dataSnapshot.child("locked").getValue(Boolean.class);
                    String role = dataSnapshot.child("role").getValue(String.class);
                    if (dataSnapshot.exists() && "user".equals(role)) {
                        accountList.add(new Account(email, locked != null && locked));
                    } else {
                        // Nếu node không tồn tại, tạo với role: user, locked: false
                        mDatabase.child("users").child(userId).child("role").setValue("user");
                        mDatabase.child("users").child(userId).child("locked").setValue(false);
                        accountList.add(new Account(email, false));
                    }
                    Log.d(TAG, "Loaded account: " + email + ", role: " + role + ", locked: " + locked);
                    if (loadedCount.incrementAndGet() == totalAccounts) {
                        accountAdapter.notifyDataSetChanged();
                        updateVisibility();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Error loading data for " + email + ": " + databaseError.getMessage());
                    accountList.add(new Account(email, false));
                    if (loadedCount.incrementAndGet() == totalAccounts) {
                        accountAdapter.notifyDataSetChanged();
                        updateVisibility();
                    }
                }
            });
        }
    }

    private void updateVisibility() {
        if (accountList.isEmpty()) {
            tvNoAccounts.setVisibility(View.VISIBLE);
            rvAccounts.setVisibility(View.GONE);
        } else {
            tvNoAccounts.setVisibility(View.GONE);
            rvAccounts.setVisibility(View.VISIBLE);
        }
    }

    private void toggleAccountLock(String email, boolean isLocked) {
        String userId = email.replace(".", ",");
        boolean newLockState = !isLocked;
        mDatabase.child("users").child(userId).child("locked").setValue(newLockState)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully set locked to " + newLockState + " for " + email);
                    Toast.makeText(AccountActivity.this, newLockState ? "Đã khóa tài khoản " + email : "Đã mở khóa tài khoản " + email, Toast.LENGTH_SHORT).show();
                    loadFixedAccounts();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to set locked to " + newLockState + " for " + email + ": " + e.getMessage());
                    Toast.makeText(AccountActivity.this, "Lỗi khi " + (newLockState ? "khóa" : "mở khóa") + " tài khoản: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}