package project.coursemanagement.service;

import project.coursemanagement.dto.request.CreateUserRequest;
import project.coursemanagement.dto.request.UpdateUserRequest;
import project.coursemanagement.dto.response.PageResponse;
import project.coursemanagement.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    PageResponse<UserResponse> getAllUsers(
            int page,
            int size,
            String keyword
    );

    UserResponse updateUser(
            Long id,
            UpdateUserRequest request
    );

    void deactivateUser(Long id);
}
