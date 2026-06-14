package project.coursemanagement.dto.response;

import lombok.Builder;
import lombok.Getter;
import project.coursemanagement.enums.SubmissionStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class SubmissionResponse {

    private Long id;
    private Long studentId;
    private String studentUsername;
    private Long lecturerId;
    private String lecturerUsername;
    private Long courseId;
    private String courseCode;
    private String courseName;
    private String reportUrl;
    private Double score;
    private String feedback;
    private SubmissionStatus status;
    private LocalDateTime submittedAt;
}