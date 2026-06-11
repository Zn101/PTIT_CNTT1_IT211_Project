package project.coursemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.coursemanagement.dto.request.LoginRequest;
import project.coursemanagement.dto.request.RefreshTokenRequest;
import project.coursemanagement.dto.request.RegisterRequest;
import project.coursemanagement.dto.response.AuthResponse;
import project.coursemanagement.dto.response.UserResponse;
import project.coursemanagement.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        UserResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                authService.refreshToken(request)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            String authorizationHeader
    ) {

        String token = authorizationHeader
                .replace("Bearer ", "");

        authService.logout(token);

        return ResponseEntity.ok(
                "Logout successful"
        );
    }
}
