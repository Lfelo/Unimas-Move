package com.example.unimasmove;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unimasmove.databinding.ActivityStudentRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StudentRegisterActivity extends AppCompatActivity {
    private ActivityStudentRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        binding.btnRegister.setOnClickListener(v -> registerStudent());
    }

    private void registerStudent() {
        // Get input data from fields
        String studentId = binding.etStudentId.getText().toString().trim();
        String faculty = binding.etFaculty.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String mobileNumber = binding.etMobileNumber.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();
        String name = "Student User"; // Default name, or you could modify this as per input

        // Validate input fields
        if (!validateInput(studentId, faculty, email, mobileNumber, password, confirmPassword)) {
            return;
        }

        // Use Firebase Authentication to create the user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration successful
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Save additional user data to Firestore
                        saveUserDataToFirestore(user.getUid(), name, email, mobileNumber, studentId, faculty);

                        // Show success message and navigate to the next screen
                        Toast.makeText(StudentRegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StudentRegisterActivity.this, RegistrationSuccessActivity.class));
                        finish();
                    } else {
                        // Registration failed, show error message
                        Toast.makeText(StudentRegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInput(String studentId, String faculty, String email,
                                  String mobileNumber, String password, String confirmPassword) {
        // Input validation logic
        if (studentId.isEmpty()) {
            binding.etStudentId.setError("Student ID is required");
            return false;
        }

        if (faculty.isEmpty()) {
            binding.etFaculty.setError("Faculty is required");
            return false;
        }

        if (email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Please enter a valid email");
            return false;
        }

        if (mobileNumber.isEmpty()) {
            binding.etMobileNumber.setError("Mobile number is required");
            return false;
        } else if (mobileNumber.length() < 10 || mobileNumber.length() > 15) {
            binding.etMobileNumber.setError("Please enter a valid mobile number");
            return false;
        }

        if (password.isEmpty()) {
            binding.etPassword.setError("Password is required");
            return false;
        } else if (password.length() < 6) {
            binding.etPassword.setError("Password must be at least 6 characters");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            binding.etConfirmPassword.setError("Please confirm your password");
            return false;
        } else if (!password.equals(confirmPassword)) {
            binding.etConfirmPassword.setError("Passwords do not match");
            return false;
        }

        if (!binding.cbAgreeTerms.isChecked()) {
            Toast.makeText(this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveUserDataToFirestore(String userId, String name, String email,
                                         String mobileNumber, String studentId, String faculty) {
        // Save additional user data to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a user data map
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("mobileNumber", mobileNumber);
        user.put("studentId", studentId);
        user.put("faculty", faculty);

        // Save the data to Firestore under the "users" collection
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // User data successfully saved to Firestore
                })
                .addOnFailureListener(e -> {
                    // Error occurred while saving data
                    Toast.makeText(StudentRegisterActivity.this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
