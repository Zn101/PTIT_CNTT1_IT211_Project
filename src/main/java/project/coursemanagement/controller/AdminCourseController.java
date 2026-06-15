package project.coursemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.coursemanagement.dto.request.CreateCourseRequest;
import project.coursemanagement.dto.request.UpdateCourseRequest;
import project.coursemanagement.dto.response.CourseResponse;
import project.coursemanagement.dto.response.PageResponse;
import project.coursemanagement.service.CourseService;

@RestController
@RequestMapping("/api/v1/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<CourseResponse>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(courseService.getAllCourses(page, size, keyword));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequest request
    ) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}