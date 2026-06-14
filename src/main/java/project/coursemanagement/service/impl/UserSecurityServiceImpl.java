package project.coursemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.coursemanagement.entity.User;
import project.coursemanagement.exception.ResourceNotFoundException;
import project.coursemanagement.repository.UserRepository;
import project.coursemanagement.service.UserSecurityService;

@Service
@RequiredArgsConstructor
public class UserSecurityServiceImpl implements UserSecurityService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}