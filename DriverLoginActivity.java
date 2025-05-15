package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unimasmove.data.repository.AuthRepository;
import com.example.unimasmove.databinding.ActivityDriverLoginBinding;
import com.example.unimasmove.network.model.UserResponse;

public class DriverLoginActivity extends AppCompatActivity {
    private ActivityDriverLoginBinding binding;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = new AuthManager(this);

        binding.btnLogin.setOnClickListener(v -> loginDriver());
        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, DriverRegisterActivity.class));
        });
    }

    private void loginDriver() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        authManager.login(email, password, "driver", new AuthRepository.AuthCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse user, String token) {
                // Navigate to main activity after successful login
                Intent intent = new Intent(DriverLoginActivity.this, DriverDataEntryActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(DriverLoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}