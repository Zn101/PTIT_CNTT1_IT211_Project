package project.coursemanagement.dto.response;

import lombok.*;
import project.coursemanagement.enums.RoleEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private RoleEnum role;

    private Boolean isActive;
}
