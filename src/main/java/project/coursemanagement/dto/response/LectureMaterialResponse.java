package project.coursemanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LectureMaterialResponse {

    private Long id;
    private String fileName;
    private String fileUrl;
    private Long courseId;
    private String courseCode;
    private String courseName;
    private LocalDateTime uploadedAt;
}
