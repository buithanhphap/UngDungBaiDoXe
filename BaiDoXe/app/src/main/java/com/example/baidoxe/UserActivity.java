package com.example.baidoxe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class UserActivity extends AppCompatActivity {

    private Button btnParkingLot, btnPrice, btnMap;
    private ImageView powerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo các nút từ giao diện
        btnParkingLot = findViewById(R.id.btnSoDen); // Nút "Bãi đỗ"
        btnPrice = findViewById(R.id.btnSo); // Nút "Giá xe"
        btnMap = findViewById(R.id.btnMap); // Nút "Map"
        powerButton = findViewById(R.id.powerButton); // Nút nguồn

        // Xử lý sự kiện nhấn nút "Bãi đỗ"
        btnParkingLot.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, ParkingLotActivity.class);
            startActivity(intent);
            Toast.makeText(UserActivity.this, "Xem bãi đỗ", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện nhấn nút "Giá xe"
        btnPrice.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, PriceActivity.class);
            startActivity(intent);
            Toast.makeText(UserActivity.this, "Xem giá xe", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện nhấn nút "Map"
        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, MapActivity.class);
            startActivity(intent);
            Toast.makeText(UserActivity.this, "Xem bản đồ", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện nhấn nút nguồn
        powerButton.setOnClickListener(v -> {
            // Tạo dialog xác nhận đăng xuất
            AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
            builder.setTitle("Xác nhận");
            builder.setMessage("Bạn chắc chắn muốn đăng xuất?");
            builder.setPositiveButton("Chắc", (dialog, which) -> {
                // Thực hiện đăng xuất
                mAuth.signOut();
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("Không", (dialog, which) -> {
                // Hủy dialog
                dialog.dismiss();
            });
            builder.show();
        });
    }
}