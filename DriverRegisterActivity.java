package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unimasmove.data.repository.AuthRepository;
import com.example.unimasmove.databinding.ActivityDriverRegisterBinding;
import com.example.unimasmove.network.model.UserResponse;

public class DriverRegisterActivity extends AppCompatActivity {
    private ActivityDriverRegisterBinding binding;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = new AuthManager(this);

        // Set onClickListener for Register button
        binding.btnRegister.setOnClickListener(v -> registerDriver());
        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, DriverLoginActivity.class));
            finish();
        });
    }

    private void registerDriver() {
        String name = binding.etName.getText().toString().trim();
        String username = binding.etUsername.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String phone = binding.etMobileNumber.getText().toString().trim();
        String license = binding.etDriverLicense.getText().toString().trim();
        String busNumber = binding.etBusNumber.getText().toString().trim();
        String plateNumber = binding.etPlateNumber.getText().toString().trim();

        if (validateInputs(name, username, email, password, phone, license, busNumber, plateNumber)) {
            authManager.registerDriver(
                    name,
                    username,
                    email,
                    phone,
                    password,
                    license,
                    busNumber,
                    plateNumber,
                    new AuthRepository.AuthCallback<UserResponse>() {
                        @Override
                        public void onSuccess(UserResponse user, String token) {
                            Toast.makeText(DriverRegisterActivity.this,
                                    "Registration successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DriverRegisterActivity.this, RegistrationSuccessActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(DriverRegisterActivity.this,
                                    message, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean validateInputs(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
