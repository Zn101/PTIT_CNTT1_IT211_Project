package project.coursemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.coursemanagement.dto.request.GradeSubmissionRequest;
import project.coursemanagement.dto.response.SubmissionResponse;
import project.coursemanagement.entity.Course;
import project.coursemanagement.entity.Submission;
import project.coursemanagement.entity.User;
import project.coursemanagement.enums.RoleEnum;
import project.coursemanagement.enums.SubmissionStatus;
import project.coursemanagement.exception.InvalidStateException;
import project.coursemanagement.exception.ResourceNotFoundException;
import project.coursemanagement.mapper.SubmissionMapper;
import project.coursemanagement.repository.CourseRepository;
import project.coursemanagement.repository.EnrollmentRepository;
import project.coursemanagement.repository.SubmissionRepository;
import project.coursemanagement.repository.UserRepository;
import project.coursemanagement.service.CloudinaryService;
import project.coursemanagement.service.SubmissionService;
import project.coursemanagement.util.FileValidationUtil;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public SubmissionResponse uploadSubmission(Long courseId, Long studentId, MultipartFile file) {
        FileValidationUtil.validate(file);

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        if (student.getRole() != RoleEnum.STUDENT) {
            throw new InvalidStateException("Only students can submit assignments");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        if (!enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new InvalidStateException("Student is not enrolled in this course");
        }

        String reportUrl = cloudinaryService.uploadFile(file);

        Submission submission = Submission.builder()
                .student(student)
                .course(course)
                .reportUrl(reportUrl)
                .status(SubmissionStatus.SUBMITTED)
                .build();

        return SubmissionMapper.toResponse(submissionRepository.save(submission));
    }

    @Override
    public SubmissionResponse gradeSubmission(GradeSubmissionRequest request, Long lecturerId) {
        User lecturer = userRepository.findById(lecturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found with id: " + lecturerId));

        if (lecturer.getRole() != RoleEnum.LECTURER) {
            throw new InvalidStateException("Only lecturers can grade submissions");
        }

        Submission submission = submissionRepository.findById(request.getSubmissionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Submission not found with id: " + request.getSubmissionId()));

        if (submission.getStatus() != SubmissionStatus.SUBMITTED) {
            throw new InvalidStateException(
                    "Cannot grade submission with status: " + submission.getStatus());
        }

        submission.setScore(request.getScore());
        submission.setFeedback(request.getFeedback());
        submission.setStatus(SubmissionStatus.GRADED);
        submission.setLecturer(lecturer);

        return SubmissionMapper.toResponse(submissionRepository.save(submission));
    }
}