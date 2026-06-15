package project.coursemanagement.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;
import project.coursemanagement.dto.request.CreateCourseRequest;
import project.coursemanagement.dto.response.CourseResponse;
import project.coursemanagement.dto.response.PageResponse;
import project.coursemanagement.security.JwtAuthenticationFilter;
import project.coursemanagement.security.JwtService;
import project.coursemanagement.service.CourseService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminCourseController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @MockitoBean
    private CourseService courseService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCourse_success() throws Exception {
        CreateCourseRequest request = new CreateCourseRequest();
        request.setCourseCode("CS101");
        request.setCourseName("Intro to Programming");
        request.setCredit(3);

        CourseResponse response = CourseResponse.builder()
                .id(1L).courseCode("CS101")
                .courseName("Intro to Programming").credit(3)
                .build();

        when(courseService.createCourse(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/admin/courses")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseCode").value("CS101"))
                .andExpect(jsonPath("$.credit").value(3));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCourseById_success() throws Exception {
        CourseResponse response = CourseResponse.builder()
                .id(1L).courseCode("CS101")
                .courseName("Intro to Programming").credit(3)
                .build();

        when(courseService.getCourseById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/admin/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.courseCode").value("CS101"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllCourses_success() throws Exception {
        PageResponse<CourseResponse> pageResponse = PageResponse.<CourseResponse>builder()
                .content(List.of(CourseResponse.builder()
                        .id(1L).courseCode("CS101")
                        .courseName("Intro to Programming").credit(3)
                        .build()))
                .page(0).size(10).totalElements(1).totalPages(1).last(true)
                .build();

        when(courseService.getAllCourses(0, 10, null)).thenReturn(pageResponse);

        mockMvc.perform(get("/api/v1/admin/courses")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].courseCode").value("CS101"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}