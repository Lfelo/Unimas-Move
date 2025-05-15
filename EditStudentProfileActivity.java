package com.example.unimasmove;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unimasmove.data.models.Student;
import com.example.unimasmove.data.repository.UserRepository;
import com.example.unimasmove.databinding.ActivityEditStudentProfileBinding;
import com.example.unimasmove.network.model.UserResponse;
import com.example.unimasmove.utils.SessionManager;

public class EditStudentProfileActivity extends AppCompatActivity {
    private ActivityEditStudentProfileBinding binding;
    private SessionManager session;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditStudentProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize SessionManager and UserRepository
        SessionManager.init(this);
        session = SessionManager.getInstance();
        userRepository = new UserRepository(this);

        loadStudentData();
        setupSaveButton();
    }

    private void loadStudentData() {
        binding.etUsername.setText(session.getUsername());
        binding.etName.setText(session.getName());
        binding.etEmail.setText(session.getEmail());
        binding.etMobileNumber.setText(session.getPhone());
        binding.etStudentId.setText(session.getStudentId());
        binding.etFaculty.setText(session.getFaculty());
    }

    private void setupSaveButton() {
        binding.btnSave.setOnClickListener(v -> saveStudentProfile());
    }

    private void saveStudentProfile() {
        String username = binding.etUsername.getText().toString().trim();
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String phone = binding.etMobileNumber.getText().toString().trim();
        String studentId = binding.etStudentId.getText().toString().trim();
        String faculty = binding.etFaculty.getText().toString().trim();

        Student student = new Student(
                session.getUserId(),
                "1", // switch status (default to 1)
                name,
                username,
                email,
                phone,
                "", // password not needed for update
                studentId,
                faculty
        );

        userRepository.updateStudent(student, new UserRepository.UserCallback() {
            @Override
            public void onSuccess() {
                // Option 1: Using UserResponse object
                UserResponse userResponse = new UserResponse(
                        student.getUserID(),
                        student.getName(),
                        student.getUsername(),
                        student.getEmail(),
                        "student", // userType
                        student.getMobileNumber(),
                        student.getStudentId(),
                        student.getFaculty(),
                        null, // driverLicense
                        null, // busNumber
                        null  // plateNumber
                );
                session.saveUser(userResponse);

                // Option 2: Using individual parameters (alternative)
                /*
                session.saveUser(
                        student.getUserID(),
                        student.getName(),
                        student.getUsername(),
                        student.getEmail(),
                        student.getMobileNumber(),
                        "student"
                );
                */

                // Update student-specific info
                session.setStudentInfo(
                        student.getStudentId(),
                        student.getFaculty()
                );

                runOnUiThread(() -> {
                    Toast.makeText(EditStudentProfileActivity.this,
                            "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() ->
                        Toast.makeText(EditStudentProfileActivity.this,
                                "Error: " + message, Toast.LENGTH_SHORT).show());
            }
        });
    }
}