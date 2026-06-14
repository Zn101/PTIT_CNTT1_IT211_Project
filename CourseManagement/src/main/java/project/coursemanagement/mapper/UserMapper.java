package project.coursemanagement.mapper;

import project.coursemanagement.dto.response.UserResponse;
import project.coursemanagement.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .build();
    }
}
