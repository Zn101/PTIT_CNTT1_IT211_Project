package project.coursemanagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import project.coursemanagement.entity.User;
import project.coursemanagement.enums.RoleEnum;
import project.coursemanagement.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin1")) {
            userRepository.save(User.builder()
                    .username("admin1")
                    .passwordHash(passwordEncoder.encode("123456"))
                    .role(RoleEnum.ADMIN)
                    .isActive(true)
                    .build());
        }

        if (!userRepository.existsByUsername("lecturer1")) {
            userRepository.save(User.builder()
                    .username("lecturer1")
                    .passwordHash(passwordEncoder.encode("123456"))
                    .role(RoleEnum.LECTURER)
                    .isActive(true)
                    .build());
        }
    }
}