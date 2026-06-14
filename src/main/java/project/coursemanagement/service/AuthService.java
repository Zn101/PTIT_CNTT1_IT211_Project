package project.coursemanagement.service;

import project.coursemanagement.dto.request.LoginRequest;
import project.coursemanagement.dto.request.RefreshTokenRequest;
import project.coursemanagement.dto.request.RegisterRequest;
import project.coursemanagement.dto.response.AuthResponse;
import project.coursemanagement.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    void logout(String token);
}