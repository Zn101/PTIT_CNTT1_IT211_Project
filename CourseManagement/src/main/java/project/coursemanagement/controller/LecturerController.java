package project.coursemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.coursemanagement.dto.request.GradeSubmissionRequest;
import project.coursemanagement.dto.response.LectureMaterialResponse;
import project.coursemanagement.dto.response.SubmissionResponse;
import project.coursemanagement.entity.User;
import project.coursemanagement.exception.ResourceNotFoundException;
import project.coursemanagement.repository.UserRepository;
import project.coursemanagement.service.LectureMaterialService;
import project.coursemanagement.service.SubmissionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lecturer")
@RequiredArgsConstructor
public class LecturerController {

    private final SubmissionService submissionService;
    private final LectureMaterialService lectureMaterialService;
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

    @PostMapping("/courses/{courseId}/materials")
    public ResponseEntity<LectureMaterialResponse> uploadMaterial(
            @PathVariable Long courseId,
            @RequestPart MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lectureMaterialService.uploadMaterial(courseId, file));
    }

    @GetMapping("/courses/{courseId}/materials")
    public ResponseEntity<List<LectureMaterialResponse>> getMaterials(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                lectureMaterialService.getMaterialsByCourse(courseId)
        );
    }
}