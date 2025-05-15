package com.example.unimasmove;

import android.content.Context;

import com.example.unimasmove.data.repository.AuthRepository;
import com.example.unimasmove.network.model.UserResponse;
import com.example.unimasmove.utils.SessionManager;
import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    private final SessionManager session;
    private final AuthRepository authRepository;

    public AuthManager(Context context) {
        SessionManager.init(context);  // Initialize SessionManager
        this.session = SessionManager.getInstance(); // Get the instance of SessionManager
        this.authRepository = new AuthRepository(context); // Initialize AuthRepository
    }

    // Login method
    public void login(String email, String password, String userType,
                      AuthRepository.AuthCallback<UserResponse> callback) {
        authRepository.login(email, password, userType,
                new AuthRepository.AuthCallback<UserResponse>() {
                    @Override
                    public void onSuccess(UserResponse userResponse, String token) {
                        handleSuccessfulAuth(userResponse, token); // Handle successful login
                        callback.onSuccess(userResponse, token); // Pass success to the callback
                    }

                    @Override
                    public void onError(String message) {
                        callback.onError(message); // Pass error message to the callback
                    }
                });
    }

    // Driver registration method using the new registerUser() method
    public void registerDriver(String username, String name, String email, String mobileNumber,
                               String password, String driverLicense, String busNumber, String plateNumber,
                               AuthRepository.AuthCallback<UserResponse> callback) {
        authRepository.registerUser(username, name, email, mobileNumber, password, "driver",
                driverLicense, busNumber + "," + plateNumber, new AuthRepository.AuthCallback<UserResponse>() {
                    @Override
                    public void onSuccess(UserResponse userResponse, String token) {
                        handleSuccessfulAuth(userResponse, token);
                        callback.onSuccess(userResponse, token);
                    }

                    @Override
                    public void onError(String message) {
                        callback.onError(message);
                    }
                });
    }

    // Student registration method using the new registerUser() method
    public void registerStudent(String username, String name, String email, String mobileNumber,
                                String password, String studentId, String faculty,
                                AuthRepository.AuthCallback<UserResponse> callback) {
        // Call registerUser with appropriate parameters
        authRepository.registerUser(username, name, email, mobileNumber, password, "student",
                studentId, faculty, new AuthRepository.AuthCallback<UserResponse>() {
                    @Override
                    public void onSuccess(UserResponse userResponse, String token) {
                        handleSuccessfulAuth(userResponse, token); // Handle successful registration
                        callback.onSuccess(userResponse, token); // Pass success to the callback
                    }

                    @Override
                    public void onError(String message) {
                        callback.onError(message); // Pass error message to the callback
                    }
                });
    }

    // Helper method to handle successful authentication or registration
    private void handleSuccessfulAuth(UserResponse userResponse, String token) {
        // Save the full user data to session
        session.saveUserSession(userResponse, token);

        // Also save the auth token separately
        session.saveAuthToken(token);
    }
}
