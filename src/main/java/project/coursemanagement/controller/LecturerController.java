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
import project.coursemanagement.dto.response.PageResponse;
import project.coursemanagement.dto.response.SubmissionResponse;
import project.coursemanagement.entity.User;
import project.coursemanagement.service.LectureMaterialService;
import project.coursemanagement.service.SubmissionService;
import project.coursemanagement.service.UserSecurityService;

@RestController
@RequestMapping("/api/v1/lecturer")
@RequiredArgsConstructor
public class LecturerController {

    private final SubmissionService submissionService;
    private final LectureMaterialService lectureMaterialService;
    private final UserSecurityService userSecurityService;

    @PostMapping("/grades")
    public ResponseEntity<SubmissionResponse> gradeSubmission(
            @Valid @RequestBody GradeSubmissionRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User lecturer = userSecurityService.getCurrentUser(userDetails.getUsername());
        return ResponseEntity.ok(submissionService.gradeSubmission(request, lecturer.getId()));
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
    public ResponseEntity<PageResponse<LectureMaterialResponse>> getMaterials(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(lectureMaterialService.getMaterialsByCourse(courseId, page, size));
    }
}