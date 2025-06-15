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

public class AdminActivity extends AppCompatActivity {

    private Button btnAccount, btnParkingLot, btnRevenue, btnVehicle;
    private ImageView powerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo các nút từ giao diện
        btnAccount = findViewById(R.id.btnSoDen10); // Nút "Tài khoản"
        btnParkingLot = findViewById(R.id.btnSoDen); // Nút "Bãi đỗ"
        btnRevenue = findViewById(R.id.btnSo); // Nút "Doanh thu"
        btnVehicle = findViewById(R.id.btn); // Nút "Xe vào/ra"
        powerButton = findViewById(R.id.powerButton); // Nút nguồn

        // Xử lý sự kiện nhấn nút "Tài khoản"
        btnAccount.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AccountActivity.class);
            startActivity(intent);
            Toast.makeText(AdminActivity.this, "Mở quản lý tài khoản", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện nhấn nút "Bãi đỗ"
        btnParkingLot.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ParkingLotActivity.class);
            startActivity(intent);
            Toast.makeText(AdminActivity.this, "Mở quản lý bãi đỗ", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện nhấn nút "Doanh thu"
        btnRevenue.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, RevenueActivity.class);
            startActivity(intent);
            Toast.makeText(AdminActivity.this, "Mở quản lý doanh thu", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện nhấn nút "Xe vào/ra"
        btnVehicle.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, VehicleActivity.class);
            startActivity(intent);
            Toast.makeText(AdminActivity.this, "Mở quản lý xe vào/ra", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện nhấn nút nguồn
        powerButton.setOnClickListener(v -> {
            // Tạo dialog xác nhận đăng xuất
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
            builder.setTitle("Xác nhận");
            builder.setMessage("Bạn chắc chắn muốn đăng xuất?");
            builder.setPositiveButton("Chắc", (dialog, which) -> {
                // Thực hiện đăng xuất
                mAuth.signOut();
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
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