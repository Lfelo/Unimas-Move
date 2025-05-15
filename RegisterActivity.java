package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private Button btnStudent, btnDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnStudent = findViewById(R.id.btnStudent);
        btnDriver = findViewById(R.id.btnDriver);

        btnStudent.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, StudentLoginActivity.class);
            intent.putExtra("user_type", "student");
            startActivity(intent);
        });

        btnDriver.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, DriverLoginActivity.class);
            intent.putExtra("user_type", "bus_driver");
            startActivity(intent);
        });
    }
}