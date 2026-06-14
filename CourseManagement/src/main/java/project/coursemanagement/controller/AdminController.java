package project.coursemanagement.controller;

import project.coursemanagement.dto.request.*;
import project.coursemanagement.dto.response.*;
import project.coursemanagement.service.CourseService;
import project.coursemanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final CourseService courseService;

    // ================= USER =================

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    @GetMapping("/users")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {

        return ResponseEntity.ok(
                userService.getAllUsers(
                        page,
                        size,
                        keyword
                )
        );
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {

        return ResponseEntity.ok(
                userService.updateUser(id, request)
        );
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deactivateUser(
            @PathVariable Long id
    ) {

        userService.deactivateUser(id);

        return ResponseEntity.noContent().build();
    }

    // ================= COURSE =================

    @PostMapping("/courses")
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CreateCourseRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createCourse(request));
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseResponse> getCourseById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                courseService.getCourseById(id)
        );
    }

    @GetMapping("/courses")
    public ResponseEntity<PageResponse<CourseResponse>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {

        return ResponseEntity.ok(
                courseService.getAllCourses(
                        page,
                        size,
                        keyword
                )
        );
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequest request
    ) {

        return ResponseEntity.ok(
                courseService.updateCourse(id, request)
        );
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long id
    ) {

        courseService.deleteCourse(id);

        return ResponseEntity.noContent().build();
    }
}
