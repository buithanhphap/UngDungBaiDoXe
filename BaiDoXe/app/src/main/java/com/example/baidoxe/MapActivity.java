package com.example.baidoxe;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Khởi tạo views
        btnBack = findViewById(R.id.btn_back);

        // Xử lý nút Quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        } else {
            Log.e(TAG, "Button btn_back is null");
        }
    }
}