package project.coursemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.coursemanagement.dto.response.LectureMaterialResponse;
import project.coursemanagement.dto.response.PageResponse;
import project.coursemanagement.entity.Course;
import project.coursemanagement.entity.LectureMaterial;
import project.coursemanagement.exception.ResourceNotFoundException;
import project.coursemanagement.mapper.LectureMaterialMapper;
import project.coursemanagement.repository.CourseRepository;
import project.coursemanagement.repository.LectureMaterialRepository;
import project.coursemanagement.service.CloudinaryService;
import project.coursemanagement.service.LectureMaterialService;
import project.coursemanagement.util.FileValidationUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureMaterialServiceImpl implements LectureMaterialService {

    private final LectureMaterialRepository lectureMaterialRepository;
    private final CourseRepository courseRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public LectureMaterialResponse uploadMaterial(Long courseId, MultipartFile file) {
        FileValidationUtil.validate(file);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        String fileUrl = cloudinaryService.uploadFile(file);

        LectureMaterial material = LectureMaterial.builder()
                .fileName(file.getOriginalFilename())
                .fileUrl(fileUrl)
                .course(course)
                .build();

        return LectureMaterialMapper.toResponse(lectureMaterialRepository.save(material));
    }

    @Override
    public PageResponse<LectureMaterialResponse> getMaterialsByCourse(Long courseId, int page, int size) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }

        Page<LectureMaterial> materialPage = lectureMaterialRepository
                .findByCourseId(courseId, PageRequest.of(page, size));

        List<LectureMaterialResponse> responses = materialPage.getContent()
                .stream()
                .map(LectureMaterialMapper::toResponse)
                .toList();

        return PageResponse.<LectureMaterialResponse>builder()
                .content(responses)
                .page(materialPage.getNumber())
                .size(materialPage.getSize())
                .totalElements(materialPage.getTotalElements())
                .totalPages(materialPage.getTotalPages())
                .last(materialPage.isLast())
                .build();
    }
}