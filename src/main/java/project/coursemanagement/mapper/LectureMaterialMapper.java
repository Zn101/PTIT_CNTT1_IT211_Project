package project.coursemanagement.mapper;

import project.coursemanagement.dto.response.LectureMaterialResponse;
import project.coursemanagement.entity.LectureMaterial;

public class LectureMaterialMapper {

    private LectureMaterialMapper() {}

    public static LectureMaterialResponse toResponse(LectureMaterial material) {
        return LectureMaterialResponse.builder()
                .id(material.getId())
                .fileName(material.getFileName())
                .fileUrl(material.getFileUrl())
                .courseId(material.getCourse().getId())
                .courseCode(material.getCourse().getCourseCode())
                .courseName(material.getCourse().getCourseName())
                .uploadedAt(material.getUploadedAt())
                .build();
    }
}