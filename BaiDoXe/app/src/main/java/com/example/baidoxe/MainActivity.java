package com.example.baidoxe;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String ADMIN_EMAIL = "admin@baidoxe.com";
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView signupText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private boolean isPasswordVisible = false;
    private boolean isTransitioning = false; // Biến cờ để ngăn chặn lặp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Khởi tạo các thành phần giao diện
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupText = findViewById(R.id.signupText);

        // Xử lý toggle mật khẩu
        passwordEditText.setOnTouchListener((v, event) -> {
            if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[2].getBounds().width())) {
                togglePasswordVisibility();
                return true;
            }
            return false;
        });

        // Xử lý sự kiện nhấn nút đăng nhập
        loginButton.setOnClickListener(v -> {
            String email = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isTransitioning) return; // Ngăn chặn lặp nếu đang chuyển

            // Đặt cờ trước khi bắt đầu quá trình
            isTransitioning = true;

            // Đăng nhập với Firebase Authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Login successful for email: " + email);
                            String userId = email.replace(".", ",");
                            if (email.equalsIgnoreCase(ADMIN_EMAIL)) {
                                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                Toast.makeText(MainActivity.this, "Đăng nhập admin thành công!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                            } else {
                                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Boolean locked = dataSnapshot.child("locked").getValue(Boolean.class);
                                        if (locked != null && locked) {
                                            Log.e(TAG, "Account is locked: " + email);
                                            Toast.makeText(MainActivity.this, "Tài khoản đã bị khóa!", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                        } else {
                                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                                            intent.putExtra("username", email);
                                            Toast.makeText(MainActivity.this, "Đăng nhập người dùng thành công!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        }
                                        isTransitioning = false; // Đặt lại cờ sau khi hoàn tất
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e(TAG, "Database error: " + databaseError.getMessage());
                                        Toast.makeText(MainActivity.this, "Lỗi khi kiểm tra trạng thái tài khoản: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        isTransitioning = false; // Đặt lại cờ nếu bị hủy
                                    }
                                });
                            }
                        } else {
                            Log.e(TAG, "Login failed: " + task.getException().getMessage());
                            Toast.makeText(MainActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            isTransitioning = false; // Đặt lại cờ nếu thất bại
                        }
                    });
        });

        // Xử lý sự kiện nhấn vào "Đăng ký ngay"
        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Chuyển đến màn hình đăng ký", Toast.LENGTH_SHORT).show();
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_24, 0);
        } else {
            passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_lock_24, 0, R.drawable.ic_baseline_visibility_off_24, 0);
        }
        passwordEditText.setSelection(passwordEditText.getText().length()); // Giữ con trỏ cuối chuỗi
        isPasswordVisible = !isPasswordVisible;
    }
}