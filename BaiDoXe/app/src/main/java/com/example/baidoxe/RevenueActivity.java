package com.example.baidoxe;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RevenueActivity extends AppCompatActivity {

    private static final String TAG = "RevenueActivity";
    private TextView tvTotalRevenue;
    private Button btnBack;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        // Khởi tạo Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("revenue");

        // Khởi tạo views
        tvTotalRevenue = findViewById(R.id.tv_total_revenue);
        btnBack = findViewById(R.id.btn_back);

        // Xử lý nút Quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        } else {
            Log.e(TAG, "Button btn_back is null");
        }

        // Lấy dữ liệu doanh thu từ Firebase
        loadRevenueData();
    }

    private void loadRevenueData() {
        mDatabase.child("total_cars_out").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer totalCarsOut = dataSnapshot.getValue(Integer.class);
                if (totalCarsOut != null) {
                    int totalRevenue = totalCarsOut * 50000; // Mỗi xe 50k
                    tvTotalRevenue.setText("Tổng doanh thu: " + totalRevenue + " VND");
                    Log.d(TAG, "Total cars out: " + totalCarsOut + ", Revenue: " + totalRevenue);
                } else {
                    tvTotalRevenue.setText("Tổng doanh thu: 0 VND");
                    Log.w(TAG, "No data for total_cars_out");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading revenue data: " + databaseError.getMessage());
                tvTotalRevenue.setText("Tổng doanh thu: 0 VND");
            }
        });
    }
}