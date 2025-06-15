package com.example.baidoxe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;

public class VehicleActivity extends AppCompatActivity {

    private static final String TAG = "VehicleActivity";
    private ImageView ivCarIn, ivCarOut;
    private TextView tvTimeIn, tvTimeOut;
    private Button btnBack;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        // Khởi tạo Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("vehicles");

        // Khởi tạo views
        ivCarIn = findViewById(R.id.iv_car_in);
        ivCarOut = findViewById(R.id.iv_car_out);
        tvTimeIn = findViewById(R.id.tv_time_in);
        tvTimeOut = findViewById(R.id.tv_time_out);
        btnBack = findViewById(R.id.btn_back);

        // Xử lý nút Quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        } else {
            Log.e(TAG, "Button btn_back is null");
        }

        // Lấy dữ liệu từ Firebase
        loadVehicleData();
    }

    private void loadVehicleData() {
        mDatabase.child("last_in_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lastInTime = dataSnapshot.child("time").getValue(String.class);
                String imageBase64 = dataSnapshot.child("image").getValue(String.class);
                if (lastInTime != null) {
                    tvTimeIn.setText("Thời gian vào: " + lastInTime);
                    if (imageBase64 != null) {
                        setImageFromBase64(ivCarIn, imageBase64);
                    }
                } else {
                    tvTimeIn.setText("Thời gian vào: Chưa có");
                    ivCarIn.setImageResource(R.drawable.ic_temp_car);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading last_in_time: " + databaseError.getMessage());
                tvTimeIn.setText("Thời gian vào: Chưa có");
                ivCarIn.setImageResource(R.drawable.ic_temp_car);
            }
        });

        mDatabase.child("last_out_time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lastOutTime = dataSnapshot.child("time").getValue(String.class);
                String imageBase64 = dataSnapshot.child("image").getValue(String.class);
                if (lastOutTime != null) {
                    tvTimeOut.setText("Thời gian ra: " + lastOutTime);
                    if (imageBase64 != null) {
                        setImageFromBase64(ivCarOut, imageBase64);
                    }
                } else {
                    tvTimeOut.setText("Thời gian ra: Chưa có");
                    ivCarOut.setImageResource(R.drawable.ic_temp_car);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading last_out_time: " + databaseError.getMessage());
                tvTimeOut.setText("Thời gian ra: Chưa có");
                ivCarOut.setImageResource(R.drawable.ic_temp_car);
            }
        });
    }

    private void setImageFromBase64(ImageView imageView, String base64String) {
        try {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e(TAG, "Error decoding base64 image: " + e.getMessage());
            imageView.setImageResource(R.drawable.ic_temp_car);
        }
    }
}