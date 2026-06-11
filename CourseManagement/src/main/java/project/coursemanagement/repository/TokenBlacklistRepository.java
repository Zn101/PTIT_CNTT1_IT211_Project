package project.coursemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coursemanagement.entity.TokenBlacklist;

public interface TokenBlacklistRepository
        extends JpaRepository<TokenBlacklist, Long> {

    boolean existsByTokenString(String tokenString);
}
