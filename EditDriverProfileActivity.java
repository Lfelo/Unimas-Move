package com.example.unimasmove;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unimasmove.data.models.BusDriver;
import com.example.unimasmove.data.repository.UserRepository;
import com.example.unimasmove.databinding.ActivityEditDriverProfileBinding;
import com.example.unimasmove.network.model.UserResponse;
import com.example.unimasmove.utils.SessionManager;

public class EditDriverProfileActivity extends AppCompatActivity {
    private ActivityEditDriverProfileBinding binding;
    private SessionManager session;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditDriverProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize SessionManager and UserRepository
        SessionManager.init(this);
        session = SessionManager.getInstance();
        userRepository = new UserRepository(this);

        loadDriverData();
        setupSaveButton();
    }

    private void loadDriverData() {
        binding.etUsername.setText(session.getUsername());
        binding.etName.setText(session.getName());
        binding.etEmail.setText(session.getEmail());
        binding.etMobileNumber.setText(session.getPhone());
        binding.etLicense.setText(session.getDriverLicense());
        binding.etBusNumber.setText(session.getBusNumber());
        binding.etPlateNumber.setText(session.getPlateNumber());
    }

    private void setupSaveButton() {
        binding.btnSave.setOnClickListener(v -> saveDriverProfile());
    }

    private void saveDriverProfile() {
        String username = binding.etUsername.getText().toString().trim();
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String phone = binding.etMobileNumber.getText().toString().trim();
        String license = binding.etLicense.getText().toString().trim();
        String busNumber = binding.etBusNumber.getText().toString().trim();
        String plateNumber = binding.etPlateNumber.getText().toString().trim();

        BusDriver driver = new BusDriver(
                session.getUserId(),
                "1", // switch status
                name,
                username,
                email,
                phone,
                "", // password not needed for update
                license,
                busNumber,
                plateNumber
        );

        userRepository.updateBusDriver(driver, new UserRepository.UserCallback() {
            @Override
            public void onSuccess() {
                // Option 1: Using the UserResponse approach
                UserResponse userResponse = new UserResponse(
                        driver.getUserID(),
                        driver.getName(),
                        driver.getUsername(),
                        driver.getEmail(),
                        "driver", // userType
                        driver.getMobileNumber(),
                        null, // studentId
                        null, // faculty
                        driver.getDriverLicense(),
                        driver.getBusNumber(),
                        driver.getPlateNumber()
                );
                session.saveUser(userResponse);

                // Option 2: Using the simpler save method
                /*
                session.saveUser(
                        driver.getUserID(),
                        driver.getName(),
                        driver.getUsername(),
                        driver.getEmail(),
                        driver.getMobileNumber(),
                        "driver"
                );
                */

                // Update driver-specific info
                session.setDriverInfo(
                        driver.getDriverLicense(),
                        driver.getBusNumber(),
                        driver.getPlateNumber()
                );

                runOnUiThread(() -> {
                    Toast.makeText(EditDriverProfileActivity.this,
                            "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() ->
                        Toast.makeText(EditDriverProfileActivity.this,
                                "Error: " + message, Toast.LENGTH_SHORT).show());
            }
        });
    }
}