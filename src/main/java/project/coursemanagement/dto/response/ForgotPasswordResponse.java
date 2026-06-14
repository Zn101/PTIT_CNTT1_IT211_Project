package project.coursemanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ForgotPasswordResponse {

    private String resetToken;
    private String message;
}
