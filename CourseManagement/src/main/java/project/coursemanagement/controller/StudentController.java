package project.coursemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.coursemanagement.dto.response.EnrollmentResponse;
import project.coursemanagement.dto.response.SubmissionResponse;
import project.coursemanagement.entity.User;
import project.coursemanagement.exception.ResourceNotFoundException;
import project.coursemanagement.repository.UserRepository;
import project.coursemanagement.service.EnrollmentService;
import project.coursemanagement.service.SubmissionService;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final EnrollmentService enrollmentService;
    private final SubmissionService submissionService;
    private final UserRepository userRepository;

    @PostMapping("/enrollments")
    public ResponseEntity<EnrollmentResponse> enroll(
            @RequestParam Long courseId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User student = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enroll(student.getId(), courseId));
    }

    @PostMapping("/submissions/upload")
    public ResponseEntity<SubmissionResponse> uploadSubmission(
            @RequestParam Long courseId,
            @RequestPart MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User student = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(
                submissionService.uploadSubmission(courseId, student.getId(), file)
        );
    }
}
