package project.coursemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.coursemanagement.dto.request.CreateUserRequest;
import project.coursemanagement.dto.request.UpdateUserRequest   ;
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
                .isActive(request.getIsActive())
                .build();

        return UserMapper.toResponse(
                userRepository.save(user)
        );
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return UserMapper.toResponse(user);
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(
            int page,
            int size,
            String keyword
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<User> userPage;

        if (keyword != null && !keyword.isBlank()) {

            List<User> users =
                    userRepository.findByUsernameContainingIgnoreCase(keyword);

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), users.size());

            List<User> pageContent =
                    users.subList(start, end);

            userPage = new PageImpl<>(
                    pageContent,
                    pageable,
                    users.size()
            );

        } else {
            userPage = userRepository.findAll(pageable);
        }

        List<UserResponse> responses =
                userPage.getContent()
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
    public UserResponse updateUser(
            Long id,
            UpdateUserRequest request
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setUsername(request.getUsername());
        user.setRole(request.getRole());
        user.setIsActive(request.getIsActive());

        return UserMapper.toResponse(
                userRepository.save(user)
        );
    }

    @Override
    public void deactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setIsActive(false);

        userRepository.save(user);
    }
}
