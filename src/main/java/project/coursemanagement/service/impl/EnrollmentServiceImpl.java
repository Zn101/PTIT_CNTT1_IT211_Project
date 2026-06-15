package project.coursemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.coursemanagement.dto.response.EnrollmentResponse;
import project.coursemanagement.entity.Course;
import project.coursemanagement.entity.Enrollment;
import project.coursemanagement.entity.User;
import project.coursemanagement.enums.RoleEnum;
import project.coursemanagement.exception.DuplicateResourceException;
import project.coursemanagement.exception.InvalidStateException;
import project.coursemanagement.exception.ResourceNotFoundException;
import project.coursemanagement.mapper.EnrollmentMapper;
import project.coursemanagement.repository.CourseRepository;
import project.coursemanagement.repository.EnrollmentRepository;
import project.coursemanagement.repository.UserRepository;
import project.coursemanagement.service.EnrollmentService;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public EnrollmentResponse enroll(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + studentId));

        if (student.getRole() != RoleEnum.STUDENT) {
            throw new InvalidStateException("Only students can enroll in courses");
        }

        if (!student.getIsActive()) {
            throw new InvalidStateException("Cannot enroll an inactive user");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new DuplicateResourceException("Student is already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        return EnrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    public boolean isEnrolled(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }
}