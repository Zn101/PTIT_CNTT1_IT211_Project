package project.coursemanagement.service;

import project.coursemanagement.dto.request.CreateCourseRequest;
import project.coursemanagement.dto.request.UpdateCourseRequest;
import project.coursemanagement.dto.response.CourseResponse;
import project.coursemanagement.dto.response.PageResponse;

public interface CourseService {

    CourseResponse createCourse(CreateCourseRequest request);

    CourseResponse getCourseById(Long id);

    PageResponse<CourseResponse> getAllCourses(int page, int size, String keyword);

    CourseResponse updateCourse(Long id, UpdateCourseRequest request);

    void deleteCourse(Long id);
}