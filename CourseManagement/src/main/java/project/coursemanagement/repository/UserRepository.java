package project.coursemanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import project.coursemanagement.entity.User;
import project.coursemanagement.enums.RoleEnum;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByRole(RoleEnum role);

    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
