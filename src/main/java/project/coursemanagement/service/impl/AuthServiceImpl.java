package project.coursemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.coursemanagement.dto.request.LoginRequest;
import project.coursemanagement.dto.request.RefreshTokenRequest;
import project.coursemanagement.dto.request.RegisterRequest;
import project.coursemanagement.dto.response.AuthResponse;
import project.coursemanagement.dto.response.UserResponse;
import project.coursemanagement.entity.User;
import project.coursemanagement.enums.RoleEnum;
import project.coursemanagement.exception.DuplicateResourceException;
import project.coursemanagement.exception.InvalidStateException;
import project.coursemanagement.exception.ResourceNotFoundException;
import project.coursemanagement.mapper.UserMapper;
import project.coursemanagement.repository.UserRepository;
import project.coursemanagement.security.JwtService;
import project.coursemanagement.service.AuthService;
import project.coursemanagement.service.TokenBlacklistService;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(RoleEnum.STUDENT)
                .isActive(true)
                .build();

        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(userDetails))
                .refreshToken(jwtService.generateRefreshToken(userDetails))
                .build();
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String username = jwtService.extractUsername(request.getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(request.getRefreshToken(), userDetails)) {
            throw new InvalidStateException("Invalid refresh token");
        }

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(userDetails))
                .refreshToken(request.getRefreshToken())
                .build();
    }

    @Override
    public void logout(String token) {
        Date expiration = jwtService.extractClaim(token, claims -> claims.getExpiration());
        long expirationMillis = expiration.getTime() - System.currentTimeMillis();

        if (expirationMillis > 0) {
            tokenBlacklistService.blacklistToken(token, expirationMillis);
        }
    }
}