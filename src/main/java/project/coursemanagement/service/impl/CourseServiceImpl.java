package project.coursemanagement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.coursemanagement.dto.request.CreateCourseRequest;
import project.coursemanagement.dto.request.UpdateCourseRequest;
import project.coursemanagement.dto.response.CourseResponse;
import project.coursemanagement.dto.response.PageResponse;
import project.coursemanagement.entity.Course;
import project.coursemanagement.exception.DuplicateResourceException;
import project.coursemanagement.exception.ResourceNotFoundException;
import project.coursemanagement.mapper.CourseMapper;
import project.coursemanagement.repository.CourseRepository;
import project.coursemanagement.service.CourseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public CourseResponse createCourse(CreateCourseRequest request) {
        if (courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new DuplicateResourceException("Course code already exists: " + request.getCourseCode());
        }

        Course course = Course.builder()
                .courseCode(request.getCourseCode())
                .courseName(request.getCourseName())
                .credit(request.getCredit())
                .build();

        return CourseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(CourseMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    @Override
    public PageResponse<CourseResponse> getAllCourses(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Course> coursePage = (keyword != null && !keyword.isBlank())
                ? courseRepository.searchByKeyword(keyword, pageable)
                : courseRepository.findAll(pageable);

        List<CourseResponse> responses = coursePage.getContent()
                .stream()
                .map(CourseMapper::toResponse)
                .toList();

        return PageResponse.<CourseResponse>builder()
                .content(responses)
                .page(coursePage.getNumber())
                .size(coursePage.getSize())
                .totalElements(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .last(coursePage.isLast())
                .build();
    }

    @Override
    public CourseResponse updateCourse(Long id, UpdateCourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        if (!course.getCourseCode().equals(request.getCourseCode())
                && courseRepository.existsByCourseCode(request.getCourseCode())) {
            throw new DuplicateResourceException("Course code already exists: " + request.getCourseCode());
        }

        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setCredit(request.getCredit());

        return CourseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}