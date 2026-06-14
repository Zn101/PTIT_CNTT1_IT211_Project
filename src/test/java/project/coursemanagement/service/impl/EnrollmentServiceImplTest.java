package project.coursemanagement.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.coursemanagement.dto.response.EnrollmentResponse;
import project.coursemanagement.entity.Course;
import project.coursemanagement.entity.Enrollment;
import project.coursemanagement.entity.User;
import project.coursemanagement.enums.RoleEnum;
import project.coursemanagement.exception.DuplicateResourceException;
import project.coursemanagement.repository.CourseRepository;
import project.coursemanagement.repository.EnrollmentRepository;
import project.coursemanagement.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Test
    void enroll_success() {
        User student = User.builder()
                .id(1L).username("student1")
                .role(RoleEnum.STUDENT).isActive(true)
                .build();

        Course course = Course.builder()
                .id(1L).courseCode("CS101")
                .courseName("Intro to Programming").credit(3)
                .build();

        Enrollment saved = Enrollment.builder()
                .id(1L).student(student).course(course)
                .enrolledAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(saved);

        EnrollmentResponse response = enrollmentService.enroll(1L, 1L);

        assertThat(response.getStudentId()).isEqualTo(1L);
        assertThat(response.getCourseCode()).isEqualTo("CS101");
    }

    @Test
    void enroll_duplicate_throwsException() {
        User student = User.builder()
                .id(1L).username("student1")
                .role(RoleEnum.STUDENT).isActive(true)
                .build();

        Course course = Course.builder()
                .id(1L).courseCode("CS101")
                .courseName("Intro to Programming").credit(3)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.enroll(1L, 1L))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already enrolled");

        verify(enrollmentRepository, never()).save(any());
    }
}