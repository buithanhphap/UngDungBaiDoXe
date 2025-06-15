package com.example.baidoxe;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParkingLotActivity extends AppCompatActivity {

    private static final String TAG = "ParkingLotActivity";
    private TextView tvSlot1, tvSlot2, tvSlot3, tvSlot4;
    private CardView cardSlot1, cardSlot2, cardSlot3, cardSlot4;
    private Button btnBack;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot);

        // Khởi tạo Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child("parking_slots");

        // Khởi tạo views
        try {
            tvSlot1 = findViewById(R.id.tv_slot1);
            tvSlot2 = findViewById(R.id.tv_slot2);
            tvSlot3 = findViewById(R.id.tv_slot3);
            tvSlot4 = findViewById(R.id.tv_slot4);
            cardSlot1 = findViewById(R.id.card_slot1);
            cardSlot2 = findViewById(R.id.card_slot2);
            cardSlot3 = findViewById(R.id.card_slot3);
            cardSlot4 = findViewById(R.id.card_slot4);
            btnBack = findViewById(R.id.btn_back);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views: " + e.getMessage());
            finish();
            return;
        }

        // Xử lý nút Quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        } else {
            Log.e(TAG, "Button btn_back is null");
        }

        // Đặt trạng thái mặc định trước khi lắng nghe Firebase
        initializeDefaultSlots();

        // Lắng nghe dữ liệu từ Firebase
        setupFirebaseListeners();
    }

    private void initializeDefaultSlots() {
        // Đặt trạng thái mặc định: trống xe
        updateSlotStatus(1, 0);
        updateSlotStatus(2, 0);
        updateSlotStatus(3, 0);
        updateSlotStatus(4, 0);

        // Khởi tạo node parking_slots nếu chưa tồn tại
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    mDatabase.child("slot1").setValue(0);
                    mDatabase.child("slot2").setValue(0);
                    mDatabase.child("slot3").setValue(0);
                    mDatabase.child("slot4").setValue(0);
                    Log.d(TAG, "Initialized default parking_slots");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error initializing parking_slots: " + databaseError.getMessage());
            }
        });
    }

    private void setupFirebaseListeners() {
        // Lắng nghe slot1 với kiểm tra null
        mDatabase.child("slot1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer status = dataSnapshot.getValue(Integer.class);
                if (dataSnapshot.exists() && status != null) {
                    updateSlotStatus(1, status);
                } else {
                    Log.w(TAG, "Invalid data for slot1: " + dataSnapshot.getValue());
                    updateSlotStatus(1, 0); // Mặc định trống nếu dữ liệu không hợp lệ
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading slot1: " + databaseError.getMessage());
                updateSlotStatus(1, 0); // Mặc định trống nếu lỗi
            }
        });

        // Lắng nghe slot2
        mDatabase.child("slot2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer status = dataSnapshot.getValue(Integer.class);
                if (dataSnapshot.exists() && status != null) {
                    updateSlotStatus(2, status);
                } else {
                    Log.w(TAG, "Invalid data for slot2: " + dataSnapshot.getValue());
                    updateSlotStatus(2, 0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading slot2: " + databaseError.getMessage());
                updateSlotStatus(2, 0);
            }
        });

        // Lắng nghe slot3
        mDatabase.child("slot3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer status = dataSnapshot.getValue(Integer.class);
                if (dataSnapshot.exists() && status != null) {
                    updateSlotStatus(3, status);
                } else {
                    Log.w(TAG, "Invalid data for slot3: " + dataSnapshot.getValue());
                    updateSlotStatus(3, 0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading slot3: " + databaseError.getMessage());
                updateSlotStatus(3, 0);
            }
        });

        // Lắng nghe slot4
        mDatabase.child("slot4").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer status = dataSnapshot.getValue(Integer.class);
                if (dataSnapshot.exists() && status != null) {
                    updateSlotStatus(4, status);
                } else {
                    Log.w(TAG, "Invalid data for slot4: " + dataSnapshot.getValue());
                    updateSlotStatus(4, 0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading slot4: " + databaseError.getMessage());
                updateSlotStatus(4, 0);
            }
        });
    }

    private void updateSlotStatus(int slotNumber, Integer status) {
        TextView textView;
        CardView cardView;
        switch (slotNumber) {
            case 1:
                textView = tvSlot1;
                cardView = cardSlot1;
                break;
            case 2:
                textView = tvSlot2;
                cardView = cardSlot2;
                break;
            case 3:
                textView = tvSlot3;
                cardView = cardSlot3;
                break;
            case 4:
                textView = tvSlot4;
                cardView = cardSlot4;
                break;
            default:
                return;
        }

        if (textView == null || cardView == null) {
            Log.e(TAG, "TextView or CardView is null for slot" + slotNumber);
            return;
        }

        try {
            if (status != null && status == 1) {
                textView.setText("Slot " + slotNumber + ": Có xe");
                cardView.setCardBackgroundColor(getResources().getColor(R.color.red, getTheme()));
            } else {
                textView.setText("Slot " + slotNumber + ": Trống xe");
                cardView.setCardBackgroundColor(getResources().getColor(R.color.white, getTheme()));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating slot" + slotNumber + ": " + e.getMessage());
            if (status != null && status == 1) {
                textView.setText("Slot " + slotNumber + ": Có xe");
                cardView.setCardBackgroundColor(0xFFF44336); // Màu đỏ mặc định
            } else {
                textView.setText("Slot " + slotNumber + ": Trống xe");
                cardView.setCardBackgroundColor(0xFFFFFFFF); // Màu trắng mặc định
            }
        }
        Log.d(TAG, "Updated slot" + slotNumber + ": " + (status == 1 ? "Có xe" : "Trống xe"));
    }
}