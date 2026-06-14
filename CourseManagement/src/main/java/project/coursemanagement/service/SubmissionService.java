package project.coursemanagement.service;

import org.springframework.web.multipart.MultipartFile;
import project.coursemanagement.dto.request.GradeSubmissionRequest;
import project.coursemanagement.dto.response.SubmissionResponse;

public interface SubmissionService {

    SubmissionResponse gradeSubmission(GradeSubmissionRequest request, Long lecturerId);

    SubmissionResponse uploadSubmission(Long courseId, Long studentId, MultipartFile file);
}