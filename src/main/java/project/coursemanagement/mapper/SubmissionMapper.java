package project.coursemanagement.mapper;

import project.coursemanagement.dto.response.SubmissionResponse;
import project.coursemanagement.entity.Submission;

public class SubmissionMapper {

    private SubmissionMapper() {}

    public static SubmissionResponse toResponse(Submission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .studentId(submission.getStudent().getId())
                .studentUsername(submission.getStudent().getUsername())
                .lecturerId(submission.getLecturer() != null ? submission.getLecturer().getId() : null)
                .lecturerUsername(submission.getLecturer() != null ? submission.getLecturer().getUsername() : null)
                .courseId(submission.getCourse().getId())
                .courseCode(submission.getCourse().getCourseCode())
                .courseName(submission.getCourse().getCourseName())
                .reportUrl(submission.getReportUrl())
                .score(submission.getScore())
                .feedback(submission.getFeedback())
                .status(submission.getStatus())
                .submittedAt(submission.getSubmittedAt())
                .build();
    }
}