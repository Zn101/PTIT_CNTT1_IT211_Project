package project.coursemanagement.service;

import org.springframework.web.multipart.MultipartFile;
import project.coursemanagement.dto.response.LectureMaterialResponse;

import java.util.List;

public interface LectureMaterialService {

    LectureMaterialResponse uploadMaterial(Long courseId, MultipartFile file);

    List<LectureMaterialResponse> getMaterialsByCourse(Long courseId);
}