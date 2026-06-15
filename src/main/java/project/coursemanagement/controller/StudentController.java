package project.coursemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.coursemanagement.dto.response.EnrollmentResponse;
import project.coursemanagement.dto.response.LectureMaterialResponse;
import project.coursemanagement.dto.response.PageResponse;
import project.coursemanagement.dto.response.SubmissionResponse;
import project.coursemanagement.entity.User;
import project.coursemanagement.service.EnrollmentService;
import project.coursemanagement.service.LectureMaterialService;
import project.coursemanagement.service.SubmissionService;
import project.coursemanagement.service.UserSecurityService;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final EnrollmentService enrollmentService;
    private final SubmissionService submissionService;
    private final LectureMaterialService lectureMaterialService;
    private final UserSecurityService userSecurityService;

    @PostMapping("/enrollments")
    public ResponseEntity<EnrollmentResponse> enroll(
            @RequestParam Long courseId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User student = userSecurityService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enroll(student.getId(), courseId));
    }

    @PostMapping("/submissions/upload")
    public ResponseEntity<SubmissionResponse> uploadSubmission(
            @RequestParam Long courseId,
            @RequestPart MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User student = userSecurityService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(submissionService.uploadSubmission(courseId, student.getId(), file));
    }

    @GetMapping("/courses/{courseId}/materials")
    public ResponseEntity<PageResponse<LectureMaterialResponse>> getMaterials(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User student = userSecurityService.getCurrentUser(userDetails.getUsername());
        validateEnrollment(student.getId(), courseId);
        return ResponseEntity.ok(lectureMaterialService.getMaterialsByCourse(courseId, page, size));
    }

    private void validateEnrollment(Long studentId, Long courseId) {
        if (!enrollmentService.isEnrolled(studentId, courseId)) {
            throw new project.coursemanagement.exception.InvalidStateException(
                    "Student is not enrolled in this course");
        }
    }
}