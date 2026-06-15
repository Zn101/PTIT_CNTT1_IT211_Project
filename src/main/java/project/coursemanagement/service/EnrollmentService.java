package project.coursemanagement.service;

import project.coursemanagement.dto.response.EnrollmentResponse;

public interface EnrollmentService {

    EnrollmentResponse enroll(Long studentId, Long courseId);

    boolean isEnrolled(Long studentId, Long courseId);
}