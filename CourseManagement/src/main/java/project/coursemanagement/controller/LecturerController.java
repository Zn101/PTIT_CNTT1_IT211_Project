package project.coursemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.coursemanagement.dto.request.GradeSubmissionRequest;
import project.coursemanagement.dto.response.SubmissionResponse;
import project.coursemanagement.entity.User;
import project.coursemanagement.exception.ResourceNotFoundException;
import project.coursemanagement.repository.UserRepository;
import project.coursemanagement.service.SubmissionService;

@RestController
@RequestMapping("/api/v1/lecturer")
@RequiredArgsConstructor
public class LecturerController {

    private final SubmissionService submissionService;
    private final UserRepository userRepository;

    @PostMapping("/grades")
    public ResponseEntity<SubmissionResponse> gradeSubmission(
            @Valid @RequestBody GradeSubmissionRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User lecturer = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(
                submissionService.gradeSubmission(request, lecturer.getId())
        );
    }
}