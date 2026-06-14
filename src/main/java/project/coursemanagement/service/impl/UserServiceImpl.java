package project.coursemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.coursemanagement.dto.request.CreateUserRequest;
import project.coursemanagement.dto.request.UpdateUserRequest;
import project.coursemanagement.dto.response.PageResponse;
import project.coursemanagement.dto.response.UserResponse;
import project.coursemanagement.entity.User;
import project.coursemanagement.exception.DuplicateResourceException;
import project.coursemanagement.exception.ResourceNotFoundException;
import project.coursemanagement.mapper.UserMapper;
import project.coursemanagement.repository.UserRepository;
import project.coursemanagement.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();

        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);

        Page<User> userPage = (keyword != null && !keyword.isBlank())
                ? userRepository.findByUsernameContainingIgnoreCase(keyword, pageable)
                : userRepository.findAll(pageable);

        List<UserResponse> responses = userPage.getContent()
                .stream()
                .map(UserMapper::toResponse)
                .toList();

        return PageResponse.<UserResponse>builder()
                .content(responses)
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .last(userPage.isLast())
                .build();
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setRole(request.getRole());
        user.setIsActive(request.getIsActive());

        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setIsActive(false);
        userRepository.save(user);
    }
}