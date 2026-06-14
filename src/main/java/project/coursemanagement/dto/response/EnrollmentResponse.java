package project.coursemanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EnrollmentResponse {

    private Long id;
    private Long studentId;
    private String studentUsername;
    private Long courseId;
    private String courseCode;
    private String courseName;
    private LocalDateTime enrolledAt;
}