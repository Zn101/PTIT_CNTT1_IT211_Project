package project.coursemanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import project.coursemanagement.enums.RoleEnum;

@Getter
public class UpdateUserRequest {

    @NotNull(message = "Role is required")
    private RoleEnum role;

    @NotNull(message = "isActive is required")
    private Boolean isActive;
}