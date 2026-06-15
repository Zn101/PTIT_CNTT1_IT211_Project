package project.coursemanagement.service;

import org.springframework.web.multipart.MultipartFile;
import project.coursemanagement.dto.response.LectureMaterialResponse;
import project.coursemanagement.dto.response.PageResponse;

import java.util.List;

public interface LectureMaterialService {

    LectureMaterialResponse uploadMaterial(Long courseId, MultipartFile file);

    PageResponse<LectureMaterialResponse> getMaterialsByCourse(Long courseId, int page, int size);
}