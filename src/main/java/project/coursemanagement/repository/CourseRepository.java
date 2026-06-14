package project.coursemanagement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.coursemanagement.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCourseCode(String courseCode);

    @Query("SELECT c FROM Course c WHERE " +
            "LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Course> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
