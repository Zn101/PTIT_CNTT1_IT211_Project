package project.coursemanagement.service;

public interface TokenBlacklistService {

    void blacklistToken(String token, long expirationMillis);

    boolean isTokenBlacklisted(String token);
}
