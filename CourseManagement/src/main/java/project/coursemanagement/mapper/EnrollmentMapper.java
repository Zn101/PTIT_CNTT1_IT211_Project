package project.coursemanagement.mapper;

import project.coursemanagement.dto.response.EnrollmentResponse;
import project.coursemanagement.entity.Enrollment;

public class EnrollmentMapper {

    private EnrollmentMapper() {}

    public static EnrollmentResponse toResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentUsername(enrollment.getStudent().getUsername())
                .courseId(enrollment.getCourse().getId())
                .courseCode(enrollment.getCourse().getCourseCode())
                .courseName(enrollment.getCourse().getCourseName())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }
}
