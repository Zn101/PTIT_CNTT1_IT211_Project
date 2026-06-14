package project.coursemanagement.mapper;

import project.coursemanagement.dto.response.CourseResponse;
import project.coursemanagement.entity.Course;

public class CourseMapper {

    private CourseMapper() {
    }

    public static CourseResponse toResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .courseName(course.getCourseName())
                .credit(course.getCredit())
                .build();
    }
}
